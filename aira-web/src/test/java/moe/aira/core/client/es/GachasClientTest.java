package moe.aira.core.client.es;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GachasClientTest {
    @Autowired
    GachasClient gachasClient;

    @Test
    void gachas() {
        System.out.println("gachasClient.gachas() = " + gachasClient.gachas());
    }

    @Test
    void results() throws Exception {
            System.out.println(gachasClient.results("100014", "3", "1"));
            Thread.sleep(3000);
    }

    @Test
    void cards() {
        System.out.println(gachasClient.cards("5000014"));
    }
}