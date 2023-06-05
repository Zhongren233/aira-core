package moe.aira.core.client.es;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CasinoClientTest {
    @Autowired
    CasinoClient casinoClient;
    @Test
    void casino() {
        System.out.println(casinoClient.casino());
    }
}