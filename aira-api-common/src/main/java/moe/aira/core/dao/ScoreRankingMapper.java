package moe.aira.core.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.aira.entity.es.ScoreRanking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScoreRankingMapper extends BaseMapper<ScoreRanking> {
    int upsertScoreRankings(@Param("scoreRankings") List<ScoreRanking> scoreRankings);

    int upsertSSScoreRankings(@Param("scoreRankings") List<ScoreRanking> scoreRankings);

}
