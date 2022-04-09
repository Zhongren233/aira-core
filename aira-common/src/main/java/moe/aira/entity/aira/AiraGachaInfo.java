package moe.aira.entity.aira;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AiraGachaInfo {
    private Integer gachaId;
    private Map<String, List<Integer>> cards;

    private Map<String, Integer> pickedMap;
    private Map<String, Integer> fixedMap;
}
