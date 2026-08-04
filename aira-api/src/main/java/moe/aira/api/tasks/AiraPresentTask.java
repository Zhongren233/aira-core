package moe.aira.api.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import moe.aira.core.client.es.PresentsClient;
import moe.aira.core.client.es.StoryClient;
import moe.aira.core.client.hekk.BaseClient;
import moe.aira.core.client.hekk.PresentClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AiraPresentTask {
    @Autowired
    PresentClient presentsClient;
    @Autowired
    PresentsClient cnPresentCLlient;
    @Autowired
    BaseClient baseClient;
    @Scheduled(fixedDelay = 60, timeUnit = TimeUnit.MINUTES)
    public void task() {
        JsonNode presents = cnPresentCLlient.presents("1");
        if (!presents.get("user_presents").isEmpty()) {
            JsonNode node = cnPresentCLlient.receiveAll();
            log.info("领取礼物:{}", node);
        }
    }

    @Autowired
    StoryClient storyClient;

    @Scheduled(fixedDelay = 60, timeUnit = TimeUnit.MINUTES)
    public void task2() {
        JsonNode jsonNode = storyClient.es1Recommend();
        if (jsonNode.get("story") != null) {
            return;
        }

        JsonNode es1Recommend = storyClient.es1Recommend("4");
        log.info("es1 :{}", es1Recommend);

    }

    @Scheduled(fixedDelay = 12, timeUnit = TimeUnit.HOURS)
    public void jpTask() {
        JsonNode mypage = baseClient.mypage();
        log.info("日服mypage:{}", mypage);
    }
}
