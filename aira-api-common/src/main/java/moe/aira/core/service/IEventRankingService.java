package moe.aira.core.service;

import moe.aira.core.entity.dto.UserRanking;
import moe.aira.entity.es.PointRanking;
import moe.aira.entity.es.ScoreRanking;
import moe.aira.enums.AiraEventRankingStatus;

import java.util.concurrent.CountDownLatch;

public interface IEventRankingService {

    /**
     * 获取全部PointRanking
     *
     * @return CountDownLatch计数器 注：该计数器有可能永远不会为0，请务必设置超时策略。
     */
    CountDownLatch fetchAllPointRanking() throws InterruptedException;

    /**
     * 获取全部ScoreRanking
     *
     * @return CountDownLatch计数器 注：该计数器有可能永远不会为0，请务必设置超时策略。
     */
    CountDownLatch fetchAllScoreRanking();


    CountDownLatch fetchAllSSScoreRanking(String colorTypeId);

    UserRanking<PointRanking> fetchPointRankingByRank(Integer rank);

    UserRanking<ScoreRanking> fetchScoreRankingByRank(Integer rank);

    UserRanking<ScoreRanking> fetchScoreRankingByRank(Integer rank, String colorType);

    UserRanking<PointRanking> fetchPointRankingByUserId(Integer userId, AiraEventRankingStatus status);

    UserRanking<ScoreRanking> fetchScoreRankingByUserId(Integer userId, AiraEventRankingStatus status);

    UserRanking<ScoreRanking> fetchScoreRankingByUserId(Integer userId, AiraEventRankingStatus status, String songStatus);

    Integer countScoreRankingWhereGtPoint(Integer point);

    Integer countPointRankingWhereGtPoint(Integer point);

}
