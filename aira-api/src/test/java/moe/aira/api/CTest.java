package moe.aira.api;

import moe.aira.core.manager.IEventRankingManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CTest {
    @Autowired
    IEventRankingManager eventRankingManager;

    @Test
    void test() throws InterruptedException {
        System.out.println(eventRankingManager.fetchPointRankings(1));
        Thread.sleep(300000);
    }
}
