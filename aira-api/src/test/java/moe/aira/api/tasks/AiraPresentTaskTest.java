package moe.aira.api.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.core.client.es.PresentsClient;
import moe.aira.core.client.hekk.BaseClient;
import moe.aira.core.client.hekk.PresentClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AiraPresentTaskTest {
    @Autowired
    PresentClient  presentsClient;
    @Autowired
    BaseClient baseClient;
    @Test
    void jpTask() {
        JsonNode presents = presentsClient.presents("1");
        System.out.println(presents);
        if (presents.get("user_presents").size() != 0) {
            JsonNode node = presentsClient.receiveAll();
            System.out.println(node);
        }
    }
}