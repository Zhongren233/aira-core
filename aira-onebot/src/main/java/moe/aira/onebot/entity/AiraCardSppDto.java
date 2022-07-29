package moe.aira.onebot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import moe.aira.enums.ColorType;

import java.util.Date;

@Data
@TableName("aira_spp_view")
public class AiraCardSppDto {
    @TableId("card_id")
    private String cardId;
    @TableField
    private Integer rarity;
    @TableField("card_name_cn")
    private String cardName;
    private String idolName;
    @TableField("card_color")
    private ColorType cardColorType;
    private String sppName;

    private String songId;
    private String songNameJp;
    private String songNameCn;
    private String unitName;
    @TableField("music_type")
    private ColorType songColorType;

    private Date cnInstallTime;
    private Date jpInstallTime;
}
