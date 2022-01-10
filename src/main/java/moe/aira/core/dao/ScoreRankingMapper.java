package moe.aira.core.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.aira.core.entity.es.PointRanking;
import moe.aira.core.entity.es.ScoreRanking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScoreRankingMapper extends BaseMapper<ScoreRanking> {
    int upsertScoreRankings(@Param("scoreRankings") List<ScoreRanking> scoreRankings);

}
