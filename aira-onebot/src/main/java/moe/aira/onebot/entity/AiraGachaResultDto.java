package moe.aira.onebot.entity;

import lombok.Data;
import moe.aira.entity.aira.Card;

import java.util.List;
import java.util.Map;

@Data
public class AiraGachaResultDto {
    private Integer userId;
    private Integer gachaPoolId;
    private List<Card> cards;
    private List<Map.Entry<String, Integer>> cardIds;

    private ResultType type;

    public enum ResultType {
        NORMAL, GOLDEN, RAINBOW
    }

}
