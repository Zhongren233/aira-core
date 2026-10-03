package moe.aira.onebot.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import moe.aira.config.EventConfig;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class AiraUnitAwardDto extends AiraAwardDto {
    private Integer[] cards;

    public AiraUnitAwardDto(EventConfig eventConfig, List<Integer> counts) {
        super(eventConfig);
        cards = counts.toArray(new Integer[0]);
    }
}
