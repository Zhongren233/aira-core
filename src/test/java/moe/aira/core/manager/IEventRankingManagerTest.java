package moe.aira.core.manager;

import moe.aira.core.entity.dto.UserRanking;
import moe.aira.core.entity.es.PointRanking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}