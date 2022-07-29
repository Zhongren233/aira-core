package moe.aira.onebot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.aira.onebot.entity.AiraCardSppDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CardMapper extends BaseMapper<AiraCardSppDto> {

}
