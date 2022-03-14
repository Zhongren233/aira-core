package moe.aira.core.entity.aira;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("aira_card")
public class Card {
    @TableId
    private Integer cardId;
    @TableField("rarity")
    private Byte rarity;
    @TableField("card_name_jp")
    private String cardNameJp;
    @TableField("card_name_cn")
    private String cardNameCn;
    @TableField("idol_name")
    private String idolName;
    @TableField("card_color")
    private String cardColor;
    @TableField("card_attr")
    private String cardAttr;
    @TableField("card_pool_name")
    private String cardPoolName;
    @TableField("spp_name")
    private String sppName;

    public String buildAiraReturnText() {
        return "★" + rarity + " " +
                cardColor + " " +
                "[" + cardNameCn + "]" +
                "(" + cardNameJp + ")" +
                " " + idolName +
                "\n" +
                "\t" +
                "SPP:" +
                (sppName != null ? sppName : "未知");

    }
}
