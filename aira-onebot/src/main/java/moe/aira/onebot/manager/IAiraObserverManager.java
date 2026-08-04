package moe.aira.onebot.manager;

import moe.aira.entity.aira.AiraObserverDetail;
import moe.aira.entity.aira.AiraObserverInfo;

import java.util.List;

public interface IAiraObserverManager {

    AiraObserverInfo getAiraObserverInfo(Integer userId);

    AiraObserverInfo regAiraObserver(Integer userId);

    AiraObserverDetail getLastestAiraObserverDetail(Integer userId);

    List<AiraObserverDetail> getAllAiraObserverDetails(Integer userId, Integer eventId);
}

