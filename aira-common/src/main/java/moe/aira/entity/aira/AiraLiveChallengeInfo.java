package moe.aira.entity.aira;

import lombok.Data;

import java.util.Map;

@Data
public class AiraLiveChallengeInfo {

    private AiraLiveChallengeDetail platRank;
    private AiraLiveChallengeDetail goldRank;
    private AiraLiveChallengeDetail sliverRank;
    private AiraLiveChallengeDetail bronzeRank;

    public String toString() {
        String str = "当期LiveChallenge\n";
        if (platRank != null) {
            str += "♛白金段位♛\n" + platRank;
        }
        if (goldRank != null) {
            str += "\n♚黄金段位♚\n" + goldRank;
        }
        if (sliverRank != null) {
            str += "\n白银段位:\n" + sliverRank;
        }
        if (bronzeRank != null) {
            str += "\n青铜段位:\n" + bronzeRank;
        }
        return str;
    }

    @Data
    public static class AiraLiveChallengeDetail {
        private Integer rank;
        private Integer point;
        private Integer lostPoint;

        public AiraLiveChallengeDetail(Map.Entry<Integer, Integer> entry, Integer maxPoint) {
            if (entry != null) {
                this.rank = entry.getKey();
                this.point = entry.getValue();
                this.lostPoint = (int) ((1.0 - (entry.getValue() / 100000.0)) * maxPoint);
            }
        }

        public AiraLiveChallengeDetail() {
        }

        @Override
        public String toString() {
            return
                    "\t排名:" + rank +
                            "\n\t达成率:" + point +
                            "\n\t失分:" + lostPoint;
        }
    }

}
