package moe.aira.core.util;

import lombok.extern.slf4j.Slf4j;
import moe.aira.core.config.EnsembleStarsConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

@Component
@Slf4j
public class CryptoUtils {
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
}