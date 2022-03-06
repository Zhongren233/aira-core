package moe.aira.core.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.aira.core.entity.aira.AiraConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AiraConfigMapper extends BaseMapper<AiraConfig> {
    @Select("select config_value from aira_config where config_key = #{configKey}")
    String selectConfigValueByConfigKey(@Param("configKey") String configKey);
}
