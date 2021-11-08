package moe.aira.core.entity.aira;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("aira_gacha_result")
public class GachaResult {
    private Long qqNumber;
    private Integer cardId;
    private Byte cardNumber;
    private Byte rarity;
    private String cardNameCn;
    private String cardNameJp;
    private String idolName;
    @TableField(exist = false)
    private Boolean newCard;

    public void setCardNumber(Byte cardNumber) {
        this.cardNumber = cardNumber;
        if (cardNumber == 1) {
            newCard = Boolean.TRUE;
        } else {
            newCard = Boolean.FALSE;
        }
    }

    public String toViewString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte i = 0; i < rarity; i++) {
            stringBuilder.append("★");
        }
        stringBuilder.append(" ");
        /*if (newCard) {
            stringBuilder.append("(新获得) ");
        } else {
            stringBuilder.append("(第").append(cardNumber).append("张)");
        }*/
        stringBuilder.append("[").append(cardNameCn).append("]");
        stringBuilder.append(" ");
        stringBuilder.append(idolName);
        return stringBuilder.toString();
    }
}
