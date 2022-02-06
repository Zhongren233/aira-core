package moe.aira.core.client.es;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EventsClientTest {
    @Autowired
    EventsClient eventsClient;

    @Test
    void pointBonuses() {
        System.out.println(eventsClient.pointBonuses());
    }

    @Test
    void tours() {
        System.out.println(eventsClient.tours());
    }

    @Test
    void index() {
        System.out.println(eventsClient.index());
    }

    @Test
    void announce() {
        System.out.println(eventsClient.eventAnnounce());
    }

    @Test
    void cTest() {
        System.out.println("eventsClient.christmas2020Game() = " + eventsClient.christmas2020Game());
    }

    @Test
    void tree() {
        System.out.println("eventsClient.christmas2020GameTree() = " + eventsClient.christmas2020GameTree());
    }
}