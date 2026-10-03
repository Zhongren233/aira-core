package moe.aira.onebot.entity;

import lombok.Getter;
import lombok.Setter;
import moe.aira.config.EventConfig;

import java.util.List;

@Getter
@Setter
public class AiraTourAwardDto extends AiraAwardDto {
    private Integer[] oneCards;
    private Integer[] twoCards;

    public AiraTourAwardDto(EventConfig eventConfig, List<Integer> oneCounts, List<Integer> twoCounts) {
        super(eventConfig);
        oneCards = oneCounts.toArray(new Integer[0]);
        twoCards = twoCounts.toArray(new Integer[0]);
    }
}
