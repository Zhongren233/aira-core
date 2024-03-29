package moe.aira.util;

public class RankPageCalculator {

    private RankPageCalculator() {

    }

    public static int calcPage(int rank) {
        int page = 1;
        if (rank > 19)
            page = rank / 20 + 1;
        return page;
    }

    public static int calcIndex(int rank) {
        int index;
        if (rank <= 19) {
            index = rank - 1;
        } else {
            index = rank % 20;
        }
        return index;
    }
}
