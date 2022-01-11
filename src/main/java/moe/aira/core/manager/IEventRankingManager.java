package moe.aira.core.manager;

import moe.aira.core.entity.dto.UserRanking;
import moe.aira.core.entity.es.PointRanking;
import moe.aira.core.entity.es.ScoreRanking;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IEventRankingManager {
    /**
     * @return 总PointRanking页数
     */
    Integer fetchTotalPointRankingPage();
    /**
     * @return 总ScoreRanking页数
     */
    Integer fetchTotalScoreRankingPage();

    List<UserRanking<PointRanking>> fetchPointRankings(Integer page);

    List<UserRanking<ScoreRanking>> fetchScoreRankings(Integer page);

    CompletableFuture<List<UserRanking<PointRanking>>> fetchPointRankingsAsync(Integer page);

    CompletableFuture<List<UserRanking<ScoreRanking>>> fetchScoreRankingsAsync(Integer page);

    UserRanking<PointRanking> fetchPointRankingByRank(Integer rank);

    UserRanking<ScoreRanking> fetchScoreRankingByRank(Integer rank);
}
