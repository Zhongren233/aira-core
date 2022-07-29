package moe.aira.entity.aira;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AiraEventScoreDto {
    private Integer rank;
    private Integer score;
    private Integer userId;
}
