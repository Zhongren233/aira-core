package moe.aira.task.job;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import moe.aira.config.EventConfig;
import moe.aira.core.biz.IAiraEventBiz;
import moe.aira.core.manager.IEventConfigManager;
import moe.aira.core.service.IEventRankingService;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class EventJob {
    final
    IEventConfigManager eventConfigManager;
    final
    IEventRankingService eventRankingService;
    final
    IAiraEventBiz eventBiz;

    public EventJob(IEventConfigManager eventConfigManager, IEventRankingService eventRankingService, IAiraEventBiz eventBiz) {
        this.eventConfigManager = eventConfigManager;
        this.eventRankingService = eventRankingService;
        this.eventBiz = eventBiz;
    }


    @XxlJob(
            "fetchAllPointRankingHandler"
    )
    public void fetchAllPointRankingJob() {
        Timer timer = new Timer();
        try {
            if (eventConfigManager.fetchEventConfig().checkAvailable()) {
                CountDownLatch countDownLatch = eventRankingService.fetchAllPointRanking();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        long count = countDownLatch.getCount();
                        XxlJobHelper.log("剩余{}页PointRanking", count);
                        log.info("剩余{}页", count);
                    }
                }, 0, 10000);
                if (!countDownLatch.await(5, TimeUnit.MINUTES)) {
                    XxlJobHelper.handleFail("超时爬取！");
                }
            } else {
                XxlJobHelper.log("当前功能不可用");
            }
        } catch (Exception e) {
            log.error("", e);
            XxlJobHelper.handleFail(e.toString());
        } finally {
            timer.cancel();
        }

    }

    @XxlJob("fetchAllScoreRankingHandler")
    public void fetchAllScoreRankingJob() {
        try {
            EventConfig eventConfig = eventConfigManager.fetchEventConfig();
            if (eventConfig.checkAvailable()) {
                CountDownLatch countDownLatch = eventRankingService.fetchAllScoreRanking();
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        XxlJobHelper.log("剩余{}页ScoreRanking", countDownLatch.getCount());
                    }
                }, 0, 10000);
                if (!countDownLatch.await(5, TimeUnit.MINUTES)) {
                    XxlJobHelper.handleFail("超时爬取！");
                }
                timer.cancel();
            } else {
                XxlJobHelper.log("当前功能不可用");
            }
        } catch (Exception e) {
            XxlJobHelper.handleFail(e.toString());
        }
    }

    @XxlJob("fetchAllSSScoreRankingHandler")
    public void fetchAllSSScoreRankingJob() {
        try {
            EventConfig eventConfig = eventConfigManager.fetchEventConfig();
            if (eventConfig.checkAvailable()) {
                CountDownLatch countDownLatch = eventRankingService.fetchAllSSScoreRanking(XxlJobHelper.getJobParam());
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        XxlJobHelper.log("剩余{}页ScoreRanking", countDownLatch.getCount());
                    }
                }, 0, 10000);
                if (!countDownLatch.await(5, TimeUnit.MINUTES)) {
                    XxlJobHelper.handleFail("超时爬取！");
                }
                timer.cancel();
            } else {
                XxlJobHelper.log("当前功能不可用");
            }
        } catch (Exception e) {
            XxlJobHelper.handleFail(e.toString());
        }
    }


}
