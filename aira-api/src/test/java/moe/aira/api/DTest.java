package moe.aira.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import moe.aira.core.util.CryptoUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;

@SpringBootTest
public class DTest {
    @Autowired
    CryptoUtils cryptoUtils;
    @Qualifier("messagePackMapper")
    @Autowired
    private ObjectMapper messagePackMapper;

    @Test
    void test() throws Exception {
        byte[] bytes = new FileInputStream("C:\\Users\\sc\\Desktop\\my_page").readAllBytes();
        byte[] bytes1 = cryptoUtils.decrypt(bytes);
        System.out.println(new String(bytes1));

    }
}
