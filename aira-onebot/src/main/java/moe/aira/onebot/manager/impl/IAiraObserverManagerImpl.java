package moe.aira.onebot.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import moe.aira.entity.aira.AiraObserverDetail;
import moe.aira.entity.aira.AiraObserverInfo;
import moe.aira.onebot.manager.IAiraObserverManager;
import moe.aira.onebot.mapper.AiraObserverDetailMapper;
import moe.aira.onebot.mapper.AiraObserverInfoMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class IAiraObserverManagerImpl implements IAiraObserverManager {
    private final AiraObserverInfoMapper airaObserverInfoMapper;
    private final AiraObserverDetailMapper airaObserverDetailMapper;

    public IAiraObserverManagerImpl(AiraObserverInfoMapper airaObserverInfoMapper, AiraObserverDetailMapper airaObserverDetailMapper) {
        this.airaObserverInfoMapper = airaObserverInfoMapper;
        this.airaObserverDetailMapper = airaObserverDetailMapper;
    }

    @Cacheable(cacheNames = "AiraObserverInfo", key = "#userId")
    @Override
    public AiraObserverInfo getAiraObserverInfo(Integer userId) {
        QueryWrapper<AiraObserverInfo> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("id");
        return airaObserverInfoMapper.selectOne(queryWrapper);
    }

    @CacheEvict(cacheNames = "AiraObserverInfo", key = "#userId")

    @Override
    public AiraObserverInfo regAiraObserver(Integer userId) {
        QueryWrapper<AiraObserverInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("id");
        queryWrapper.last("limit 1");
        AiraObserverInfo airaObserverInfo1 = airaObserverInfoMapper.selectOne(queryWrapper);
        if (airaObserverInfo1 != null) {
            airaObserverInfo1.setExpiredTime(new Date(1749391200000L));
            airaObserverInfoMapper.updateById(airaObserverInfo1);
            return airaObserverInfo1;
        } else {
            AiraObserverInfo airaObserverInfo = new AiraObserverInfo();
            airaObserverInfo.setUserId(userId);
            airaObserverInfo.setExpiredTime(new Date(1749391200000L));
            airaObserverInfoMapper.insert(airaObserverInfo);
            return airaObserverInfo;
        }
    }

    @Override
    public AiraObserverDetail getLastestAiraObserverDetail(Integer userId) {
        QueryWrapper<AiraObserverDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("ob_event_point");
        queryWrapper.orderByAsc("ob_time");
        queryWrapper.last("limit 1");
        return airaObserverDetailMapper.selectOne(queryWrapper);
    }

    @Override
    public List<AiraObserverDetail> getAllAiraObserverDetails(Integer userId, Integer eventId) {
        return List.of();
    }
}
