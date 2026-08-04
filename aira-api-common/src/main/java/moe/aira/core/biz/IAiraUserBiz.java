package moe.aira.core.biz;

import moe.aira.annotation.EventAvailable;
import moe.aira.entity.aira.AiraEventRanking;
import moe.aira.entity.aira.AiraSSFEventRanking;
import moe.aira.entity.es.UserInfo;

import java.util.concurrent.CompletableFuture;

public interface IAiraUserBiz {
    AiraEventRanking fetchAiraEventRanking(Integer userId);

    AiraSSFEventRanking fetchAiraSSFEventRanking(Integer userId);

    AiraSSFEventRanking fetchAiraTwoUnitEventRanking(Integer userId);

    UserInfo fetchUserInfo(String uidCode);
}
