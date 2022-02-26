package moe.aira.core.entity.dto;

import lombok.Data;
import moe.aira.core.entity.es.EventRanking;
import moe.aira.core.entity.es.UserProfile;
import moe.aira.enums.AiraEventRankingStatus;

@Data
public class UserRanking<T extends EventRanking> {
    private T ranking;

    private UserProfile profile;

    private AiraEventRankingStatus status;

    private Integer userId;
}
