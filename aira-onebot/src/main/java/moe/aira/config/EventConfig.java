package moe.aira.config;

import lombok.Data;
import lombok.experimental.Accessors;
import moe.aira.entity.aira.AiraEventAward;
import moe.aira.enums.EventStatus;
import moe.aira.enums.EventType;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class EventConfig {
    private Integer eventId;
    private EventStatus eventStatus;
    private EventType eventType;
    List<AiraEventAward> eventAwards;
    /**
     * 妈的 这个local-datetime 是真傻逼
     */
    private Date startTime;
    private String eventCnName;
    private String eventJpName;

    public boolean checkAvailable() {
        return eventStatus == EventStatus.OPEN || eventStatus == EventStatus.COUNTING_END;
    }
}

