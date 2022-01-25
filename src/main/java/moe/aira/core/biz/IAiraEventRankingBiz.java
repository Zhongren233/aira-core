package moe.aira.core.biz;

import moe.aira.core.entity.aira.AiraBindRelation;
import moe.aira.core.entity.aira.AiraEventRanking;

public interface IAiraEventRankingBiz {
    AiraEventRanking fetchAiraEventRanking(Integer userId);

    /**获取Bind关系*/
    AiraBindRelation selectAiraEventBind(Long qqNumber);
}
