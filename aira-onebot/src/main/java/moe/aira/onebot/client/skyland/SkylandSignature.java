package moe.aira.onebot.client.skyland;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 森空岛接口签名，对应参考项目 skyland.py 的 generate_signature。
 * <p>
 * 签名串 = path + bodyOrQuery + timestamp + {"platform":"","timestamp":"{timestamp}","dId":"","vName":""}
 * 先 HMAC-SHA256（密钥为 cred token），结果转十六进制后做 MD5。
 */
public final class SkylandSignature {

    private SkylandSignature() {
    }

    public record SignResult(String sign, String timestamp) {
    }

    public static SignResult sign(String token, String path, String bodyOrQuery) {
        // 与服务端时钟容差，参考项目固定减 2 秒
        return signAt(token, path, bodyOrQuery, String.valueOf(System.currentTimeMillis() / 1000 - 2));
    }

    static SignResult signAt(String token, String path, String bodyOrQuery, String timestamp) {
        String headerCa = "{\"platform\":\"\",\"timestamp\":\"" + timestamp + "\",\"dId\":\"\",\"vName\":\"\"}";
        String raw = path + bodyOrQuery + timestamp + headerCa;
        String hmacHex = hmacSha256Hex(token, raw);
        return new SignResult(md5Hex(hmacHex), timestamp);
    }

    private static String hmacSha256Hex(String key, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return toHex(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException("HMAC-SHA256计算失败", e);
        }
    }

    private static String md5Hex(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return toHex(md.digest(data.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
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
