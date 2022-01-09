package moe.aira.core.client.es;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LiveChallengesClientTest {
    @Autowired
    LiveChallengesClient liveChallengesClient;
    @Test
    void ranking() {
        JsonNode ranking = liveChallengesClient.ranking(3);
        System.out.println(ranking);
    }
}