package moe.aira.onebot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import moe.aira.enums.CenterSkill;
import moe.aira.enums.ColorType;
import moe.aira.enums.EvolutionType;

import java.util.List;

@Data
@TableName("aira_card_value_view")
public class AiraCardValueView {
    private Integer cardId;
    private String idolName;
    private String cardNameJp;
    private ColorType cardColor;
    private Integer daValue;
    private Integer voValue;
    private Integer pfValue;
    private EvolutionType evolutionType;
    @TableField(exist = false)
    private Integer gainValue = 0;

    public void addCenterGain(ColorType colorType, CenterSkill centerSkill) {
        if (colorType.equals(this.cardColor)) {
            switch (centerSkill) {
                case DA -> gainValue += (int) (daValue * 1.2);
                case VO -> gainValue += (int) (voValue * 1.2);
                case PF -> gainValue += (int) (pfValue * 1.2);
                case ALL -> gainValue += (int) (calcBaseValue() * 0.5);
            }
        }
    }

    public void addOriginalGain(ColorType colorType, List<String> originalIdol) {
        if (colorType == ColorType.ALL) {
            gainValue += (int) (calcBaseValue() * 0.1);
            return;
        }
        switch (originalIdol.size()) {
            case 1 -> gainValue += (int) (calcBaseValue());
            case 2 -> gainValue += (int) (calcBaseValue() * 0.55);
            case 3 -> gainValue += (int) (calcBaseValue() * 0.4);
            case 4 -> gainValue += (int) (calcBaseValue() * 0.35);
            case 5 -> gainValue += (int) (calcBaseValue() * 0.3);
        }
    }

    public void addSongColorGain(ColorType colorType) {
        if (colorType == ColorType.ALL) {
            gainValue += (int) (calcBaseValue() * 0.3);
            return;
        }
        if (colorType.equals(this.cardColor)) {
            gainValue += (int) (calcBaseValue() * 0.5);
        }
    }

    private Integer calcBaseValue() {
        return daValue + voValue + pfValue;
    }
}
