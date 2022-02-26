package moe.aira.core.client.es;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OfficeClientTest {
    @Autowired
    OfficeClient officeClient;
    @Test
    void commu() {
        System.out.println(officeClient.commu());
    }
}