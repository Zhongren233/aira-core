package moe.aira.entity.es;

import lombok.Data;

@Data
public abstract class EventRanking {
    private Integer eventId;
    private Integer userId;
    private Integer eventRank;
    private Integer eventPoint;
}
