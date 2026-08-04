package moe.aira.onebot.entity;

import lombok.Data;

@Data
public class MonthCardRewardResponse {
    private Result result;

    @Data
    public static class Result {
        private String msg;
        private Integer state;
    }



}
