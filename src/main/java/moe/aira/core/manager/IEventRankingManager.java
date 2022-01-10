package moe.aira.core.manager;

import moe.aira.core.entity.dto.UserRanking;
import moe.aira.core.entity.es.PointRanking;
import moe.aira.core.entity.es.ScoreRanking;

import java.util.List;

public interface IEventRankingManager {
    List<UserRanking<PointRanking>> fetchPointRankings(Integer page);

    List<UserRanking<ScoreRanking>> fetchScoreRankings(Integer page);

    UserRanking<PointRanking>  fetchPointRankingByRank(Integer rank);

    UserRanking<ScoreRanking>  fetchScoreRankingByRank(Integer rank);
}
