package moe.aira.onebot.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AiraGachaResultDto {
    private Long userId;
    private Integer gachaPoolId;
    //Entry是真的好用
    private List<Map.Entry<String, Integer>> cardIds;

    //用来确认渲染图
    private ResultType type;

    //单纯的一个枚举
    public enum ResultType {
        NORMAL, GOLDEN, RAINBOW
    }

}
