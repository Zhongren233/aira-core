package moe.aira.api.hekk;

import com.fasterxml.jackson.core.JsonProcessingException;
import moe.aira.core.client.hekk.BaseClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
public class BaseClientTest {
    @Autowired
    BaseClient baseClient;


    @Test
    void test() throws JsonProcessingException {
        System.out.println(baseClient.title());

    }


}
