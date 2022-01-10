package moe.aira.core.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.aira.core.entity.es.PointRanking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PointRankingMapper extends BaseMapper<PointRanking> {
    int upsertPointRanking(@Param("pointRankings") List<PointRanking> pointRankings);
}
