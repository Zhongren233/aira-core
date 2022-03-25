package moe.aira.api.tasks;

import lombok.extern.slf4j.Slf4j;
import moe.aira.config.EventConfig;
import moe.aira.core.biz.IAiraEventBiz;
import moe.aira.core.client.es.MyPageClient;
import moe.aira.core.manager.IEventConfigManager;
import moe.aira.core.service.IAiraLogPointService;
import moe.aira.core.service.IAiraLogScoreService;
import moe.aira.entity.aira.AiraEventPointDto;
import moe.aira.entity.aira.AiraEventScoreDto;
import moe.aira.entity.aira.AiraLogPoint;
import moe.aira.entity.aira.AiraLogScore;
import moe.aira.enums.EventStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
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
    final
    IAiraLogScoreService logScoreService;

    final
    MyPageClient client;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void task() {
        EventConfig eventConfig = eventConfigManager.fetchEventConfig();
        if (eventConfig.getEventStatus() == EventStatus.OPEN) {
            Date trucDate = new Date((System.currentTimeMillis() / 1000 / 60) * 1000 * 60);
            log.info("开始记录");
            recordPointRanking(trucDate, eventConfig.getEventId());
            recordScoreRanking(trucDate, eventConfig.getEventId());
            log.info("记录完成");
        }
    }

    public AiraRecordTask(IEventConfigManager eventConfigManager, IAiraEventBiz eventBiz, IAiraLogPointService logPointService, IAiraLogScoreService logScoreService, MyPageClient client) {
        this.eventConfigManager = eventConfigManager;
        this.eventBiz = eventBiz;
        this.logPointService = logPointService;
        this.logScoreService = logScoreService;
        this.client = client;
    }

    private void recordPointRanking(Date trucDate, Integer eventId) {
        log.info("开始记录Point");
        List<AiraEventPointDto> data = eventBiz.fetchCurrentRankPoint();
        List<AiraLogPoint> collect = data.stream().map(airaEventPointDto -> {
            AiraLogPoint airaLogPoint = new AiraLogPoint();
            airaLogPoint.setUserId(airaEventPointDto.getUserId());
            airaLogPoint.setLogRank(airaEventPointDto.getRank());
            airaLogPoint.setLogPoint(airaEventPointDto.getPoint());
            airaLogPoint.setEventId(eventId);
            airaLogPoint.setCreateTime(trucDate);
            return airaLogPoint;
        }).collect(Collectors.toList());
        logPointService.saveBatch(collect);
        log.info("记录Point完成");
    }

    private void recordScoreRanking(Date truncDate, Integer eventId) {
        log.info("开始记录Score");
        List<AiraEventScoreDto> data = eventBiz.fetchCurrentRankScore();
        List<AiraLogScore> collect = data.stream().map(airaEventScoreDto -> {
            AiraLogScore airaLogScore = new AiraLogScore();
            airaLogScore.setUserId(airaEventScoreDto.getUserId());
            airaLogScore.setLogRank(airaEventScoreDto.getRank());
            airaLogScore.setLogScore(airaEventScoreDto.getScore());
            airaLogScore.setEventId(eventId);
            airaLogScore.setCreateTime(truncDate);
            return airaLogScore;
        }).collect(Collectors.toList());
        logScoreService.saveBatch(collect);
        log.info("记录Score完成");
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void myPage() {
        log.info("开始保活");
        client.myPage();
        log.info("保活完成");
    }


}
