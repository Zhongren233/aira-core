package moe.aira.onebot.entity;

import lombok.Data;
import moe.aira.entity.aira.Card;

import java.util.List;

@Data
public class AiraGachaResultDto {
    private Integer userId;
    private Integer gachaPoolId;
    private List<Card> cards;
    private ResultType type;

    public enum ResultType {
        NORMAL, GOLDEN, RAINBOW
    }

}
