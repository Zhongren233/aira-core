package moe.aira.core.service;

import java.util.concurrent.CountDownLatch;

public interface IEventRankingService {

    /**
     * 获取全部PointRanking
     *
     * @return CountDownLatch计数器 注：该计数器有可能永远不会为0，请务必设置超时策略。
     */
    CountDownLatch fetchAllPointRanking();

    /**
     * 获取全部ScoreRanking
     *
     * @return CountDownLatch计数器 注：该计数器有可能永远不会为0，请务必设置超时策略。
     */
    CountDownLatch fetchAllScoreRanking();

}
