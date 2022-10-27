package moe.aira.entity.es;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("es_user_profile")
public class UserProfile {
    @TableField("event_id")
    private Integer eventId;

    @TableId("user_id")
    private Integer userId;
    @TableField("user_name")
    private String userName;
    @TableField("user_favorite_card_id")
    private Integer userFavoriteCardId;
    @TableField("user_favorite_card_evolved")
    private Boolean userFavoriteCardEvolved;
    @TableField("user_award1_id")
    private Integer userAward1Id;
    @TableField("user_award1_value")
    private Integer userAward1Value;
    @TableField("user_award2_id")
    private Integer userAward2Id;
    @TableField("user_award2_value")
    private Integer userAward2Value;
    @TableField("location")
    private String location;

}
