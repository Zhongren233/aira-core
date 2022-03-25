package moe.aira.config;

import lombok.Data;
import lombok.experimental.Accessors;
import moe.aira.entity.aira.AiraEventAward;
import moe.aira.enums.EventStatus;
import moe.aira.enums.EventType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class EventConfig {
    private Integer eventId;
    // 为了方便计算
    private LocalDateTime startTime;
    private EventStatus eventStatus;
    private EventType eventType;
    private List<AiraEventAward> eventAwards;

    public boolean checkAvailable() {
        return eventStatus == EventStatus.OPEN || eventStatus == EventStatus.COUNTING_END;
    }
}

