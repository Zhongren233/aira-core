package moe.aira.core.client.es;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PointRankingClientTest {
    @Autowired
    PointRankingClient pointRankingClient;
    @Test
    void page() {
        System.out.println(pointRankingClient.page(1));
    }
}