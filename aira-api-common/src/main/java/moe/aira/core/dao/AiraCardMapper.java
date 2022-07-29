package moe.aira.core.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.aira.entity.aira.Card;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AiraCardMapper extends BaseMapper<Card> {
}
