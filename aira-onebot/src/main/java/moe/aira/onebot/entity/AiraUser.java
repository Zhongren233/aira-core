package moe.aira.onebot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class AiraUser {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Long qqNumber;
    private Integer userId;
    private Integer permLevel;
    private Date createTime;
    private Date updateTime;
}
