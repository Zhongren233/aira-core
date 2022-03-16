package moe.aira.core.biz.impl;

import moe.aira.config.EventConfig;
import moe.aira.core.biz.IAiraEventBiz;
import moe.aira.core.manager.IEventConfigManager;
import moe.aira.core.service.IEventRankingService;
import moe.aira.enums.EventRank;
import moe.aira.enums.EventType;
import moe.aira.exception.client.AiraIllegalParamsException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class IAiraEventBizImpl implements IAiraEventBiz {
    final
    IEventConfigManager eventConfigManager;

    final
    IEventRankingService eventRankingService;


    public IAiraEventBizImpl(IEventConfigManager eventConfigManager, IEventRankingService eventRankingService) {
        this.eventConfigManager = eventConfigManager;
        this.eventRankingService = eventRankingService;
    }

    @Override
    public Map<Integer, Integer> countEventPointBatch() {
        EventConfig eventConfig = eventConfigManager.fetchEventConfig();
        if (eventConfig.getEventType() == EventType.TOUR) {
            return countEventPointBatch(
                    300 * 10000,
                    750 * 10000,
                    950 * 10000,
                    1500 * 10000,
                    2100 * 10000,
                    350 * 10000,
                    600 * 10000,
                    1100 * 10000,
                    1350 * 10000,
                    2200 * 10000
            );
        } else {
            return countEventPointBatch(
                    350 * 10000,
                    750 * 10000,
                    1100 * 10000,
                    1500 * 10000,
                    2200 * 10000
            );
        }
    }


    @Override
    public Map<Integer, Integer> countEventPointBatch(Integer... points) {
        int length = points.length;
        if (points.length > 20) {
            throw new AiraIllegalParamsException();
        }

        LinkedHashMap<Integer, Integer> pointCountMap = new LinkedHashMap<>(length);

        for (Integer point : Arrays.stream(points).sorted().collect(Collectors.toList()))
            pointCountMap.put(point,
                    eventRankingService.countPointRankingWhereGtPoint(point));
        return pointCountMap;
    }

    @Override
    public Map<Integer, Integer> fetchCurrentRankPoint() {
        return fetchCurrentRankPoint(
                Arrays.stream(EventRank.values()).map(EventRank::getRank).collect(Collectors.toList()).toArray(Integer[]::new)
        );
    }

    @Override
    public Map<Integer, Integer> fetchCurrentRankPoint(Integer... ranks) {
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>(ranks.length);
        for (Integer rank : ranks) {
            Integer eventPoint = eventRankingService.fetchPointRankingByRank(rank).getRanking().getEventPoint();
            map.put(rank, eventPoint);
        }
        return map;
    }

    @Override
    public Map<Integer, Integer> fetchCurrentRankScore() {
        return fetchCurrentRankScore(
                Arrays.stream(EventRank.values()).map(EventRank::getRank).collect(Collectors.toList()).toArray(Integer[]::new)
        );
    }

    @Override
    public Map<Integer, Integer> fetchCurrentRankScore(Integer... ranks) {
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>(ranks.length);
        for (Integer rank : ranks) {
            Integer eventPoint = eventRankingService.fetchScoreRankingByRank(rank).getRanking().getEventPoint();
            map.put(rank, eventPoint);
        }
        return map;
    }
}
