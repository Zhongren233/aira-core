package moe.aira.core.entity.es;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("es_score_ranking")
public class ScoreRanking extends EventRanking{

    @TableField("event_id")
    private Integer eventId;
    @TableId("user_id")
    private Integer userId;
    @TableField("event_rank")
    private Integer eventRank;
    @TableField("event_point")
    private Integer eventPoint;
}
