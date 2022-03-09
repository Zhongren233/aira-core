package moe.aira.core.client.es;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WorkClientTest {
    @Autowired
    WorkClient workClient;

    @Test
    void works() {
        System.out.println(workClient.works());
    }
}