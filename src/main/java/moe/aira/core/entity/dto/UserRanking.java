package moe.aira.core.entity.dto;

import lombok.Data;
import moe.aira.core.entity.es.EventRanking;
import moe.aira.core.entity.es.UserProfile;

@Data
public class UserRanking<T extends EventRanking> {
    T ranking;

    UserProfile profile;
}
