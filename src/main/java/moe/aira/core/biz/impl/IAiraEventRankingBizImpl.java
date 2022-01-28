package moe.aira.core.biz.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import moe.aira.annotation.EventAvailable;
import moe.aira.core.biz.IAiraEventRankingBiz;
import moe.aira.core.entity.aira.AiraBindRelation;
import moe.aira.core.entity.aira.AiraEventRanking;
import moe.aira.core.entity.dto.UserRanking;
import moe.aira.core.entity.es.PointRanking;
import moe.aira.core.entity.es.ScoreRanking;
import moe.aira.core.service.IAiraBindRelationService;
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

    final
    IAiraBindRelationService airaBindRelationService;

    public IAiraEventRankingBizImpl(IEventRankingService eventRankingService, IAiraBindRelationService airaBindRelationService) {
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

    @Override
    public AiraBindRelation selectAiraEventBind(String qqNumber) {
        QueryWrapper<AiraBindRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("qq_number", qqNumber);
        return airaBindRelationService.getOne(queryWrapper);
    }
}
