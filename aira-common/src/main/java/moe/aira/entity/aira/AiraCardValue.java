package moe.aira.entity.aira;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import moe.aira.enums.EvolutionType;

@Data
@TableName("aira_card_value")
public class AiraCardValue {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer cardId;

    private Integer daValue;
    private Integer voValue;
    private Integer pfValue;

    private EvolutionType evolutionType;

}
