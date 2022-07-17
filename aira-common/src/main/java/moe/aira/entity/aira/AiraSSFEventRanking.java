package moe.aira.entity.aira;

import lombok.Data;
import moe.aira.entity.es.PointRanking;
import moe.aira.entity.es.ScoreRanking;
import moe.aira.entity.es.UserProfile;
import moe.aira.enums.AiraEventRankingStatus;

import java.util.Date;

@Data
public class AiraSSFEventRanking {
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 活动ID
     */
    private Integer eventId;
    private PointRanking pointRanking;
    private ScoreRanking redScoreRanking;
    private ScoreRanking whiteScoreRanking;
    private UserProfile userProfile;

    /**
     * 数据状态
     *
     * @see AiraEventRankingStatus
     */
    private AiraEventRankingStatus status;

    private Date pointUpdateTime;
    private Date scoreUpdateTime;
}

