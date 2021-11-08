package moe.aira.core.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class GachaInfo {
    @JsonProperty("gachaId")
    String gachaId;
    @JsonProperty("pickup_tag_id_ratio_map")
    Map<String, Integer> pickupTagIdRatioMap;
    @JsonProperty("fixed_pickup_tag_id_ratio_map")
    Map<String, Integer> fixPickupTagIdRatioMap;
}
