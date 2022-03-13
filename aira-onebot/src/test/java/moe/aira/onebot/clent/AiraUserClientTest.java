package moe.aira.onebot.clent;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AiraUserClientTest {
    @Autowired
    AiraUserClient airaUserClient;

    @Test
    void fetchUserInfo() {
        System.out.println(airaUserClient.fetchUserInfo("雪泉"));
    }
}