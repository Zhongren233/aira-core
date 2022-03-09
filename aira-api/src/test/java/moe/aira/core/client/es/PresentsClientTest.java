package moe.aira.core.client.es;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PresentsClientTest {
    @Autowired
    PresentsClient presentsClient;

    @Test
    void getPresent() {
        System.out.println(presentsClient.presents("1"));
    }

    @Test
    void presents() {
        System.out.println(presentsClient.receiveAll());
    }
}