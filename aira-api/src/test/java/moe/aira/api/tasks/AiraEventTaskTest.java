package moe.aira.api.tasks;

import moe.aira.core.service.IEventRankingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

@SpringBootTest
class AiraEventTaskTest {
    @Autowired
    IEventRankingService eventRankingService;

    @Test
    void test() throws InterruptedException {
        CountDownLatch countDownLatch = eventRankingService.fetchAllPointRanking();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(countDownLatch.getCount());
            }
        }, 0, 1000);
        countDownLatch.await();
        timer.cancel();
    }

    @Test
    void test2() throws InterruptedException {
        CountDownLatch countDownLatch = eventRankingService.fetchAllScoreRanking();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(countDownLatch.getCount());
            }
        }, 0, 1000);
        countDownLatch.await();
        timer.cancel();
    }
}