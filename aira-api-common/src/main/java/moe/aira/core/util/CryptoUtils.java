package moe.aira.core.util;

import lombok.extern.slf4j.Slf4j;
import moe.aira.core.config.EnsembleStarsConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.Method;
import org.apache.hc.core5.http.message.BasicHeader;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.text.MessageFormat;
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
        EnsembleStarsConfig ensembleStarsConfig = new EnsembleStarsConfig();
        ensembleStarsConfig.setCryptoKey("saki#*e49x%tt-7m%P/+g");
        CryptoUtils cryptoUtils = new CryptoUtils(ensembleStarsConfig);
        byte[] decrypt = cryptoUtils.decrypt(new FileInputStream("C:\\Users\\sc\\Desktop\\perform").readAllBytes());

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