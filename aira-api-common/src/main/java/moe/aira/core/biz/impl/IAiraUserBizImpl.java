package moe.aira.core.biz.impl;

import moe.aira.annotation.EventAvailable;
import moe.aira.core.biz.IAiraUserBiz;
import moe.aira.core.entity.dto.UserRanking;
import moe.aira.core.service.IEventRankingService;
import moe.aira.core.service.IFriendService;
import moe.aira.entity.aira.AiraEventRanking;
import moe.aira.entity.aira.AiraSSFEventRanking;
import moe.aira.entity.es.PointRanking;
import moe.aira.entity.es.ScoreRanking;
import moe.aira.entity.es.UserInfo;
import moe.aira.enums.AiraEventRankingStatus;
import moe.aira.exception.client.AiraNoUserDataException;
import moe.aira.exception.server.AiraTooManyResultException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Component
public class IAiraUserBizImpl implements IAiraUserBiz {
    final
    IEventRankingService eventRankingService;

    final
    IFriendService friendService;
    @Value("${aira.core.stat-level}")
    private AiraEventRankingStatus rankingLevel;

    public IAiraUserBizImpl(IEventRankingService eventRankingService, IFriendService friendService) {
        this.eventRankingService = eventRankingService;
        this.friendService = friendService;
    }

    @Override
    @EventAvailable
    public AiraEventRanking fetchAiraEventRanking(Integer userId) {
        AiraEventRanking airaEventRanking = new AiraEventRanking();
        airaEventRanking.setStatus(rankingLevel);
        UserRanking<PointRanking> pointRanking = eventRankingService.fetchPointRankingByUserId(userId, rankingLevel);
        AiraEventRankingStatus status = pointRanking.getStatus();
        airaEventRanking.setPointUpdateTime(new Date());
        if (status != rankingLevel) {
            airaEventRanking.setStatus(status);
            PointRanking ranking = pointRanking.getRanking();
            if (ranking != null) {
                airaEventRanking.setPointUpdateTime(ranking.getUpdateTime());
            }
        }
        if (status != AiraEventRankingStatus.NO_DATA) {
            UserRanking<ScoreRanking> scoreRankingUser = eventRankingService.fetchScoreRankingByUserId(userId, rankingLevel);
            ScoreRanking scoreRanking = scoreRankingUser.getRanking();
            airaEventRanking.setScoreRanking(scoreRanking);
            airaEventRanking.setUserId(userId);
            airaEventRanking.setEventId(pointRanking.getRanking().getEventId());
            airaEventRanking.setUserProfile(pointRanking.getProfile());
            if (scoreRanking != null) {
                airaEventRanking.setScoreUpdateTime(scoreRanking.getUpdateTime());
            }
            airaEventRanking.setPointRanking(pointRanking.getRanking());
        }
        return airaEventRanking;
    }

    @Override
    public AiraSSFEventRanking fetchAiraSSFEventRanking(Integer userId) {
        AiraSSFEventRanking airaSSFEventRanking = new AiraSSFEventRanking();
        airaSSFEventRanking.setStatus(rankingLevel);
        UserRanking<PointRanking> pointRanking = eventRankingService.fetchPointRankingByUserId(userId, rankingLevel);
        AiraEventRankingStatus status = pointRanking.getStatus();
        airaSSFEventRanking.setPointUpdateTime(new Date());
        if (status != rankingLevel) {
            airaSSFEventRanking.setStatus(status);
            PointRanking ranking = pointRanking.getRanking();
            if (ranking != null) {
                airaSSFEventRanking.setPointUpdateTime(ranking.getUpdateTime());
            }
        }

        if (status != AiraEventRankingStatus.NO_DATA) {
            UserRanking<ScoreRanking> white = eventRankingService.fetchScoreRankingByUserId(userId, rankingLevel, "WHITE");
            UserRanking<ScoreRanking> red = eventRankingService.fetchScoreRankingByUserId(userId, rankingLevel, "RED");
            airaSSFEventRanking.setWhiteScoreRanking(white.getRanking());
            airaSSFEventRanking.setRedScoreRanking(red.getRanking());
            airaSSFEventRanking.setUserId(userId);
            airaSSFEventRanking.setEventId(pointRanking.getRanking().getEventId());
            airaSSFEventRanking.setUserProfile(pointRanking.getProfile());

            airaSSFEventRanking.setPointRanking(pointRanking.getRanking());
        }
        return airaSSFEventRanking;
    }

    @Override
    public UserInfo fetchUserInfo(String uidCode) {
        List<UserInfo> list = friendService.fetchFriendSearchList(uidCode);
        int size = list.size();
        if (size > 1) {
            throw new AiraTooManyResultException(MessageFormat.format("期望获取1个用户，实际获取{0}个", size));
        }
        if (list.isEmpty()) {
            throw new AiraNoUserDataException("没有获取到用户");
        }
        return list.get(0);
    }

}
