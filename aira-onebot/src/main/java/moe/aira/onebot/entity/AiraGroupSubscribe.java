package moe.aira.onebot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("aira_group_subscribe")
public class AiraGroupSubscribe {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Long groupId;
    private String channelName;
}
