package moe.aira.onebot.client.skyland;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

/**
 * 数美设备指纹 dId 生成，对应参考项目 SecuritySm.py 的 get_d_id()。
 * <p>
 * 流程：RSA 加密 uid → 构造浏览器环境并做字段混淆(DES) → gzip → AES-CBC(密钥为 priId) →
 * POST https://fp-it.portal101.cn/deviceprofile/v4 换取 deviceId。
 */
public final class ShumeiDeviceId {

    private static final ObjectMapper JSON = new ObjectMapper();
    private static final HttpClient HTTP = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10)).build();

    private static final String ORGANIZATION = "UWXspnCCJN4sfYlNfqps";
    private static final String PUBLIC_KEY_B64 =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCmxMNr7n8ZeT0tE1R9j/mPixoinPkeM+k4VGIn/s0k7N5rJAfnZ0eMER+QhwFvshzo0LNmeUkpR8uIlU/GEVr8mN28sKmwd2gpygqj0ePnBmOW4v0ZVwbSYK+izkhVFk2V/doLoMbWy6b+UnA8mkjvg0iYWRByfRsK2gdl7llqCwIDAQAB";
    private static final String DEVICE_INFO_URL = "https://fp-it.portal101.cn/deviceprofile/v4";

    private record DesRule(int isEncrypt, String obfuscatedName, String key) {
    }

    private static final Map<String, DesRule> DES_RULES = new LinkedHashMap<>();

    static {
        putRule("appId", "uy7mzc4h", "xx");
        putRule("box", null, "jf");
        putRule("canvas", "snrn887t", "yk");
        putRule("clientSize", "cpmjjgsu", "zx");
        putRule("organization", "78moqjfc", "dp");
        putRule("os", "je6vk6t4", "pj");
        putRule("platform", "pakxhcd2", "gm");
        putRule("plugins", "v51m3pzl", "kq");
        putRule("pmf", "2mdeslu3", "vw");
        putRule("protocol", null, "protocol");
        putRule("referer", "y7bmrjlc", "ab");
        putRule("res", "whxqm2a7", "hf");
        putRule("rtype", "x8o2h2bl", "lo");
        putRule("sdkver", "9q3dcxp2", "sc");
        putRule("status", "2jbrxxw4", "an");
        putRule("subVersion", "eo3i2puh", "ns");
        putRule("svm", "fzj3kaeh", "qr");
        putRule("time", "q2t3odsk", "nb");
        putRule("timezone", "1uv05lj5", "as");
        putRule("tn", "x9nzj1bp", "py");
        putRule("trees", "acfs0xo4", "pi");
        putRule("ua", "k92crp1t", "bj");
        putRule("url", "y95hjkoo", "cf");
        putRule("version", null, "version");
        putRule("vpw", "r9924ab5", "ca");
    }

    private static void putRule(String field, String key, String obfuscatedName) {
        DES_RULES.put(field, new DesRule(key == null ? 0 : 1, obfuscatedName, key));
    }

    private ShumeiDeviceId() {
    }

    public static String getDId() {
        String uid = UUID.randomUUID().toString();
        String priId = md5Hex(uid).substring(0, 16);
        String ep = Base64.getEncoder().encodeToString(rsaEncrypt(uid.getBytes(StandardCharsets.UTF_8)));

        Map<String, Object> browser = new LinkedHashMap<>();
        browser.put("plugins",
                "MicrosoftEdgePDFPluginPortableDocumentFormatinternal-pdf-viewer1,MicrosoftEdgePDFViewermhjfbmdgcfjbbpaeojofohoefgiehjai1");
        browser.put("ua",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36 Edg/129.0.0.0");
        browser.put("canvas", "259ffe69");
        browser.put("timezone", -480);
        browser.put("platform", "Win32");
        browser.put("url", "https://www.skland.com/");
        browser.put("referer", "");
        browser.put("res", "1920_1080_24_1.25");
        browser.put("clientSize", "0_0_1080_1920_1920_1080_1920_1080");
        browser.put("status", "0011");
        long now = System.currentTimeMillis();
        browser.put("vpw", UUID.randomUUID().toString());
        browser.put("svm", now);
        browser.put("trees", UUID.randomUUID().toString());
        browser.put("pmf", now);

        Map<String, Object> target = new LinkedHashMap<>(browser);
        target.put("protocol", 102);
        target.put("organization", ORGANIZATION);
        target.put("appId", "default");
        target.put("os", "web");
        target.put("version", "3.0.0");
        target.put("sdkver", "3.0.0");
        target.put("box", "");
        target.put("rtype", "all");
        target.put("smid", getSmid());
        target.put("subVersion", "1.0.0");
        target.put("time", 0);
        target.put("tn", md5Hex(getTn(target)));

        Map<String, Object> obfuscated = desObfuscate(target);
        byte[] gz = gzipJson(obfuscated);
        String hexData = aesEncryptHex(gz, priId);

        ObjectNode payload = JSON.createObjectNode();
        payload.put("appId", "default");
        payload.put("compress", 2);
        payload.put("data", hexData);
        payload.put("encode", 5);
        payload.put("ep", ep);
        payload.put("organization", ORGANIZATION);
        payload.put("os", "web");
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(DEVICE_INFO_URL))
                    .timeout(Duration.ofSeconds(15))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            JSON.writeValueAsString(payload), StandardCharsets.UTF_8))
                    .build();
            HttpResponse<String> response = HTTP.send(request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            JsonNode node = JSON.readTree(response.body());
            if (node.path("code").asInt() != 1100) {
                throw new IllegalStateException("did计算失败：" + node);
            }
            return "B" + node.path("detail").path("deviceId").asText();
        } catch (Exception e) {
            throw new IllegalStateException("获取设备指纹失败", e);
        }
    }

    private static Map<String, Object> desObfuscate(Map<String, Object> input) {
        Map<String, Object> out = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : input.entrySet()) {
            DesRule rule = DES_RULES.get(entry.getKey());
            if (rule != null) {
                Object value = entry.getValue();
                if (rule.isEncrypt() == 1) {
                    out.put(rule.obfuscatedName(), encryptField(entry.getKey(), String.valueOf(value)));
                } else {
                    out.put(rule.obfuscatedName(), value);
                }
            } else {
                out.put(entry.getKey(), entry.getValue());
            }
        }
        return out;
    }

    /**
     * 对单个字段做 DES 混淆：值补 8 个 0x00 后加密，尾部不足整块的数据丢弃
     * （参考实现未调用 finalize，与 cryptography 库缓冲行为一致）。
     */
    static String encryptField(String field, String value) {
        DesRule rule = DES_RULES.get(field);
        byte[] data = Arrays.copyOf(value.getBytes(StandardCharsets.UTF_8),
                value.getBytes(StandardCharsets.UTF_8).length + 8);
        int aligned = data.length - data.length % 8;
        return Base64.getEncoder().encodeToString(desEncrypt(rule.key(), Arrays.copyOf(data, aligned)));
    }

    private static byte[] desEncrypt(String key, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "DES"));
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new IllegalStateException("DES加密失败", e);
        }
    }

    private static byte[] gzipJson(Map<String, Object> obfuscated) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try (GZIPOutputStream gz = new GZIPOutputStream(bos)) {
                gz.write(JSON.writeValueAsBytes(obfuscated));
            }
            return Base64.getEncoder().encode(bos.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("gzip压缩失败", e);
        }
    }

    private static String aesEncryptHex(byte[] data, String priId) {
        try {
            // 参考实现：先补一个 0x00，再补齐到 16 的倍数
            int padded = ((data.length + 1 + 15) / 16) * 16;
            byte[] paddedData = new byte[padded];
            System.arraycopy(data, 0, paddedData, 0, data.length);
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE,
                    new SecretKeySpec(priId.getBytes(StandardCharsets.US_ASCII), "AES"),
                    new IvParameterSpec("0102030405060708".getBytes(StandardCharsets.US_ASCII)));
            return toHex(cipher.doFinal(paddedData));
        } catch (Exception e) {
            throw new IllegalStateException("AES加密失败", e);
        }
    }

    private static byte[] rsaEncrypt(byte[] data) {
        try {
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(
                    new X509EncodedKeySpec(Base64.getDecoder().decode(PUBLIC_KEY_B64)));
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new IllegalStateException("RSA加密失败", e);
        }
    }

    /**
     * 所有 key 排序后，值（数字乘 10000、字典递归）拼接后 md5，对应 get_tn。
     */
    static String getTn(Map<String, Object> o) {
        StringBuilder sb = new StringBuilder();
        o.keySet().stream().sorted().forEach(key -> {
            Object v = o.get(key);
            if (v instanceof Number number) {
                sb.append(number.longValue() * 10000L);
            } else if (v instanceof Map<?, ?> map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> sub = (Map<String, Object>) map;
                sb.append(getTn(sub));
            } else {
                sb.append(v);
            }
        });
        return sb.toString();
    }

    private static String getSmid() {
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String uid = UUID.randomUUID().toString();
        String v = time + md5Hex(uid) + "00";
        String smskWeb = md5Hex("smsk_web_" + v).substring(0, 14);
        return v + smskWeb + "0";
    }

    private static String md5Hex(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return toHex(md.digest(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException("MD5不可用", e);
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(Character.forDigit((b >> 4) & 0xF, 16));
            sb.append(Character.forDigit(b & 0xF, 16));
        }
        return sb.toString();
    }
}
