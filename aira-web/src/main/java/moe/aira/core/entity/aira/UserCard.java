package moe.aira.core.entity.aira;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("aira_user_card")
public class UserCard {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Long qqNumber;

    private String cardId;

    private String cardNumber;

    private String createTime;

    private String updateTime;
}
