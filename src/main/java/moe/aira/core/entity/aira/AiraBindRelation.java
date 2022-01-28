package moe.aira.core.entity.aira;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class AiraBindRelation {
    @TableId
    private Integer id;

    private Long qqNumber;
    private Integer userId;
    private Boolean ban;
    private Date createTime;
    private Date updateTime;
}
