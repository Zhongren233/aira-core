package moe.aira.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.aira.core.entity.es.PointRanking;
import moe.aira.core.entity.es.ScoreRanking;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ScoreRankingMapper extends BaseMapper<ScoreRanking> {

}
