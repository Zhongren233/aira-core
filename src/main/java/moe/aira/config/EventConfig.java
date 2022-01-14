package moe.aira.config;

import lombok.Data;
import moe.aira.core.client.es.EventsClient;
import moe.aira.enums.EventStatus;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class EventConfig {
    private Integer eventId;
    private EventStatus eventStatus;

    public EventConfig(EventsClient eventsClient) {
//        JsonNode node = eventsClient.eventAnnounce();

    }


}

