package moe.aira.api.tasks;

import lombok.extern.slf4j.Slf4j;
import moe.aira.core.biz.IAiraEventBiz;
import moe.aira.core.manager.IEventConfigManager;
import moe.aira.core.service.IAiraLogPointService;
import moe.aira.entity.aira.AiraEventPointDto;
import moe.aira.entity.aira.AiraLogPoint;
import moe.aira.enums.EventStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Component
@EnableScheduling
public class AiraRecordTask {
    final
    IEventConfigManager eventConfigManager;
    final
    IAiraEventBiz eventBiz;

    final
    IAiraLogPointService logPointService;

    public AiraRecordTask(IEventConfigManager eventConfigManager, IAiraEventBiz eventBiz, IAiraLogPointService logPointService) {
        this.eventConfigManager = eventConfigManager;
        this.eventBiz = eventBiz;
        this.logPointService = logPointService;
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void task() {
        if (eventConfigManager.fetchEventConfig().getEventStatus() == EventStatus.OPEN) {
            Date trucDate = new Date((System.currentTimeMillis() / 1000 / 60) * 10000 * 60);
            log.info("开始记录");
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> recordPointRanking(trucDate));
            CompletableFuture<Void> future1 = CompletableFuture.runAsync(this::recordScoreRanking);
            CompletableFuture<Void> future2 = CompletableFuture.runAsync(this::recordPointAwardCount);
            CompletableFuture.allOf(future, future1, future2).join();
            log.info("记录完成");
        }
    }

    public void recordPointRanking(Date trucDate) {
        List<AiraEventPointDto> data = eventBiz.fetchCurrentRankPoint();
        List<AiraLogPoint> collect = data.stream().map(airaEventPointDto -> {
            AiraLogPoint airaLogPoint = new AiraLogPoint();
            airaLogPoint.setLogRank(airaEventPointDto.getRank());
            airaLogPoint.setLogPoint(airaEventPointDto.getPoint());
            airaLogPoint.setCreateTime(trucDate);
            return airaLogPoint;
        }).collect(Collectors.toList());
        logPointService.saveBatch(collect);
    }


    public void recordScoreRanking() {

    }

    public void recordPointAwardCount() {

    }
}
