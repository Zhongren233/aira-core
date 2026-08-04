package moe.aira.core.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import moe.aira.core.config.EnsembleStarsConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.Method;
import org.apache.hc.core5.http.message.BasicHeader;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

@Component
@Slf4j
public class CryptoUtils {
    @Value("${es.game.token}")
    private String token;
    @Value("${es.game.session}")
    private String session;

    @Value("${es.game.resMd5}")
    private String resMd5;
    @Value("${es.game.major}")
    private String major;


    private final Cipher deCryptoCipher;
    private final Cipher enCryptoCipher;

    public CryptoUtils(EnsembleStarsConfig ensembleStarsConfig) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        Security.addProvider(new BouncyCastleProvider());
        String cryptoKey = ensembleStarsConfig.getCryptoKey();
        log.info(cryptoKey);
        byte[] md5 = DigestUtils.md5(cryptoKey);
        byte[] sha1 = DigestUtils.sha1(cryptoKey);
        byte[] key = new byte[16];
        byte[] iv = new byte[16];
        System.arraycopy(md5, 0, key, 0, 16);
        System.arraycopy(sha1, 0, key, 8, 8);
        System.arraycopy(sha1, 0, iv, 0, 16);
        deCryptoCipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        enCryptoCipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        deCryptoCipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
        enCryptoCipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);

        log.info("Crypto key: {}", Base64.getEncoder().encodeToString(key));
        log.info("Crypto iv: {}", Base64.getEncoder().encodeToString(iv));

        log.info("加密组件已初始化完毕");

    }


    /**
     * 突然发现这玩意有并发问题
     * synchronized一下
     */
    public byte[] decrypt(byte[] bytes) throws BadPaddingException, IllegalBlockSizeException {
        synchronized (deCryptoCipher) {
            return deCryptoCipher.doFinal(bytes);
        }
    }

    public byte[] encrypt(byte[] bytes) throws BadPaddingException, IllegalBlockSizeException {
        synchronized (enCryptoCipher) {
            return enCryptoCipher.doFinal(bytes);
        }
    }

    public byte[] encrypt(String s) throws BadPaddingException, IllegalBlockSizeException {
        return encrypt(s.getBytes(StandardCharsets.UTF_8));
    }

    public SimpleHttpRequest generatorPointRankPageRequest(int page) throws RuntimeException {
        SimpleHttpRequest httpRequest = SimpleHttpRequest.create(Method.POST, URI.create("https://saki-server.happyelements.cn/get/events/point_ranking"));
        try {
            httpRequest.setHeaders(baseHeader());
            String s = baseBody() + "&" + MessageFormat.format("page={0}", page);
            httpRequest.setBody(encrypt(s), ContentType.APPLICATION_OCTET_STREAM);
            return httpRequest;
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

    public Header[] baseHeader() {
        return new Header[]{
                new BasicHeader("Authorization", "Token " + token),
                new BasicHeader("X-Game-Version", major),
                new BasicHeader("Content-Type", "application/octet-stream"),
        };
    }

    public static void main(String[] args) throws Exception {

        Security.addProvider(new BouncyCastleProvider());
        byte[] key;
        byte[] iv;
        String keyStr = "9D8DC98B85A699CB9BC688CA8E94B2B38BB4A8ADB19EBB9A9CC9A8BAACBEB69C8BAC9085BBB78BB3D4C8A5C2";
        String ivStr = "9DAA90BA8D858F97A9899492BEB49C92CBCF9BC8B2BAC2C2";

        key = Base64.getDecoder().decode(hexToBytes(keyStr, true));
        iv = Base64.getDecoder().decode(hexToBytes(ivStr, true));
        System.out.println(Base64.getEncoder().encodeToString(key));
        System.out.println(Base64.getEncoder().encodeToString(iv));

//        key = Base64.getDecoder().decode("br6tzYf4d9w5qkMLtKWRNaDec6WESAIctSozDHtL+7Z=");
//        iv = Base64.getDecoder().decode("bUoErzphVvkmAKcm40d7ME==");

            Cipher deCryptoCipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            Cipher enCryptoCipher = Cipher.getInstance("AES/CBC/PKCS7Padding");



        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        deCryptoCipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
        enCryptoCipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);

        byte[] bytes = urlsafeDecode("NQZ-huemce1TclLl1d2ReOCG3mjuKrKTbr4cVxB18DRggsr1xyiKisoGyAliYMgu");

        byte[] bytes1 = deCryptoCipher.doFinal(bytes);
        System.out.println(new String(bytes1));

    }

    static byte[] hexToBytes(String hex, boolean e) {
        byte[] bytes = hexToBytes(hex);
        if (e) {
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) (bytes[i] ^ 0xFF);

            }

        }
        return bytes;
    }


    static byte[] hexToBytes(String hex) {
        if (hex == null) {
            return null;
        }

        hex = hex.replaceAll("\\s+", ""); // 去空格（可选）
        if ((hex.length() & 1) != 0) {
            throw new IllegalArgumentException("Hex string length must be even");
        }

        int len = hex.length();
        byte[] result = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            result[i / 2] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
        }
        return result;
    }

    static String urlsafeEncode(byte[] data) {
        String base64 = Base64.getEncoder().encodeToString(data);
        return base64
                .replace('+', '-')
                .replace('/', '_');
    }

    /** 与上面 encode 完全对称 */
    static byte[] urlsafeDecode(String text) {
        String base64 = text
                .replace('-', '+')
                .replace('_', '/');
        return Base64.getDecoder().decode(base64);
    }


    private String baseBody() {
        return MessageFormat.format(
                "login_type=mobile&" +
                        "hei_token={0}&" +
                        "session={1}&" +
                        "channel_uid=522e3495d82423b3675b035c9a06c69c&" +
                        "platform=iOS&" +
                        "packageName=apple&" +
                        "resMd5={2}&" +
                        "major={3}&" +
                        "maintainceCnfVer=31&" +
                        "msg_id={4}",
                token,
                session,
                resMd5,
                major,
                UUID.randomUUID());

    }
}