package moe.aira.core.manager;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import moe.aira.core.entity.dto.UserRanking;
import moe.aira.core.entity.es.PointRanking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IEventRankingManagerTest {
    @Autowired
    IEventRankingManager eventRankingManager;

    @Test
    void fetchPointRankings() {
        for (int i = 1; i <= 20; i++) {
            eventRankingManager.fetchPointRankings(i);
        }
    }

    @Test
    void fetchScoreRankings() {
        for (int i = 1; i <= 20; i++) {
            eventRankingManager.fetchScoreRankings(i);
        }
    }

    @Test
    void fetchPointRankingByRank_01() {
        UserRanking<PointRanking> pointRankingUserRanking = eventRankingManager.fetchPointRankingByRank(18);
        assert pointRankingUserRanking.getRanking().getEventRank() == 18;
    }

    @Test
    void fetchPointRankingByRank_02() {
        UserRanking<PointRanking> pointRankingUserRanking = eventRankingManager.fetchPointRankingByRank(199);
        assert pointRankingUserRanking.getRanking().getEventRank() == 199;
    }

    @Test
    void fetchScoreRankingByRank_01() {
        UserRanking<PointRanking> pointRankingUserRanking = eventRankingManager.fetchPointRankingByRank(18);
        assert pointRankingUserRanking.getRanking().getEventRank() == 18;
    }

    @Test
    void fetchScoreRankingByRank_02() {
        UserRanking<PointRanking> pointRankingUserRanking = eventRankingManager.fetchPointRankingByRank(199);
        assert pointRankingUserRanking.getRanking().getEventRank() == 199;
    }

    @Test
    @SneakyThrows
    void fetchPointRankingAsync_01() {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 1; i <= 10; i++) {
            CompletableFuture<List<UserRanking<PointRanking>>> listCompletableFuture = eventRankingManager.fetchPointRankingsAsync(10);
            listCompletableFuture.thenAcceptAsync(
                    userRankings -> countDownLatch.countDown()
            );
        }
        countDownLatch.await();
    }
    @Test
    void fetchTotalPointRankingPage() {
        Integer x = eventRankingManager.fetchTotalPointRankingPage();
        log.info("fetchTotalPointRankingPage:{}",x);
    }
    @Test
    void fetchTotalScoreRankingPage() {
        Integer x = eventRankingManager.fetchTotalScoreRankingPage();
        log.info("fetchTotalScoreRankingPage:{}",x);
    }
}