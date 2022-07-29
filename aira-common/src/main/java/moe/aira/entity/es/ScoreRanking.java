package moe.aira.entity.es;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("es_score_ranking")
public class ScoreRanking extends EventRanking {
    @TableField("event_id")
    private Integer eventId;
    @TableField("user_id")
    private Integer userId;
    @TableField("color_type_id")
    private Integer colorTypeId;
    @TableField("event_rank")
    private Integer eventRank;
    @TableField("event_point")
    private Integer eventPoint;
    @TableField("update_time")
    private Date updateTime;
}
