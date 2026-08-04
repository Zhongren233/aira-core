package moe.aira.core.biz.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import moe.aira.core.biz.IAiraObserverBiz;
import moe.aira.core.biz.IAiraUserBiz;
import moe.aira.core.dao.AiraObserverDetailMapper;
import moe.aira.core.dao.AiraObserverInfoMapper;
import moe.aira.core.entity.dto.UserRanking;
import moe.aira.core.manager.IEventRankingManager;
import moe.aira.entity.aira.AiraEventRanking;
import moe.aira.entity.aira.AiraObserverDetail;
import moe.aira.entity.aira.AiraObserverInfo;
import moe.aira.entity.es.PointRanking;
import moe.aira.enums.AiraEventRankingStatus;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component

public class IAiraObserverBizImpl implements IAiraObserverBiz {


    private final AiraObserverInfoMapper airaObserverInfoMapper;
    private final IAiraUserBiz iAiraUserBiz;
    private final AiraObserverDetailMapper airaObserverDetailMapper;

    public IAiraObserverBizImpl(AiraObserverInfoMapper airaObserverInfoMapper, IAiraUserBiz iAiraUserBiz, AiraObserverDetailMapper airaObserverDetailMapper) {
        this.airaObserverInfoMapper = airaObserverInfoMapper;
        this.iAiraUserBiz = iAiraUserBiz;
        this.airaObserverDetailMapper = airaObserverDetailMapper;
    }

    @Override
    public void observe() {
        List<Integer> integers = airaObserverInfoMapper.selectObserverUser();

        integers.forEach(userId -> CompletableFuture.runAsync(() -> {
            AiraEventRanking airaEventRanking = iAiraUserBiz.fetchAiraEventRanking(userId);
            if (airaEventRanking == null) {
                return;
            }

            AiraEventRankingStatus status = airaEventRanking.getStatus();
            if (status == AiraEventRankingStatus.NOT_REALTIME_SCORE_RANKING || status == AiraEventRankingStatus.REALTIME_DATA) {
                PointRanking pointRanking = airaEventRanking.getPointRanking();
                if (pointRanking == null) {
                    return;
                }

                AiraObserverDetail airaObserverDetail = new AiraObserverDetail();
                airaObserverDetail.setUserId(pointRanking.getUserId());
                airaObserverDetail.setObEventPoint(pointRanking.getEventPoint());
                airaObserverDetail.setObTime(new Date()); // 非同期タスク実行時の現在時刻
                airaObserverDetail.setEventId(pointRanking.getEventId());
                airaObserverDetailMapper.insert(airaObserverDetail);
            }
        }));

    }

    @Override
    public List<AiraObserverDetail> getObserverDetails(Integer userId) {
        return airaObserverDetailMapper.selectList(new QueryWrapper<AiraObserverDetail>().eq("user_id", userId));
    }
}
