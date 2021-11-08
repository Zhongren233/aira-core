package moe.aira.core.service;

public interface IEventRankingService {
    void fetchAllEventRanking() throws InterruptedException;

    void fetchUserEventStat(Integer userId);

    Integer countAchievePointUser(Integer point);
}
