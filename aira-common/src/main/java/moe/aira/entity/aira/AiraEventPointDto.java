package moe.aira.entity.aira;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AiraEventPointDto {
    private Integer rank;
    private Integer point;
    private Integer userId;
}
