package moe.aira.entity.aira;

import lombok.Data;

import java.nio.channels.InterruptedByTimeoutException;
import java.util.List;

@Data
public class AiraTriColorMatsuriInfo {

    private Boolean fever;

    private Integer periodId;
    private List<AiraTriColorBattleInfo> battleInfos;

    public enum MatsuriTeam {
        RED(1), BLUE(2), YELLOW(3);
        private final int teamId;

        MatsuriTeam(int teamId) {
            this.teamId = teamId;
        }

        public int getTeamId() {
            return teamId;
        }
    }

    @Data
    public static class AiraTriColorBattleInfo {
        private Integer battleId;
        private Integer teamId;
        private Integer score;

    }
}
