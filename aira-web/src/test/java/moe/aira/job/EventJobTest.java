package moe.aira.job;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EventJobTest {
    @Autowired
    EventJob eventJob;

    @Test
    void fetchAllPointRankingJob() throws InterruptedException {
        eventJob.fetchAllPointRankingJob();
        Thread.sleep(3000000);

    }

    @Test
    void fetchAllScoreRankingJob() {
        eventJob.fetchAllScoreRankingJob();
    }
}