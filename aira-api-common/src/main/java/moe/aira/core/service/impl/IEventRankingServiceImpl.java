package moe.aira.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import moe.aira.core.dao.PointRankingMapper;
import moe.aira.core.dao.ScoreRankingMapper;
import moe.aira.core.dao.UserProfileMapper;
import moe.aira.core.entity.dto.UserRanking;
import moe.aira.core.manager.IEventRankingManager;
import moe.aira.core.service.IEventRankingService;
import moe.aira.entity.es.PointRanking;
import moe.aira.entity.es.ScoreRanking;
import moe.aira.entity.es.UserProfile;
import moe.aira.enums.AiraEventRankingStatus;
import moe.aira.util.RankPageCalculator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

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
                                if (userRankings != null) {
                                    pointRankingMapper.upsertPointRanking(userRankings.stream().map(UserRanking::getRanking).collect(Collectors.toList()));
                                    userProfileMapper.upsertUserProfile(userRankings.stream().map(UserRanking::getProfile).collect(Collectors.toList()));
                                }
                            }, daoAsyncExecutor
                    ).handleAsync((unused, throwable) -> {
                        if (throwable != null) {
                            log.error("fetchAllPointRanking error", throwable);
                        }
                        countDownLatch.countDown();
                        return null;
                    });
        }
        return countDownLatch;
    }

    @Override
    public CountDownLatch fetchAllScoreRanking() {
        Integer page = eventRankingManager.fetchTotalScoreRankingPage();
        CountDownLatch countDownLatch = new CountDownLatch(page);
        for (Integer i = 1; i <= page; i++) {
            eventRankingManager.fetchScoreRankingsAsync(i)
                    .thenAcceptAsync(userRankings -> {
                                if (userRankings != null) {
                                    scoreRankingMapper.upsertScoreRankings(userRankings.stream().map(UserRanking::getRanking).collect(Collectors.toList()));
                                }
                            }, daoAsyncExecutor
                    ).handleAsync((unused, throwable) -> {
                        if (throwable != null) {
                            log.error("fetchScoreRanking error", throwable);
                        }
                        countDownLatch.countDown();
                        return null;
                    });
        }
        return countDownLatch;
    }


    @Override
    public UserRanking<PointRanking> fetchPointRankingByRank(Integer rank) {
        int page = RankPageCalculator.calcPage(rank);
        int index = RankPageCalculator.calcIndex(rank);
        return eventRankingManager.fetchPointRankings(page).get(index);
    }

    @Override
    public UserRanking<ScoreRanking> fetchScoreRankingByRank(Integer rank) {
        int page = RankPageCalculator.calcPage(rank);
        int index = RankPageCalculator.calcIndex(rank);
        return eventRankingManager.fetchScoreRankings(page).get(index);
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
                ranking.setStatus(status);
                return ranking;
            } else {
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
        int startPage = RankPageCalculator.calcPage(dbPointRanking.getEventRank());
        int fetchPageCount = 0;
        int pageOffset = 0;
        int turnDirection = 1;
        do {
            List<UserRanking<PointRanking>> userRankings =
                    eventRankingManager.fetchPointRankings(startPage + pageOffset);
            //noinspection DuplicatedCode
            first = userRankings.stream().filter(pointRankingUserRanking -> Objects.equals(pointRankingUserRanking.getUserId(), userId)).findFirst();
            pageOffset += turnDirection;
            if (userRankings.get(0).getRanking().getEventPoint() < dbPointRanking.getEventPoint()) {
                turnDirection = -1;
                pageOffset = 0;
            }
            fetchPageCount++;
            log.info("爬取{}页", fetchPageCount);
        } while (first.isEmpty() && fetchPageCount <= configMaxPage);
        return first;
    }

    @Override
    public UserRanking<ScoreRanking> fetchScoreRankingByUserId(Integer userId, AiraEventRankingStatus status) {
        UserRanking<ScoreRanking> userRanking = new UserRanking<>();
        userRanking.setStatus(AiraEventRankingStatus.NO_DATA);
        ScoreRanking dbScoreRanking = scoreRankingMapper.selectById(userId);
        if (dbScoreRanking == null) {
            return userRanking;
        }

        if (status == AiraEventRankingStatus.NOT_REALTIME_POINT_RANKING || status == AiraEventRankingStatus.NOT_REALTIME_SCORE_RANKING) {
            UserProfile userProfile = userProfileMapper.selectById(userId);
            userRanking.setStatus(AiraEventRankingStatus.NOT_REALTIME_SCORE_RANKING);
            userRanking.setProfile(userProfile);
            userRanking.setRanking(dbScoreRanking);
            return userRanking;
        }

        if (status == AiraEventRankingStatus.REALTIME_DATA) {
            Optional<UserRanking<ScoreRanking>> optionalRealTimeScoreRanking = fetchRealTimeScoreRanking(userId, dbScoreRanking);
            if (optionalRealTimeScoreRanking.isPresent()) {
                UserRanking<ScoreRanking> ranking = optionalRealTimeScoreRanking.get();
                ranking.setStatus(AiraEventRankingStatus.REALTIME_DATA);
                return ranking;
            } else {
                UserProfile userProfile = userProfileMapper.selectById(userId);
                userRanking.setStatus(AiraEventRankingStatus.NOT_REALTIME_SCORE_RANKING);
                userRanking.setProfile(userProfile);
                userRanking.setRanking(dbScoreRanking);
                return userRanking;
            }
        }

        throw new IllegalArgumentException();
    }

    private Optional<UserRanking<ScoreRanking>> fetchRealTimeScoreRanking(Integer userId, ScoreRanking dbScoreRanking) {
        Optional<UserRanking<ScoreRanking>> first;
        int startPage = RankPageCalculator.calcPage(dbScoreRanking.getEventRank());
        int fetchPageCount = 0;
        int pageOffset = 0;
        int turnDirection = 1;
        do {
            List<UserRanking<ScoreRanking>> userRankings =
                    eventRankingManager.fetchScoreRankings(startPage + pageOffset);
            //noinspection DuplicatedCode
            first = userRankings.stream().filter(pointRankingUserRanking -> Objects.equals(pointRankingUserRanking.getUserId(), userId)).findFirst();
            pageOffset += turnDirection;
            if (userRankings.get(0).getRanking().getEventPoint() < dbScoreRanking.getEventPoint()) {
                turnDirection = -1;
                pageOffset = 0;
            }
            fetchPageCount++;
            log.info("爬取{}页", fetchPageCount);
        } while (first.isEmpty() && fetchPageCount <= configMaxPage);
        return first;
    }


    @Override
    public Integer countPointRankingWhereGtPoint(Integer point) {
        QueryWrapper<PointRanking> wrapper = new QueryWrapper<>();
        wrapper.ge("event_point", point);
        Long dbCount = pointRankingMapper.selectCount(wrapper);
        int page = RankPageCalculator.calcPage(Math.toIntExact(dbCount));
        int result;
        do {
            List<UserRanking<PointRanking>> userRankings = eventRankingManager.fetchPointRankings(page);
            UserRanking<PointRanking> userRanking = userRankings.get(userRankings.size() - 1);
            Integer lastPoint = userRanking.getRanking().getEventPoint();
            if (lastPoint >= point) {
                page++;
            } else {
                result = (page - 1) * 20 + (int)
                        userRankings.stream().map(UserRanking::getRanking)
                                .map(PointRanking::getEventPoint).filter(integer -> integer >= point).count();
                break;
            }
        } while (true);
        if (page == 1) {
            return result;
        } else {
            return result - 1;
        }

    }

    @Override
    public Integer countScoreRankingWhereGtPoint(Integer point) {
        QueryWrapper<ScoreRanking> wrapper = new QueryWrapper<>();
        wrapper.ge("event_point", point);
        Long dbCount = scoreRankingMapper.selectCount(wrapper);
        int page = RankPageCalculator.calcPage(Math.toIntExact(dbCount));
        do {
            List<UserRanking<ScoreRanking>> userRankings = eventRankingManager.fetchScoreRankings(page);
            UserRanking<ScoreRanking> userRanking = userRankings.get(userRankings.size() - 1);
            Integer lastPoint = userRanking.getRanking().getEventPoint();
            if (lastPoint >= point) {
                page++;
            } else {
                return Math.toIntExact((((page - 1) * 20L) + 1) +
                        userRankings.stream().filter(eventPoint -> eventPoint.getRanking().getEventPoint() >= point).count());
            }
        } while (true);
    }
}