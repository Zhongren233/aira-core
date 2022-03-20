package moe.aira.entity.aira;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("aira_log_score")
public class AiraLogScore {

    private Integer logId;
    private Integer eventId;
    private Integer logRank;
    private Integer logScore;
    private Integer userId;
    private Date createTime;
}
