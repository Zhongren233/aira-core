package moe.aira.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Base64;

@Component
@Slf4j
public class CryptoUtils {


    private final Cipher deCryptoCipher;
    private final Cipher enCryptoCipher;

    public CryptoUtils(Environment environment) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        Security.addProvider(new BouncyCastleProvider());
        String cryptoKey = environment.getProperty("crypto.key");
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

    public static void main(String[] args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("D:\\RiderProject\\Solution1\\MessagePackLz4Unziper\\bin\\Debug\\net6.0\\resources_production");
        JsonNode jsonNode = objectMapper.readTree(file);
        ArrayNode jsonNodes = (ArrayNode) jsonNode.get(6);
        for (JsonNode node : jsonNodes) {
            for (JsonNode jsonNode1 : node) {
                if (jsonNode1.isArray()) {
                    for (JsonNode jsonNode2 : jsonNode1) {
                        if (jsonNode2.isTextual()) {
                            String base = jsonNode2.textValue();
                            byte[] decode = Base64.getDecoder().decode(base);
                            byte length = decode[3];
                            byte[] nameBytes = new byte[length];
                            byte[] md5Bytes = new byte[16];
                            System.arraycopy(decode, 4, nameBytes, 0, length);
                            System.arraycopy(decode, 4 + length, md5Bytes, 0, 16);
                            String name = new String(nameBytes, StandardCharsets.UTF_8);
                            if (name.contains("resources/card/") ) {
                                String substring = name.substring(name.lastIndexOf("/") + 1);
                                int endIndex = substring.lastIndexOf("_");
                                if (endIndex==-1) {
                                    continue;
                                }
                                substring = substring.substring(0, endIndex);
                                try {
                                    if (Integer.parseInt(substring)>3231) {
                                        System.out.println("https://assets.boysm.hekk.org/asset_bundles/iOS/" + name + ".bundle." + DatatypeConverter.printHexBinary(md5Bytes).toLowerCase());
                                    }
                                } catch (NumberFormatException e) {
                                    continue;
                                }
                            }

                            if (name.contains("resources/event/logo/")) {
                                String substring = name.substring(name.lastIndexOf("/") + 1);
                                if (Integer.parseInt(substring) > 255) {
                                    System.out.println("https://assets.boysm.hekk.org/asset_bundles/iOS/" + name + ".bundle." + DatatypeConverter.printHexBinary(md5Bytes).toLowerCase());
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}