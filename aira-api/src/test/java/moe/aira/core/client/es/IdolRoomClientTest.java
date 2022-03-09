package moe.aira.core.client.es;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IdolRoomClientTest {
    @Autowired
    IdolRoomClient idolRoomClient;

    @Test
    void cards() {
        System.out.println(idolRoomClient.cards("2"));
    }
}