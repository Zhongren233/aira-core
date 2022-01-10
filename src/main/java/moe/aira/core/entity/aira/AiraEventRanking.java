package moe.aira.core.entity.aira;

import lombok.Data;
import moe.aira.core.entity.es.PointRanking;
import moe.aira.core.entity.es.ScoreRanking;
import moe.aira.core.entity.es.UserProfile;

@Data
public class AiraEventRanking {
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 活动ID
     */
    private Integer eventId;
    private PointRanking pointRanking;
    private ScoreRanking scoreRanking;
    private UserProfile userProfile;

    /**
     * 数据状态
     *
     * @see moe.aira.enums.AiraEventRankingStatus
     */
    private Integer status;


}

