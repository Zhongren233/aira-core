package moe.aira.aspect;

import moe.aira.config.EventConfig;
import moe.aira.core.dao.PointRankingMapper;
import moe.aira.core.dao.ScoreRankingMapper;
import moe.aira.core.dao.UserProfileMapper;
import moe.aira.core.entity.dto.UserRanking;
import moe.aira.core.entity.es.PointRanking;
import moe.aira.core.entity.es.ScoreRanking;
import moe.aira.core.manager.IEventConfigManager;
import moe.aira.enums.EventStatus;
import moe.aira.exception.AiraException;
import moe.aira.exception.EnsembleStarsException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Aspect
@Component
public class EventRankingAspect {

    final
    Executor daoAsyncExecutor;

    final
    PointRankingMapper pointRankingMapper;

    final
    UserProfileMapper userProfileMapper;

    final
    ScoreRankingMapper scoreRankingMapper;

    final
    IEventConfigManager eventConfigManager;

    public EventRankingAspect(@Qualifier("daoAsyncExecutor") Executor daoAsyncExecutor, PointRankingMapper pointRankingMapper, UserProfileMapper userProfileMapper, ScoreRankingMapper scoreRankingMapper, EventConfig eventConfig, IEventConfigManager eventConfigManager) {
        this.daoAsyncExecutor = daoAsyncExecutor;
        this.pointRankingMapper = pointRankingMapper;
        this.userProfileMapper = userProfileMapper;
        this.scoreRankingMapper = scoreRankingMapper;
        this.eventConfig = eventConfig;
        this.eventConfigManager = eventConfigManager;
    }


    @SuppressWarnings("unchecked")
    @Around("bean(IEventRankingManagerImpl) && execution(* fetchPointRankings(..) ) ")
    public Object recordPointRankings(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        List<UserRanking<PointRanking>> proceed = (List<UserRanking<PointRanking>>) proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        daoAsyncExecutor.execute(() -> {
            pointRankingMapper.upsertPointRanking(proceed.stream().map(UserRanking::getRanking).collect(Collectors.toList()));
            userProfileMapper.upsertUserProfile(proceed.stream().map(UserRanking::getProfile).collect(Collectors.toList()));
        });
        return proceed;
    }

    @SuppressWarnings("unchecked")
    @Around("bean(IEventRankingManagerImpl) && execution(* fetchScoreRankings(..) ) ")
    public Object recordScoreRankings(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        List<UserRanking<ScoreRanking>> proceed = (List<UserRanking<ScoreRanking>>) proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        daoAsyncExecutor.execute(() -> scoreRankingMapper.upsertScoreRankings(proceed.stream().map(UserRanking::getRanking).collect(Collectors.toList())));
        return proceed;
    }

    @Around("@annotation(moe.aira.annotation.EventAvailable)")
    public Object checkEventStatusMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if (eventConfigManager.fetchEventConfig().checkAvailable()) {
            return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        } else {
            throw new AiraException("当前不是活动状态");
        }
    }

    final
    EventConfig eventConfig;

    @Around("bean(pointRankingClient))")
    public Object pointRankingClient(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        } catch (EnsembleStarsException e) {
            eventConfig.setEventStatus(EventStatus.End);
            throw e;
        }
    }


}
