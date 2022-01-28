package moe.aira.core.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.aira.core.entity.aira.AiraBindRelation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface AiraBindRelationMapper extends BaseMapper<AiraBindRelation> {
}
