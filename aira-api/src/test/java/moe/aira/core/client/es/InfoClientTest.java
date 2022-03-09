package moe.aira.core.client.es;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InfoClientTest {
    @Autowired
    InfoClient infoClient;

    @Test
    void getInfoList() {
        System.out.println(infoClient.getInfoList());
    }

    @Test
    void baseInfo() {
        System.out.println(infoClient.baseInfo());
    }
}