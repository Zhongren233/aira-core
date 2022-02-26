package moe.aira.config;

import lombok.Data;
import lombok.experimental.Accessors;
import moe.aira.enums.EventStatus;

@Data
@Accessors(chain = true)
public class EventConfig {
    private Integer eventId;
    private EventStatus eventStatus;

   public boolean checkAvailable() {
        return eventStatus == EventStatus.OPEN || eventStatus == EventStatus.COUNTING_END;
    }
}

