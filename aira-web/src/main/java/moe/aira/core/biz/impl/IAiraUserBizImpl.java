package moe.aira.core.biz.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import moe.aira.annotation.EventAvailable;
import moe.aira.core.biz.IAiraUserBiz;
import moe.aira.core.entity.aira.AiraEventRanking;
import moe.aira.core.entity.aira.AiraUser;
import moe.aira.core.entity.dto.UserRanking;
import moe.aira.core.entity.es.PointRanking;
import moe.aira.core.entity.es.ScoreRanking;
import moe.aira.core.service.IAiraBindRelationService;
import moe.aira.core.service.IEventRankingService;
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

    @Override
    public AiraUser selectAiraEventBind(String qqNumber) {
        QueryWrapper<AiraUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("qq_number", qqNumber);
        return airaBindRelationService.getOne(queryWrapper);
    }

    /**
     * 用好友通道绑定
     */
    @Override
    public AiraUser bindUser(Integer userId, String qqNumber) {
        return null;
    }
}
