package moe.aira.core.service.impl;

import moe.aira.core.service.IEventRankingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IEventRankingServiceImplTest {
    @Autowired
    IEventRankingService eventRankingService;
    @Test
    void fetchAllEventRanking() throws InterruptedException {
        eventRankingService.fetchAllEventRanking();
        Thread.sleep(180*1000);
    }

    @Test
    void fetchCHrimas() {
        System.out.println("eventRankingService.fetchChristmas2020Tree() = " + eventRankingService.fetchChristmas2020Tree());
    }
}