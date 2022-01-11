package moe.aira.core.service.impl;

import lombok.extern.slf4j.Slf4j;
import moe.aira.core.dao.PointRankingMapper;
import moe.aira.core.dao.ScoreRankingMapper;
import moe.aira.core.dao.UserProfileMapper;
import moe.aira.core.entity.dto.UserRanking;
import moe.aira.core.entity.es.PointRanking;
import moe.aira.core.manager.IEventRankingManager;
import moe.aira.core.service.IEventRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IEventRankingServiceImpl implements IEventRankingService {
    final
    IEventRankingManager eventRankingManager;
    @Autowired
    PointRankingMapper pointRankingMapper;
    @Autowired
    UserProfileMapper userProfileMapper;
    @Autowired
    ScoreRankingMapper scoreRankingMapper;
    @Qualifier("daoAsyncExecutor")
    @Autowired
    private Executor daoAsyncExecutor;

    public IEventRankingServiceImpl(IEventRankingManager eventRankingManager) {
        this.eventRankingManager = eventRankingManager;
    }


    @Override
    public CountDownLatch fetchAllPointRanking() {
        Integer page = eventRankingManager.fetchTotalPointRankingPage();
        CountDownLatch countDownLatch = new CountDownLatch(page);
        for (Integer i = 1; i <= page; i++) {
            eventRankingManager.fetchPointRankingsAsync(i)
                    .thenAcceptAsync(userRankings -> {
                                pointRankingMapper.upsertPointRanking(userRankings.stream().map(UserRanking::getRanking).collect(Collectors.toList()));
                                userProfileMapper.upsertUserProfile(userRankings.stream().map(UserRanking::getProfile).collect(Collectors.toList()));
                            }, daoAsyncExecutor
                    ).thenAccept(a -> countDownLatch.countDown());
        }
        return countDownLatch;
    }

    @Override
    public CountDownLatch fetchAllScoreRanking() {
        Integer page = eventRankingManager.fetchTotalScoreRankingPage();
        CountDownLatch countDownLatch = new CountDownLatch(page);
        for (Integer i = 1; i <= page; i++)
            eventRankingManager.fetchScoreRankingsAsync(i).thenAcceptAsync(userRankings -> scoreRankingMapper.upsertScoreRankings(userRankings.stream().map(UserRanking::getRanking).collect(Collectors.toList())), daoAsyncExecutor).thenAccept(a -> countDownLatch.countDown());
        return countDownLatch;
    }

    public Object mergeToPastRanking() {
        return null;
    }
}