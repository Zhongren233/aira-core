package moe.aira.core.biz;

import java.util.Map;

public interface IAiraEventBiz {
    Map<Integer, Integer> countEventPointBatch();

    Map<Integer, Integer> countEventPointBatch(Integer... points);

    Map<Integer, Integer> fetchCurrentRankPoint();

    Map<Integer, Integer> fetchCurrentRankPoint(Integer... ranks);
}
