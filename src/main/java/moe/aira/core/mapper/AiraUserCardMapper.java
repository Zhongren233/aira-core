package moe.aira.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.aira.core.entity.aira.GachaRecord;
import moe.aira.core.entity.aira.UserCard;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AiraUserCardMapper extends BaseMapper<UserCard> {
    void insertOrUpdateUserCardByGachaRecord(GachaRecord gachaRecord);
}
