package moe.aira.api.hekk;

import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.core.client.hekk.PresentClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PresentClientTest {
    @Autowired
    PresentClient presentClient;

    @Test
    void presents() {
        JsonNode presents = presentClient.presents("1");
        System.out.println(presents);

    }
}
