package moe.aira.core.biz;

import java.util.Map;

public interface IAiraEventBiz {
    Map<Integer, Integer> countEventPointBatch();

    Map<Integer, Integer> countEventPointWhereGt(Integer... points);

}
