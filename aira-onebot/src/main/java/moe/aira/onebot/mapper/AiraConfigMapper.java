package moe.aira.onebot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.aira.config.EventConfig;
import moe.aira.entity.aira.AiraEventAward;
import moe.aira.onebot.entity.AiraConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.EnumTypeHandler;

import java.util.List;

@Mapper
public interface AiraConfigMapper extends BaseMapper<AiraConfig> {
    @Select("select config_value from aira_config where config_key = #{configKey}")
    String selectConfigValueByConfigKey(@Param("configKey") String configKey);

    @Update("update aira_config set config_value = #{configValue} where config_key=#{configKey}")
    Integer updateConfigValueByConfigKey(@Param("configKey") String configKey, @Param("configValue") String configValue);


    @Results(
            id = "airaConfigResult",
            value = {
                    @Result(property = "eventId", column = "event_id"),
                    @Result(property = "eventCnName", column = "event_cn_name"),
                    @Result(property = "eventType", column = "event_type", typeHandler = EnumTypeHandler.class),
                    @Result(property = "eventStatus", column = "event_status", typeHandler = EnumTypeHandler.class),
                    @Result(property = "startTime", column = "start_time"),
            }
    )
    @Select("select A01.event_id, " +
            "A01.event_cn_name," +
            "A01.event_jp_name, " +
            "A01.event_type, " +
            "A01.start_time, " +
            "(select config_value from aira_config where config_key = 'CURRENT_EVENT_STATUS') as  event_status " +
            "from aira_event A01 " +
            "where A01.event_id = (select config_value from aira_config where config_key = 'CURRENT_EVENT_ID')")
    EventConfig selectCurrentEventConfig();

    @Select("select A02.card_id, " +
            "       A02.card_sort, " +
            "       A03.rarity, " +
            "       A03.idol_name, " +
            "       A03.card_name_jp, " +
            "       A03.card_name_cn " +
            "from aira_event_award A02 " +
            "         left join aira_card A03 on A02.card_id = A03.card_id " +
            "where A02.event_id =  #{eventId} ")
    List<AiraEventAward> findAiraEventAwardByEventId(@Param("eventId") Integer eventId);


}
