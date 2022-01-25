package moe.aira.core.biz.impl;

import moe.aira.annotation.EventAvailable;
import moe.aira.core.biz.IAiraEventRankingBiz;
import moe.aira.core.entity.aira.AiraBindRelation;
import moe.aira.core.entity.aira.AiraEventRanking;
import moe.aira.core.entity.dto.UserRanking;
import moe.aira.core.entity.es.PointRanking;
import moe.aira.core.entity.es.ScoreRanking;
import moe.aira.core.service.IEventRankingService;
import moe.aira.enums.AiraEventRankingStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IAiraEventRankingBizImpl implements IAiraEventRankingBiz {
    @Value("${aira.core.stat-level}")
    private AiraEventRankingStatus rankingLevel;


    final
    IEventRankingService eventRankingService;

    public IAiraEventRankingBizImpl(IEventRankingService eventRankingService) {
        this.eventRankingService = eventRankingService;
    }

    @Override
    @EventAvailable
    public AiraEventRanking fetchAiraEventRanking(Integer userId) {
        AiraEventRanking airaEventRanking = new AiraEventRanking();
        airaEventRanking.setStatus(rankingLevel);
        UserRanking<PointRanking> pointRanking = eventRankingService.fetchPointRankingByUserId(userId, rankingLevel);
        AiraEventRankingStatus status = pointRanking.getStatus();
        if (status != rankingLevel) {
            airaEventRanking.setStatus(status);
        }
        if (status != AiraEventRankingStatus.NO_DATA) {
            UserRanking<ScoreRanking> scoreRanking = eventRankingService.fetchScoreRankingByUserId(userId, rankingLevel);
            airaEventRanking.setScoreRanking(scoreRanking.getRanking());
            airaEventRanking.setUserId(userId);
            airaEventRanking.setEventId(pointRanking.getRanking().getEventId());
            airaEventRanking.setUserProfile(pointRanking.getProfile());
            airaEventRanking.setPointRanking(pointRanking.getRanking());
        }
        return airaEventRanking;
    }

    @Override
    public AiraBindRelation selectAiraEventBind(Long qqNumber) {


        return null;
    }
}
