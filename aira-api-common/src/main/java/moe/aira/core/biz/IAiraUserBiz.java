package moe.aira.core.biz;

import moe.aira.entity.aira.AiraEventRanking;
import moe.aira.entity.aira.AiraSSFEventRanking;
import moe.aira.entity.es.UserInfo;

public interface IAiraUserBiz {
    AiraEventRanking fetchAiraEventRanking(Integer userId);

    AiraSSFEventRanking fetchAiraSSFEventRanking(Integer userId);

    UserInfo fetchUserInfo(String uidCode);
}
