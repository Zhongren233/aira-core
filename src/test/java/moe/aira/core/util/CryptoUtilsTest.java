package moe.aira.core.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CryptoUtilsTest {
    @Autowired
    CryptoUtils cryptoUtils;
    @Autowired
    private ObjectMapper messagePackMapper;

    @Test
    void decrypt() throws Exception {
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\sc\\Documents\\costumes");
        byte[] b = new byte[80000];
        int read = fileInputStream.read(b);
        byte[] bytes = Arrays.copyOf(b, read);
        byte[] decrypt = cryptoUtils.decrypt(bytes);
        System.out.println(decrypt.length);
        System.out.println(new String(decrypt));
      /*  FileOutputStream fileOutputStream = new FileOutputStream("./decrypt");
        fileOutputStream.write(decrypt);
        fileOutputStream.flush();
        fileOutputStream.close();*/
        JsonNode jsonNode = messagePackMapper.readTree(decrypt);
        System.out.println(jsonNode);
    }
}