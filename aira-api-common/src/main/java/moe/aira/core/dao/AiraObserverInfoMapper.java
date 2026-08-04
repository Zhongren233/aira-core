package moe.aira.core.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.aira.entity.aira.AiraLogScore;
import moe.aira.entity.aira.AiraObserverInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AiraObserverInfoMapper extends BaseMapper<AiraObserverInfo> {
    @Select("""
            select user_id
            from es_point_ranking
            where user_id in (select user_id
                              from aira_observer_info
                              where expired_time > now())""")
    List<Integer> selectObserverUser();
}
