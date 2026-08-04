package moe.aira.onebot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class SkylandSignLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long qqNumber;
    private Date signDate;
    private Integer success;
    private String result;
    private Date createTime;
}
