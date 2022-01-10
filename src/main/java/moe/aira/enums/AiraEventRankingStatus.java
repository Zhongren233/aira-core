package moe.aira.enums;

public enum AiraEventRankingStatus {
    NO_DATA(0),
    NOT_REALTIME_POINT_RANKING(1),
    NOT_REALTIME_SCORE_RANKING(2),
    REALTIME_DATA(3);
    private final Integer value;

    AiraEventRankingStatus(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
