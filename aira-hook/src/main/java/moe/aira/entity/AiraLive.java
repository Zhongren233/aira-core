package moe.aira.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("aira_live")
public class AiraLive {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer musicId;
    private Integer userId;
    private Integer bp;
    private Integer supportCard1Id;
    private Integer supportCard1Skill;
    private Integer supportCard2Id;
    private Integer supportCard2Skill;

    private Integer card1Id;
    private Integer card2Id;
    private Integer card3Id;
    private Integer card4Id;
    private Integer card5Id;

    private Integer sppPosition;
}
