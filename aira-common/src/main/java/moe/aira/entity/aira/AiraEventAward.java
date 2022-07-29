package moe.aira.entity.aira;

import lombok.Data;

@Data
public class AiraEventAward {

    private Integer id;
    private Integer eventId;
    private Integer cardId;
    private String cardNameCn;
    private String cardNameJp;
    private Integer rarity;
    private Integer cardSort;

}

