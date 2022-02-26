package moe.aira.core.entity.vo;

import lombok.Data;
import moe.aira.core.entity.es.Award;

import java.util.List;

@Data
public class UserEventStatVo {
    private Integer userId;
    private String userName;
    private Integer avatarId;
    private Boolean avatarEvolved;
    private String avatarCardName;
    private List<Award> awards;
    private Integer point;
    private Integer pointRank;
    private Integer score;
    private Integer scoreRank;
}
