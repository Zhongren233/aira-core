package moe.aira.aspect;

import moe.aira.config.EventConfig;
import moe.aira.core.dao.PointRankingMapper;
import moe.aira.core.dao.ScoreRankingMapper;
import moe.aira.core.dao.UserProfileMapper;
import moe.aira.core.entity.dto.UserRanking;
import moe.aira.core.manager.IEventConfigManager;
import moe.aira.entity.es.PointRanking;
import moe.aira.entity.es.ScoreRanking;
import moe.aira.enums.EventStatus;
import moe.aira.exception.EnsembleStarsException;
import moe.aira.exception.server.AiraNotOpenEventException;
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

    public EventRankingAspect(@Qualifier("daoAsyncExecutor") Executor daoAsyncExecutor, PointRankingMapper pointRankingMapper, UserProfileMapper userProfileMapper, ScoreRankingMapper scoreRankingMapper, IEventConfigManager eventConfigManager) {
        this.daoAsyncExecutor = daoAsyncExecutor;
        this.pointRankingMapper = pointRankingMapper;
        this.userProfileMapper = userProfileMapper;
        this.scoreRankingMapper = scoreRankingMapper;
        this.eventConfigManager = eventConfigManager;
    }


    @SuppressWarnings("unchecked")
    @Around("bean(IEventRankingManagerImpl) && execution(* fetchPointRankings(..) ) ")
    public List<UserRanking<PointRanking>> recordPointRankings(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        List<UserRanking<PointRanking>> proceed = (List<UserRanking<PointRanking>>) proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        daoAsyncExecutor.execute(() -> {
            pointRankingMapper.upsertPointRanking(proceed.stream().map(UserRanking::getRanking).collect(Collectors.toList()));
            userProfileMapper.upsertUserProfile(proceed.stream().map(UserRanking::getProfile).collect(Collectors.toList()));
        });
        return proceed;
    }

    @SuppressWarnings("unchecked")
    @Around("bean(IEventRankingManagerImpl) && execution(* fetchScoreRankings(..) ) ")
    public List<UserRanking<ScoreRanking>> recordScoreRankings(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        List<UserRanking<ScoreRanking>> proceed = (List<UserRanking<ScoreRanking>>) proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        daoAsyncExecutor.execute(() -> scoreRankingMapper.upsertScoreRankings(proceed.stream().map(UserRanking::getRanking).collect(Collectors.toList())));
        return proceed;
    }

    @Around("bean(IEventRankingManagerImpl) && execution(* fetchSSScoreRankings(..) ) ")
    public List<UserRanking<ScoreRanking>> recordSSScoreRankings(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        List<UserRanking<ScoreRanking>> proceed = (List<UserRanking<ScoreRanking>>) proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        daoAsyncExecutor.execute(() -> scoreRankingMapper.upsertSSScoreRankings(proceed.stream().map(UserRanking::getRanking).collect(Collectors.toList())));
        return proceed;
    }

    @Around("@annotation(moe.aira.annotation.EventAvailable)")
    public Object checkEventStatusMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        EventConfig eventConfig = eventConfigManager.fetchEventConfig();
        if (eventConfig.checkAvailable()) {
            return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        } else {
            throw new AiraNotOpenEventException();
        }
    }


    @Around("bean(IEventRankingManagerImpl) ")
    public Object pointRankingClient(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause != null) {
                if (EnsembleStarsException.class.equals(cause.getClass())) {
                    EventConfig currentConfig = eventConfigManager.fetchEventConfig();
                    currentConfig.setEventStatus(EventStatus.END);
                    eventConfigManager.updateEventConfig(currentConfig);
                }
            }

            throw e;
        }
    }


}
