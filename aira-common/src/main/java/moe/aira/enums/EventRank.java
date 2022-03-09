package moe.aira.enums;

public enum EventRank {
    R1(1), R2(2), R3(3), R10(10), R100(100), R1000(1000), R5000(5000), R10000(10000), R50000(50000);
    private final Integer rank;

    EventRank(Integer rank) {
        this.rank = rank;
    }


    public Integer getRank() {
        return rank;
    }
}
