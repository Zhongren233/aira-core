package moe.aira.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.aira.core.entity.aira.Card;
import moe.aira.core.entity.es.PointRanking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CardMapper extends BaseMapper<Card> {
}
