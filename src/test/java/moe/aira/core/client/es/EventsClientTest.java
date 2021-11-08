package moe.aira.core.client.es;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class EventsClientTest {
    @Autowired
    EventsClient eventsClient;
    @Test
    void pointBonuses() {
    }

    @Test
    void index() {
        System.out.println(eventsClient.index());
    }
}