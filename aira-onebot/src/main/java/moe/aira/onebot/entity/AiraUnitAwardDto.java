package moe.aira.onebot.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import moe.aira.config.EventConfig;

import java.util.Map;


@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class AiraUnitAwardDto extends AiraAwardDto {
    private Integer firstCard;
    private Integer secondCard;
    private Integer thirdCard;
    private Integer fourthCard;
    private Integer fifthCard;

    private Integer[] cards;

    public AiraUnitAwardDto(EventConfig eventConfig, Map<Integer, Integer> data) {
        super(eventConfig, data);
        firstCard = data.get(350 * 10000);
        secondCard = data.get(750 * 10000);
        thirdCard = data.get(1100 * 10000);
        fourthCard = data.get(1500 * 10000);
        fifthCard = data.get(2200 * 10000);
        cards = new Integer[]{firstCard, secondCard, thirdCard, fourthCard, fifthCard};
    }


    public static AiraUnitAwardDto colabo2025(EventConfig eventConfig, Map<Integer, Integer> data) {
        AiraUnitAwardDto airaUnitAwardDto = new AiraUnitAwardDto(eventConfig, data);
        airaUnitAwardDto.firstCard = data.get(350 * 10000);
        airaUnitAwardDto.secondCard = data.get(800 * 10000);
        airaUnitAwardDto.thirdCard = data.get(1250 * 10000);
        airaUnitAwardDto.fourthCard = data.get(1900 * 10000);
        airaUnitAwardDto.fifthCard = data.get(2700 * 10000);
        airaUnitAwardDto.cards = new Integer[]{airaUnitAwardDto.firstCard, airaUnitAwardDto.secondCard, airaUnitAwardDto.thirdCard, airaUnitAwardDto.fourthCard, airaUnitAwardDto.fifthCard};
        return airaUnitAwardDto;
    }
}
