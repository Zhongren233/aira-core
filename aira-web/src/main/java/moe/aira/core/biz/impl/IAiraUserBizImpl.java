package moe.aira.core.biz.impl;

import moe.aira.annotation.EventAvailable;
import moe.aira.core.biz.IAiraUserBiz;
import moe.aira.core.entity.dto.UserRanking;
import moe.aira.core.service.IAiraBindRelationService;
import moe.aira.core.service.IEventRankingService;
import moe.aira.entity.aira.AiraEventRanking;
import moe.aira.entity.es.PointRanking;
import moe.aira.entity.es.ScoreRanking;
import moe.aira.enums.AiraEventRankingStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IAiraUserBizImpl implements IAiraUserBiz {
    @Value("${aira.core.stat-level}")
    private AiraEventRankingStatus rankingLevel;


    final
    IEventRankingService eventRankingService;

    final
    IAiraBindRelationService airaBindRelationService;

    public IAiraUserBizImpl(IEventRankingService eventRankingService, IAiraBindRelationService airaBindRelationService) {
        this.eventRankingService = eventRankingService;
        this.airaBindRelationService = airaBindRelationService;
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

}
