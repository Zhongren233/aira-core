package moe.aira.onebot.entity;

import lombok.Getter;
import lombok.Setter;
import moe.aira.config.EventConfig;
import moe.aira.entity.aira.AiraEventAward;

import java.util.List;

@Getter
@Setter
public abstract class AiraAwardDto {
    private Integer eventId;
    private String eventCnName;
    private String eventJpName;

    private List<AiraEventAward> awards;

    public AiraAwardDto(EventConfig eventConfig) {
        eventId = eventConfig.getEventId();
        eventCnName = eventConfig.getEventCnName();
        eventJpName = eventConfig.getEventJpName();
        awards = eventConfig.getEventAwards();
    }
}
