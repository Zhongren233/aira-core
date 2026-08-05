package moe.aira.onebot.client.skyland;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

/**
 * 森空岛签到客户端，对应参考项目 skyland.py。
 * <p>
 * 流程：token → grant code → cred（含签名用的 token 与请求头用的 cred）→
 * 拉取绑定角色 → 逐角色执行签到。
 */
@Slf4j
@Component
public class SkylandClient {

    private static final ObjectMapper JSON = new ObjectMapper();

    private static final String USER_AGENT =
            "Skland/1.0.1 (com.hypergryph.skland; build:100001014; Android 31; ) Okhttp/4.11.0";
    private static final String APP_CODE = "4ca99fa6b56cc2ba";
    private static final String GRANT_URL = "https://as.hypergryph.com/user/oauth2/v2/grant";
    private static final String CRED_URL = "https://zonai.skland.com/web/v1/user/auth/generate_cred_by_code";
    private static final String BINDING_URL = "https://zonai.skland.com/api/v1/game/player/binding";
    private static final String ATTENDANCE_URL = "https://zonai.skland.com/api/v1/game/attendance";
    private static final String ENDFIELD_ATTENDANCE_URL = "https://zonai.skland.com/api/v1/game/endfield/attendance";
    private static final long ARKNIGHTS_GAME_ID = 1;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10)).build();

    /** 与参考项目一致：每次进程只生成一次 dId */
    private volatile String dId;

    public record CredInfo(String token, String cred) {
    }

    public record Character(String uid, String nickName, String channelName) {
    }

    public record SignResult(boolean success, String message) {
    }

    public record EndfieldRole(String roleId, String serverId, String nickname, String serverName) {
    }

    /** 一次 binding 请求返回的全部绑定角色，按游戏区分。 */
    public record BindingApps(List<Character> arknightsCharacters, List<EndfieldRole> endfieldRoles) {
    }

    /**
     * token 校验并换取 cred（绑定命令与每日签到共用）。校验失败抛出 {@link SkylandApiException}。
     */
    public CredInfo getCredByToken(String token) {
        String grantCode = getGrantCode(token);
        return getCred(grantCode);
    }

    /**
     * 一次性拉取全部绑定游戏的角色（明日方舟 + 终末地），
     * 避免按游戏分别请求 binding 接口。
     */
    public BindingApps getBindingApps(CredInfo info) {
        List<Character> arknights = new ArrayList<>();
        List<EndfieldRole> endfield = new ArrayList<>();
        for (JsonNode game : fetchBindingApps(info)) {
            String appCode = game.path("appCode").asText();
            if ("arknights".equals(appCode)) {
                for (JsonNode c : game.path("bindingList")) {
                    arknights.add(new Character(c.path("uid").asText(),
                            c.path("nickName").asText(), c.path("channelName").asText()));
                }
            } else if ("endfield".equals(appCode)) {
                collectEndfieldRoles(game.path("bindingList"), endfield);
            }
        }
        return new BindingApps(arknights, endfield);
    }

    /**
     * 终末地绑定可能含多个角色（roles 列表），全部收集并按 roleId 去重，
     * 不再只取 bindingList[0]/roles[0]。roles 缺失或为空时回退到 defaultRole
     * （defaultRole 可能为显式 null）。
     */
    private void collectEndfieldRoles(JsonNode bindingList, List<EndfieldRole> roles) {
        Set<String> seen = new HashSet<>();
        for (JsonNode binding : bindingList) {
            JsonNode defaultRole = binding.path("defaultRole");
            if (!defaultRole.isMissingNode() && !defaultRole.isNull()) {
                addEndfieldRole(defaultRole, roles, seen);
            }
            for (JsonNode role : binding.path("roles")) {
                addEndfieldRole(role, roles, seen);
            }
        }
    }

    private void addEndfieldRole(JsonNode role, List<EndfieldRole> roles, Set<String> seen) {
        String roleId = role.path("roleId").asText();
        if (roleId.isEmpty() || !seen.add(roleId)) {
            return;
        }
        roles.add(new EndfieldRole(roleId, role.path("serverId").asText(),
                role.path("nickname").asText(), role.path("serverName").asText()));
    }

    /**
     * 终末地每日签到：空 body，通过 sk-game-role 头指定角色。
     */
    public SignResult signEndfield(CredInfo info, EndfieldRole role) {
        String roleStr = "3_" + role.roleId() + "_" + role.serverId();
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("cred", info.cred());
        headers.put("User-Agent", USER_AGENT);
        headers.put("Accept-Encoding", "gzip");
        SkylandSignature.SignResult sign =
                SkylandSignature.signEndfield(info.token(), "/api/v1/game/endfield/attendance", "");
        headers.put("sign", sign.sign());
        headers.put("platform", "3");
        headers.put("timestamp", sign.timestamp());
        headers.put("dId", "");
        headers.put("vName", "1.0.0");
        headers.put("sk-game-role", roleStr);
        JsonNode node = parse(postJson(ENDFIELD_ATTENDANCE_URL, "", headers));
        if (node.path("code").asInt() != 0) {
            String message = node.path("message").asText("未知错误");
            // 请勿重复签到：当天已签到且 token 正常，属于正常行为
            if (message.startsWith("请勿重复签到") || message.startsWith("Please do not sign in again")) {
                return new SignResult(true,
                        "[终末地]" + role.nickname() + "(" + role.serverName() + ")今日已签到");
            }
            return new SignResult(false,
                    "[终末地]" + role.nickname() + "(" + role.serverName() + ")签到失败！原因：" + message);
        }
        StringBuilder sb = new StringBuilder("[终末地]").append(role.nickname()).append("(")
                .append(role.serverName()).append(")每日签到成功");
        JsonNode resourceMap = node.path("data").path("resourceInfoMap");
        List<String> awards = new ArrayList<>();
        for (JsonNode award : node.path("data").path("awardIds")) {
            JsonNode resource = resourceMap.path(award.path("id").asText());
            if (!resource.isMissingNode()) {
                awards.add(resource.path("name").asText() + "×" + resource.path("count").asLong(1));
            }
        }
        if (!awards.isEmpty()) {
            sb.append("，获得了").append(String.join("、", awards));
        }
        return new SignResult(true, sb.toString());
    }

    private JsonNode fetchBindingApps(CredInfo info) {
        String resp = getJson(BINDING_URL, signedHeaders(info, "/api/v1/game/player/binding", ""));
        JsonNode node = parse(resp);
        if (node.path("code").asInt() != 0) {
            String message = node.path("message").asText("未知错误");
            if ("用户未登录".equals(message)) {
                throw new SkylandLoginExpiredException("用户登录已失效，请重新绑定token");
            }
            throw new SkylandApiException("请求角色列表失败：" + message);
        }
        return node.path("data").path("list");
    }

    /**
     * 对单个角色执行签到。
     */
    public SignResult signArknights(CredInfo info, Character character) {
        // 注意：body 需与服务端签名校验一致，冒号后保留空格（与参考项目 json.dumps 默认格式一致）
        String body = "{\"gameId\": 1, \"uid\": \"" + character.uid() + "\"}";
        String resp = postJson(ATTENDANCE_URL, body, signedHeaders(info, "/api/v1/game/attendance", body));
        JsonNode node = parse(resp);
        if (node.path("code").asInt() != 0) {
            String message = node.path("message").asText("未知错误");
            // 请勿重复签到：当天已签到且 token 正常，属于正常行为（服务端文案可能带全角标点）
            if (message.startsWith("请勿重复签到")) {
                return new SignResult(true,
                        "[明日方舟]" + character.nickName() + "(" + character.channelName() + ")今日已签到");
            }
            return new SignResult(false,
                    "[明日方舟]" + character.nickName() + "(" + character.channelName() + ")签到失败！原因：" + message);
        }
        String label = "[明日方舟]" + character.nickName() + "(" + character.channelName() + ")";
        StringBuilder sb = new StringBuilder();
        for (JsonNode award : node.path("data").path("awards")) {
            JsonNode resource = award.path("resource");
            String name = resource.path("name").asText();
            long count = award.path("count").asLong(1);
            sb.append(label).append("签到成功，获得了").append(name).append("×").append(count).append('\n');
        }
        if (sb.length() == 0) {
            // 响应未携带奖励明细时也给出明确结果，避免日志出现空行
            sb.append(label).append("签到成功");
        }
        return new SignResult(true, sb.toString().trim());
    }

    private String getGrantCode(String token) {
        String body = "{\"appCode\":\"" + APP_CODE + "\",\"token\":\"" + token + "\",\"type\":0}";
        String resp = postJson(GRANT_URL, body, authHeaders());
        JsonNode node = parse(resp);
        if (!node.has("data") || node.path("status").asInt() != 0) {
            String message = node.path("msg").asText(node.path("message").asText("未知错误"));
            throw new SkylandApiException("获得认证代码失败：" + message);
        }
        return node.path("data").path("code").asText();
    }

    private CredInfo getCred(String grantCode) {
        String body = "{\"code\":\"" + grantCode + "\",\"kind\":1}";
        String resp = postJson(CRED_URL, body, authHeaders());
        JsonNode node = parse(resp);
        if (node.path("code").asInt() != 0) {
            throw new SkylandApiException("获得cred失败：" + node.path("message").asText());
        }
        return new CredInfo(node.path("data").path("token").asText(),
                node.path("data").path("cred").asText());
    }

    private Map<String, String> authHeaders() {
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("User-Agent", USER_AGENT);
        headers.put("Accept-Encoding", "gzip");
        headers.put("dId", getDId());
        return headers;
    }

    private Map<String, String> signedHeaders(CredInfo info, String path, String bodyOrQuery) {
        SkylandSignature.SignResult sign = SkylandSignature.sign(info.token(), path, bodyOrQuery);
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("cred", info.cred());
        headers.put("User-Agent", USER_AGENT);
        headers.put("Accept-Encoding", "gzip");
        headers.put("sign", sign.sign());
        headers.put("platform", "");
        headers.put("timestamp", sign.timestamp());
        headers.put("dId", "");
        headers.put("vName", "");
        return headers;
    }

    private String getDId() {
        String local = dId;
        if (local == null) {
            synchronized (this) {
                if (dId == null) {
                    dId = ShumeiDeviceId.getDId();
                }
                local = dId;
            }
        }
        return local;
    }

    private String getJson(String url, Map<String, String> headers) {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(15));
        headers.forEach(builder::header);
        return send(builder.GET().build());
    }

    private String postJson(String url, String body, Map<String, String> headers) {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .header("Content-Type", "application/json");
        headers.forEach(builder::header);
        return send(builder.POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8)).build());
    }

    private String send(HttpRequest request) {
        try {
            HttpResponse<byte[]> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofByteArray());
            byte[] body = response.body();
            // 手动设置了 Accept-Encoding 后 JDK HttpClient 不会自动解压 gzip
            if ("gzip".equalsIgnoreCase(response.headers()
                    .firstValue("Content-Encoding").orElse(""))) {
                body = gunzip(body);
            }
            String text = new String(body, StandardCharsets.UTF_8);
            if (log.isDebugEnabled()) {
                log.debug("{} {} -> {} {}", request.method(), request.uri(), response.statusCode(), text);
            }
            if (response.statusCode() >= 500) {
                throw new SkylandApiException("服务异常，HTTP " + response.statusCode());
            }
            return text;
        } catch (SkylandApiException e) {
            throw e;
        } catch (Exception e) {
            throw new SkylandApiException("请求失败：" + e.getMessage(), e);
        }
    }

    private static byte[] gunzip(byte[] data) {
        try (ByteArrayInputStream in = new ByteArrayInputStream(data);
             GZIPInputStream gzip = new GZIPInputStream(in);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            gzip.transferTo(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new SkylandApiException("响应解压失败", e);
        }
    }

    private JsonNode parse(String json) {
        try {
            return JSON.readTree(json);
        } catch (Exception e) {
            throw new SkylandApiException("响应解析失败：" + json, e);
        }
    }
}
