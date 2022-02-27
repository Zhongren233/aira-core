package moe.aira.core.biz;

import moe.aira.entity.aira.AiraEventRanking;

public interface IAiraUserBiz {
    AiraEventRanking fetchAiraEventRanking(Integer userId);

}
