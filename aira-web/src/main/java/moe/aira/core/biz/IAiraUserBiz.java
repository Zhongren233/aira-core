package moe.aira.core.biz;

import moe.aira.core.entity.aira.AiraUser;
import moe.aira.entity.aira.AiraEventRanking;

public interface IAiraUserBiz {
    AiraEventRanking fetchAiraEventRanking(Integer userId);

    /**
     * 获取Bind关系
     */
    AiraUser selectAiraEventBind(String qqNumber);

    AiraUser bindUser(Integer userId, String qqNumber);
}
