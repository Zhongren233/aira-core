package moe.aira.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface AiraConfigMapper {
    @Select("select config_value from aira_config where config_key = #{key}")
    Optional<String> getConfig(@Param("key") String key);
}
