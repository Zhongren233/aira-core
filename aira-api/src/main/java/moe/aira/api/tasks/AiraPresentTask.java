package moe.aira.api.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import moe.aira.core.client.es.PresentsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AiraPresentTask {
    @Autowired
    PresentsClient presentsClient;

    @Scheduled(fixedDelay = 60, timeUnit = TimeUnit.MINUTES)
    public void task() {
        JsonNode presents = presentsClient.presents("1");
        if (presents.get("user_presents").size() != 0) {
            JsonNode node = presentsClient.receiveAll();
            log.info("领取礼物:{}", node);
        }
    }
}
