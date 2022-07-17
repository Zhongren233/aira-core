package moe.aira.onebot.entity;

import lombok.Getter;
import lombok.Setter;
import moe.aira.config.EventConfig;

import java.util.Map;

@Getter
@Setter
public class AiraTourAwardDto extends AiraAwardDto {
    private Integer oneFirstCard;
    private Integer oneSecondCard;
    private Integer oneThirdCard;
    private Integer oneFourthCard;
    private Integer oneFifthCard;

    private Integer twoFirstCard;
    private Integer twoSecondCard;
    private Integer twoThirdCard;
    private Integer twoFourthCard;
    private Integer twoFifthCard;


    private Integer[] oneCards;
    private Integer[] twoCards;

    public AiraTourAwardDto(EventConfig eventConfig, Map<Integer, Integer> data) {
        super(eventConfig, data);

        if (eventConfig.getEventId() == 243) {
            if (data.get(3500000) != null) {
                oneFirstCard = data.get(3500000);
                oneSecondCard = data.get(11000000);
                oneThirdCard = data.get(15500000);
                oneFourthCard = data.get(21000000);
                oneFifthCard = data.get(27000000);
                twoFirstCard = data.get(4800000);
                twoSecondCard = data.get(8000000);
                twoThirdCard = data.get(12500000);
                twoFourthCard = data.get(25000000);
                twoFifthCard = data.get(29500000);
            } else {
                oneFirstCard = data.get(4200000);
                oneSecondCard = data.get(7000000);
                oneThirdCard = data.get(17000000);
                oneFourthCard = data.get(23000000);
                oneFifthCard = data.get(29000000);
                twoFirstCard = data.get(5500000);
                twoSecondCard = data.get(9500000);
                twoThirdCard = data.get(14000000);
                twoFourthCard = data.get(19000000);
                twoFifthCard = data.get(30000000);
            }

        } else {
            oneFirstCard = data.get(300 * 10000);
            oneSecondCard = data.get(750 * 10000);
            oneThirdCard = data.get(950 * 10000);
            oneFourthCard = data.get(1500 * 10000);
            oneFifthCard = data.get(2100 * 10000);
            twoFirstCard = data.get(350 * 10000);
            twoSecondCard = data.get(600 * 10000);
            twoThirdCard = data.get(1100 * 10000);
            twoFourthCard = data.get(1350 * 10000);
            twoFifthCard = data.get(2200 * 10000);
        }

        oneCards = new Integer[]{oneFirstCard, oneSecondCard, oneThirdCard, oneFourthCard, oneFifthCard};
        twoCards = new Integer[]{twoFirstCard, twoSecondCard, twoThirdCard, twoFourthCard, twoFifthCard};

    }
}
