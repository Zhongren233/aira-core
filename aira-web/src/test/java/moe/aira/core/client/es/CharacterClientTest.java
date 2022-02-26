package moe.aira.core.client.es;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CharacterClientTest {
    @Autowired
    CharacterClient client;
    @Test
    void character() {
        System.out.println(client.character("22"));
    }
}