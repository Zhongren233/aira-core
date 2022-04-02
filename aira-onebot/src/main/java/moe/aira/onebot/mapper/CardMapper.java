package moe.aira.onebot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.aira.onebot.entity.AiraCardSppDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CardMapper extends BaseMapper<AiraCardSppDto> {

    List<AiraCardSppDto> selectAiraCardSppDtoList();
}
