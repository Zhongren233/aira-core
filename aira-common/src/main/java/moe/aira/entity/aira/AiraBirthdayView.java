package moe.aira.entity.aira;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("aira_birthday_view")
public class AiraBirthdayView {

    @TableId("id")
    private String id;

    @TableField("character_name_jp")
    private String characterNameJp;

    @TableField("character_name_cn")
    private String characterNameCn;

    @TableField("unit_name")
    private String unitName;

    @TableField("birth_month")
    private Integer birthMonth;

    @TableField("birth_day")
    private Integer birthDay;

    @TableField("next_birthday")
    private Date nextBirthday;

    @TableField("next_birthday_countdown")
    private Integer nextBirthdayCountdown;
}
