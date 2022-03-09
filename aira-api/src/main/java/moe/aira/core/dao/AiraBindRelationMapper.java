package moe.aira.core.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.aira.core.entity.aira.AiraUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AiraBindRelationMapper extends BaseMapper<AiraUser> {
}
