package moe.aira.onebot.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import moe.aira.onebot.entity.AiraUser;
import moe.aira.onebot.mapper.AiraUserMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class IAiraRegistryManagerImpl implements IAiraUserManager {
    final
    AiraUserMapper airaUserMapper;

    public IAiraRegistryManagerImpl(AiraUserMapper airaUserMapper) {
        this.airaUserMapper = airaUserMapper;
    }

    @Cacheable(value = "findAiraUser", key = "#p0")
    @Override
    public AiraUser findAiraUser(Long qqNumber) {
        QueryWrapper<AiraUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("qq_number", qqNumber);
        AiraUser airaUser = airaUserMapper.selectOne(queryWrapper);
        if (airaUser != null) {
            return airaUser;
        }
        AiraUser register = new AiraUser();
        register.setQqNumber(qqNumber);
        register.setPermLevel(1);
        register.setUserId(0);
        airaUserMapper.insert(register);
        return register;
    }

    @CacheEvict(value = "findAiraUser", key = "#p0.qqNumber")
    @Override
    public int updateAiraUser(AiraUser airaUser) {
        UpdateWrapper<AiraUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("qq_number", airaUser.getQqNumber());
        return airaUserMapper.update(airaUser, updateWrapper);
    }


    @CacheEvict(value = "findAiraUser", key = "#p0")
    @Override
    public Boolean cleanCache(Long qqNumber) {
        return Boolean.TRUE;
    }
}
