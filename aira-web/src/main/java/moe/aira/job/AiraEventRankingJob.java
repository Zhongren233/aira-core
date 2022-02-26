package moe.aira.job;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import moe.aira.config.EventConfig;
import moe.aira.core.service.IEventRankingService;
import moe.aira.enums.EventStatus;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Component
public class AiraEventRankingJob {
    final
    IEventRankingService eventRankingService;
    final
    EventConfig config;

    public AiraEventRankingJob(IEventRankingService eventRankingService, EventConfig config) {
        this.eventRankingService = eventRankingService;
        this.config = config;
    }

    @XxlJob("fetchPointRankingHandler")
    public void fetchPointRankingJob() throws InterruptedException {
        if (config.getEventStatus() != EventStatus.OPEN) {
            XxlJobHelper.log("非活动状态");
            return;
        }
        Timer timer = new Timer();
        CountDownLatch countDownLatch = eventRankingService.fetchAllPointRanking();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                XxlJobHelper.log("剩余{}页", countDownLatch.getCount());
            }
        }, 0, 10000);
        if (!countDownLatch.await(5, TimeUnit.MINUTES)) {
            XxlJobHelper.log("超时爬取");
            XxlJobHelper.handleTimeout();
        }
        timer.cancel();
    }


    @XxlJob("fetchScoreRankingHandler")
    public void fetchScoreRankingJob() throws InterruptedException {
        if (config.getEventStatus() != EventStatus.OPEN) {
            XxlJobHelper.log("非Open状态");
            return;
        }
        Timer timer = new Timer();
        CountDownLatch countDownLatch = eventRankingService.fetchAllScoreRanking();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                XxlJobHelper.log("剩余{}页", countDownLatch.getCount());
            }
        }, 0, 10000);
        if (!countDownLatch.await(5, TimeUnit.MINUTES)) {
            XxlJobHelper.log("超时爬取");
            XxlJobHelper.handleTimeout();
        }
        timer.cancel();
    }

    @XxlJob("updateStatus")
    public void updateStatus() {
        switch (config.getEventStatus()) {
            case COUNTING_END:
                config.setEventStatus(EventStatus.ANNOUNCE);
                break;
            case END:
                config.setEventStatus(EventStatus.COUNTING_END);
                break;
            case ANNOUNCE:
                config.setEventStatus(EventStatus.OPEN);
                break;
        }
    }

}
