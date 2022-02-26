package moe.aira.core.service.impl;

import lombok.extern.slf4j.Slf4j;
import moe.aira.core.service.IEventRankingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class IEventRankingServiceImplTest {
    @Autowired
    IEventRankingService eventRankingService;

    @Test
    void fetchAllPointRanking() throws InterruptedException {
        log.info("开始测试fetchAllPointRanking");
        long l = System.currentTimeMillis();
        CountDownLatch countDownLatch = eventRankingService.fetchAllPointRanking();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                log.info("剩余页数:{}", countDownLatch.getCount());
            }
        }, 0, 5 * 1000);
        countDownLatch.await();
        log.info("总耗时：{} ms", System.currentTimeMillis() - l);
        timer.cancel();

    }

    @Test
    void fetchAllScoreRanking() throws InterruptedException {
        log.info("开始测试fetchAllPointRanking");
        long l = System.currentTimeMillis();
        CountDownLatch countDownLatch = eventRankingService.fetchAllScoreRanking();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                log.info("剩余页数:{}", countDownLatch.getCount());
            }
        }, 0, 5 * 1000);
        countDownLatch.await();
        log.info("总耗时：{} ms", System.currentTimeMillis() - l);
        timer.cancel();

    }

    @Test
    void countScoreRankingWhereGtPoint() {
        int point = 142556;
        Integer count = eventRankingService.countScoreRankingWhereGtPoint(point);
        log.info("{}pt:{}",point,count);
    }
}