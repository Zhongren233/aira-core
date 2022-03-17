package moe.aira.core.biz;

import moe.aira.entity.aira.AiraEventPointDto;
import moe.aira.entity.aira.AiraEventScoreDto;

import java.util.List;
import java.util.Map;

public interface IAiraEventBiz {
    Map<Integer, Integer> countEventPointBatch();

    Map<Integer, Integer> countEventPointBatch(Integer... points);

    List<AiraEventPointDto> fetchCurrentRankPoint();

    List<AiraEventPointDto> fetchCurrentRankPoint(Integer... ranks);

    List<AiraEventScoreDto> fetchCurrentRankScore();

    List<AiraEventScoreDto> fetchCurrentRankScore(Integer... ranks);
}
