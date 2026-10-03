package moe.aira.onebot.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BmuAwardStats {

    private List<Award> awards;

    @Data
    public static class Award {
        @JsonProperty("card_id")
        private Integer cardId;
        private Integer point;
        private Integer count;
    }
}
