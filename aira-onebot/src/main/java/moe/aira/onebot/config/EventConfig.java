package moe.aira.onebot.config;

import lombok.Data;
import lombok.experimental.Accessors;
import moe.aira.enums.EventStatus;
import moe.aira.enums.EventType;

@Data
@Accessors(chain = true)
public class EventConfig {
    private Integer eventId;
    private EventStatus eventStatus;
    private EventType eventType;

    public boolean checkAvailable() {
        return eventStatus == EventStatus.OPEN || eventStatus == EventStatus.COUNTING_END;
    }
}

