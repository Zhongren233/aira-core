package moe.aira.core.service.impl;

import lombok.extern.slf4j.Slf4j;
import moe.aira.core.dao.PointRankingMapper;
import moe.aira.core.dao.ScoreRankingMapper;
import moe.aira.core.dao.UserProfileMapper;
import moe.aira.core.entity.dto.UserRanking;
import moe.aira.core.entity.es.PointRanking;
import moe.aira.core.entity.es.ScoreRanking;
import moe.aira.core.entity.es.UserProfile;
import moe.aira.core.manager.IEventRankingManager;
import moe.aira.core.service.IEventRankingService;
import moe.aira.enums.AiraEventRankingStatus;
import moe.aira.exception.AiraNoUserDataException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static moe.aira.util.RankPageCalculator.calcPage;

@Service
@Slf4j
public class IEventRankingServiceImpl implements IEventRankingService {
    final
    IEventRankingManager eventRankingManager;
    final
    PointRankingMapper pointRankingMapper;
    final
    UserProfileMapper userProfileMapper;
    final
    ScoreRankingMapper scoreRankingMapper;
    private final Executor daoAsyncExecutor;
    @Value("${aira.core.stalker.max-page}")
    private Integer configMaxPage = 200;

    public IEventRankingServiceImpl(IEventRankingManager eventRankingManager, PointRankingMapper pointRankingMapper, UserProfileMapper userProfileMapper, ScoreRankingMapper scoreRankingMapper, @Qualifier("daoAsyncExecutor") Executor daoAsyncExecutor) {
        this.eventRankingManager = eventRankingManager;
        this.pointRankingMapper = pointRankingMapper;
        this.userProfileMapper = userProfileMapper;
        this.scoreRankingMapper = scoreRankingMapper;
        this.daoAsyncExecutor = daoAsyncExecutor;
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

    @Override
    public UserRanking<PointRanking> fetchPointRankingByRank(Integer rank) {
        return eventRankingManager.fetchPointRankingByRank(rank);
    }

    @Override
    public UserRanking<ScoreRanking> fetchScoreRankingByRank(Integer rank) {
        return eventRankingManager.fetchScoreRankingByRank(rank);
    }

    @Override
    public UserRanking<PointRanking> fetchPointRankingByUserId(Integer userId, AiraEventRankingStatus status) {
        UserRanking<PointRanking> userRanking = new UserRanking<>();
        userRanking.setStatus(AiraEventRankingStatus.NO_DATA);
        PointRanking dbPointRanking = pointRankingMapper.selectById(userId);
        if (dbPointRanking == null) {
            return userRanking;
        }

        if (status == AiraEventRankingStatus.NOT_REALTIME_POINT_RANKING) {
            UserProfile userProfile = userProfileMapper.selectById(userId);
            userRanking.setStatus(AiraEventRankingStatus.NOT_REALTIME_POINT_RANKING);
            userRanking.setProfile(userProfile);
            userRanking.setRanking(dbPointRanking);
            return userRanking;
        }

        if (status == AiraEventRankingStatus.REALTIME_DATA ||
                status == AiraEventRankingStatus.NOT_REALTIME_SCORE_RANKING) {
            Optional<UserRanking<PointRanking>> optionalRealTimePointRanking = fetchRealTimePointRanking(userId, dbPointRanking);
            if (optionalRealTimePointRanking.isPresent()) {
                UserRanking<PointRanking> ranking = optionalRealTimePointRanking.get();
                ranking.setStatus(AiraEventRankingStatus.REALTIME_DATA);
                return ranking;
            }else {
                UserProfile userProfile = userProfileMapper.selectById(userId);
                userRanking.setStatus(AiraEventRankingStatus.NOT_REALTIME_POINT_RANKING);
                userRanking.setProfile(userProfile);
                userRanking.setRanking(dbPointRanking);
                return userRanking;
            }
        }

        throw new IllegalArgumentException();
    }

    private Optional<UserRanking<PointRanking>> fetchRealTimePointRanking(Integer userId, PointRanking dbPointRanking) {
        Optional<UserRanking<PointRanking>> first;
        int startPage = calcPage(dbPointRanking.getEventRank());
        int fetchPageCount = 0;
        int pageOffset = 0;
        int turnDirection = 1;
        do {
            List<UserRanking<PointRanking>> userRankings =
                    eventRankingManager.fetchPointRankings(startPage + pageOffset);
            first = userRankings.stream().filter(pointRankingUserRanking -> Objects.equals(pointRankingUserRanking.getUserId(), userId)).findFirst();
            pageOffset += turnDirection;
            if (userRankings.get(0).getRanking().getEventPoint() < dbPointRanking.getEventPoint()) {
                turnDirection = -1;
                pageOffset=0;
            }
            fetchPageCount++;
        } while (first.isEmpty() && fetchPageCount <= configMaxPage);
        return first;
    }

    @Override
    public UserRanking<ScoreRanking> fetchScoreRankingByUserId(Integer userId, AiraEventRankingStatus status) {
        return null;
    }

}