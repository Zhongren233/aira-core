package moe.aira.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.aira.core.entity.aira.GachaRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AiraGachaRecordMapper extends BaseMapper<GachaRecord> {
}
