package moe.aira.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import moe.aira.core.client.es.EventsClient;
import moe.aira.enums.EventStatus;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@Data
public class EventConfig {
    private Integer eventId;
    private EventStatus eventStatus;

    public EventConfig(EventsClient eventsClient) {
        JsonNode index = eventsClient.index();
        System.out.println(index);
        if (index.get("app_status_code").intValue() == 0) {
            log.info("Open");
            eventStatus = EventStatus.Open;
        } else {
            log.info("NOT OPEN");
            eventStatus = EventStatus.End;
        }
    }

    public boolean isAvailable() {
        return eventStatus == EventStatus.Open || eventStatus == EventStatus.CountingEnd;
    }


}

