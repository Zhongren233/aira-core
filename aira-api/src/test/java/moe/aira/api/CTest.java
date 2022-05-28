package moe.aira.api;

import moe.aira.core.manager.IEventRankingManager;
import moe.aira.core.service.IEventRankingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CTest {
    @Autowired
    IEventRankingManager eventRankingManager;

    @Autowired
    IEventRankingService eventRankingService;

    @Test
    void test() {
        eventRankingManager.fetchPointRankingsAsync(1).thenAccept(System.out::println).join();


    }
}
