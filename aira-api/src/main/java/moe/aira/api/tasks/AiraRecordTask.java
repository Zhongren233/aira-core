package moe.aira.api.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import moe.aira.config.EventConfig;
import moe.aira.core.biz.IAiraEventBiz;
import moe.aira.core.biz.IAiraObserverBiz;
import moe.aira.core.biz.IAiraTricolorBiz;
import moe.aira.core.biz.IAiraUserBiz;
import moe.aira.core.client.es.MyPageClient;
import moe.aira.core.manager.IEventConfigManager;
import moe.aira.core.service.IAiraLogPointService;
import moe.aira.core.service.IAiraLogScoreService;
import moe.aira.entity.aira.*;
import moe.aira.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    @Autowired
    private IAiraUserBiz iAiraUserBiz;
    @Autowired
    private IAiraObserverBiz iAiraObserverBiz;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void task() {
        EventConfig eventConfig = eventConfigManager.fetchEventConfig();
        if (eventConfig.getEventStatus() == EventStatus.OPEN) {
            Date trucDate = new Date((System.currentTimeMillis() / 1000 / 60) * 1000 * 60);
            log.info("开始记录");
            recordPointRanking(trucDate, eventConfig.getEventId());
            recordScoreRanking(trucDate, eventConfig.getEventId());
            recordObserver();

            log.info("记录完成");
        }
//        recordTricolor();
    }

    private void recordObserver() {
        log.info("observer service");
        iAiraObserverBiz.observe();
    }

    @Autowired
    IAiraTricolorBiz tricolorBiz;
    private void recordTricolor() {
        AiraTriColorMatsuriInfo airaTriColorMatsuriInfo = tricolorBiz.fetchInfo();
        List<AiraTricolorLog> collect = airaTriColorMatsuriInfo.getBattleInfos().stream().map(a -> {
            AiraTricolorLog airaTricolorLog = new AiraTricolorLog();
            airaTricolorLog.setBattleId(a.getBattleId());
            airaTricolorLog.setScore(a.getScore());
            airaTricolorLog.setTeamId(a.getTeamId());
            airaTricolorLog.setFever(airaTriColorMatsuriInfo.getFever());
            airaTricolorLog.setPeriodId(airaTriColorMatsuriInfo.getPeriodId());
            return airaTricolorLog;
        }).collect(Collectors.toList());
        tricolorBiz.saveLog(collect);

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
        if (eventId == 274) {
            ArrayList<AiraLogScore> airaLogScores = new ArrayList<>();
            List<AiraEventScoreDto> red = eventBiz.fetchCurrentRankScore(100005);
            List<AiraEventScoreDto> white = eventBiz.fetchCurrentRankScore(100006);
            red.stream().map(airaEventScoreDto -> {
                AiraLogScore airaLogScore = new AiraLogScore();
                airaLogScore.setUserId(airaEventScoreDto.getUserId());
                airaLogScore.setLogRank(airaEventScoreDto.getRank());
                airaLogScore.setLogScore(airaEventScoreDto.getScore());
                airaLogScore.setEventId(eventId);
                airaLogScore.setCreateTime(truncDate);
                airaLogScore.setColorTypeId(100005);
                return airaLogScore;
            }).forEach(airaLogScores::add);
            white.stream().map(airaEventScoreDto -> {
                AiraLogScore airaLogScore = new AiraLogScore();
                airaLogScore.setUserId(airaEventScoreDto.getUserId());
                airaLogScore.setLogRank(airaEventScoreDto.getRank());
                airaLogScore.setLogScore(airaEventScoreDto.getScore());
                airaLogScore.setEventId(eventId);
                airaLogScore.setCreateTime(truncDate);
                airaLogScore.setColorTypeId(100006);
                return airaLogScore;
            }).forEach(airaLogScores::add);
            logScoreService.saveBatch(airaLogScores);

        } else {

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
        }

        log.info("记录Score完成");
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void myPage() {
        log.info("开始保活");
        JsonNode node = client.myPage();
        log.info("保活完成,{}", node.toString());
    }


}
