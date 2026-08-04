package moe.aira.core.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.aira.entity.aira.AiraObserverDetail;
import moe.aira.entity.aira.AiraObserverInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AiraObserverDetailMapper extends BaseMapper<AiraObserverDetail> {
    @Select("""
            select min(id) as id ,aira_observer_detail.user_id,ob_event_point,min(ob_time) as ob_time
            from aira_observer_detail where user_id = #{userId} and event_id = #{eventId} group by ob_event_point;
            """)
    List<AiraObserverDetail> selectByUserIdAndEventId(@Param("userId") Integer userId, @Param("eventId") Integer eventId);

}

