package moe.aira.core.biz.impl;

import lombok.extern.slf4j.Slf4j;
import moe.aira.config.EventConfig;
import moe.aira.core.biz.IAiraEventBiz;
import moe.aira.core.entity.dto.UserRanking;
import moe.aira.core.manager.IEventConfigManager;
import moe.aira.core.service.IEventRankingService;
import moe.aira.entity.aira.AiraEventPointDto;
import moe.aira.entity.aira.AiraEventScoreDto;
import moe.aira.entity.es.PointRanking;
import moe.aira.entity.es.ScoreRanking;
import moe.aira.enums.EventRank;
import moe.aira.enums.EventType;
import moe.aira.exception.client.AiraIllegalParamsException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
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
    public List<AiraEventPointDto> fetchCurrentRankPoint() {
        return fetchCurrentRankPoint(
                Arrays.stream(EventRank.values()).map(EventRank::getRank).collect(Collectors.toList()).toArray(Integer[]::new)
        );
    }

    @Override
    public List<AiraEventPointDto> fetchCurrentRankPoint(Integer... ranks) {

        return Arrays.stream(ranks)
                .map(rank -> CompletableFuture.supplyAsync(() -> {
                    UserRanking<PointRanking> pointRankingUserRanking = eventRankingService.fetchPointRankingByRank(rank);
                    AiraEventPointDto airaEventPointDto = new AiraEventPointDto();
                    airaEventPointDto.setRank(rank);
                    airaEventPointDto.setPoint(pointRankingUserRanking.getRanking().getEventPoint());
                    airaEventPointDto.setUserId(pointRankingUserRanking.getUserId());
                    return airaEventPointDto;
                }))
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    @Override
    public List<AiraEventScoreDto> fetchCurrentRankScore() {
        return fetchCurrentRankScore(
                Arrays.stream(EventRank.values()).map(EventRank::getRank).collect(Collectors.toList()).toArray(Integer[]::new)
        );
    }


    @Override
    public List<AiraEventScoreDto> fetchCurrentRankScore(Integer... ranks) {
        return Arrays.stream(ranks)
                .map(rank -> CompletableFuture.supplyAsync(() -> {
                    UserRanking<ScoreRanking> pointRankingUserRanking = eventRankingService.fetchScoreRankingByRank(rank);
                    AiraEventScoreDto scoreDto = new AiraEventScoreDto();
                    scoreDto.setRank(rank);
                    scoreDto.setScore(pointRankingUserRanking.getRanking().getEventPoint());
                    scoreDto.setUserId(pointRankingUserRanking.getUserId());
                    return scoreDto;
                }))
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }
}
