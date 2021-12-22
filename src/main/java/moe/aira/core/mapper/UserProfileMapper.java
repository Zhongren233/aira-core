package moe.aira.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.aira.core.entity.es.UserProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserProfileMapper extends BaseMapper<UserProfile> {

    int upsertUserProfile(@Param("userProfiles") List<UserProfile> userProfiles);

}
