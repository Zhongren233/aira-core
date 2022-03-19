package moe.aira.onebot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.aira.onebot.entity.AiraConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AiraConfigMapper extends BaseMapper<AiraConfig> {
    @Select("select config_value from aira_config where config_key = #{configKey}")
    String selectConfigValueByConfigKey(@Param("configKey") String configKey);

    @Update("update aira_config set config_value = #{configValue} where config_key=#{configKey}")
    Integer updateConfigValueByConfigKey(@Param("configKey") String configKey, @Param("configValue") String configValue);
}
