package moe.aira.core.client.es;

import com.dtflys.forest.springboot.annotation.ForestScan;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
@SpringBootTest
@ForestScan("moe.aira")
@ComponentScan("moe.aira")
class PointRankingClientTest {
    @Autowired
    ScoreRankingClient scoreRankingClient;

    @Test
    void page() {
        JsonNode page = scoreRankingClient.ssPage(1, 1);
        System.out.println(page);

    }
}