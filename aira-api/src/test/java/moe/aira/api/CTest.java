package moe.aira.api;

import moe.aira.core.entity.dto.UserRanking;
import moe.aira.core.manager.IEventRankingManager;
import moe.aira.entity.es.ScoreRanking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CTest {
    @Autowired
    IEventRankingManager eventRankingManager;

    @Test
    void test() throws InterruptedException {
        List<UserRanking<ScoreRanking>> red = eventRankingManager.fetchSSScoreRankings(1, "WHITE");
        System.out.println(red);
        Thread.sleep(300000);
    }
}
