package moe.aira.entity.aira;

import lombok.Data;
import moe.aira.entity.es.PointRanking;
import moe.aira.entity.es.ScoreRanking;
import moe.aira.entity.es.UserProfile;
import moe.aira.enums.AiraEventRankingStatus;

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
     * @see AiraEventRankingStatus
     */
    private AiraEventRankingStatus status;


}

