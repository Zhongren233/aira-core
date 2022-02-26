package moe.aira.config;

import lombok.Data;
import moe.aira.enums.EventStatus;

@Data
public class EventConfig {
    private Integer eventId;
    private EventStatus eventStatus;

   public boolean checkAvailable() {
        return eventStatus == EventStatus.OPEN || eventStatus == EventStatus.COUNTING_END;
    }
}

