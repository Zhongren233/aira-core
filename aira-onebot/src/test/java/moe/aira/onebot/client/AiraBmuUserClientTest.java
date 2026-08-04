package moe.aira.onebot.client;

import moe.aira.onebot.client.dto.BmuAiraRanking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AiraBmuUserClientTest {
    @Autowired
    AiraBmuUserClient bmuUserClient;

    @Test
    void fetchRealTimeAiraEventRanking() {
        BmuAiraRanking x = bmuUserClient.fetchRealTimeAiraEventRanking(13);
        System.out.println(x);
    }
}