package moe.aira.core.entity.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class GachaPool {
    Map<String, List<String>> pickupTagIdCardBallsMap;
    Map<String, List<String>> fixedPickupTagIdCardBallsMap;

    public GachaPool() {
        pickupTagIdCardBallsMap = new HashMap<>();
        fixedPickupTagIdCardBallsMap = new HashMap<>();
    }
}
