package moe.aira.core.service;

import moe.aira.core.entity.es.Christmas2020Tree;

public interface IEventRankingService {
    void fetchAllEventRanking() throws InterruptedException;

    void fetchUserEventStat(Integer userId);

    Integer countAchievePointUser(Integer point);

    Christmas2020Tree fetchChristmas2020Tree();
}
