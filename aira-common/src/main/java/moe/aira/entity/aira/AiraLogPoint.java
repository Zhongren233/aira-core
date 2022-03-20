package moe.aira.entity.aira;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("aira_log_point")
public class AiraLogPoint {

    private Integer logId;
    private Integer eventId;
    private Integer logRank;
    private Integer logPoint;
    private Integer userId;
    private Date createTime;
}
