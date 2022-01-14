package moe.aira.core.biz;

import moe.aira.core.entity.aira.AiraEventRanking;

public interface IAiraEventRankingBiz {
    AiraEventRanking fetchAiraEventRanking(Integer userId);
}
