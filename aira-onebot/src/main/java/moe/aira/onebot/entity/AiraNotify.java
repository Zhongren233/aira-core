package moe.aira.onebot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("aira_notify")
public class AiraNotify {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String notifyChannel;
    private String notifyTitle;
    private String notifyContent;
    private Date startTime;
    private Boolean sent;
}
