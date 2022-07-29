package moe.aira.entity.aira;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("aira_log_score")
public class AiraLogScore {
    @TableId(type = IdType.AUTO)
    private Integer logId;
    private Integer eventId;
    private Integer logRank;
    private Integer logScore;
    private Integer userId;
    private Date createTime;
    private Integer colorTypeId;
}
