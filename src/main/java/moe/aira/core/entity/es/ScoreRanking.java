package moe.aira.core.entity.es;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("es_score_ranking")
public class ScoreRanking {
    @TableId
    private Integer id;
    @TableField("event_id")
    private Integer eventId;
    @TableField("user_id")
    private Integer userId;
    @TableField("event_rank")
    private Integer eventRank;
    @TableField("event_point")
    private Integer eventPoint;
}
