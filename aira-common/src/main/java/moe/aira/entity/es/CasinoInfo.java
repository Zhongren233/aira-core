package moe.aira.entity.es;

import lombok.Data;

import java.util.Date;

@Data
public class CasinoInfo {
    private Boolean openJackpot;
    private Integer jackpotPercent;
    private String jackpotEndTime;

    public String formatText() {
        if (openJackpot) {
            return "777Time！\n截止时间：" + jackpotEndTime;
        }else {
            return "当前进度：" + jackpotPercent + "%";
        }
    }
}
