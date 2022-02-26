package moe.aira.core.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import moe.aira.config.EventConfig;
import moe.aira.core.dao.AiraConfigMapper;
import moe.aira.core.entity.aira.AiraConfig;
import moe.aira.core.manager.IEventConfigManager;
import moe.aira.enums.EventStatus;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
public class IEventConfigManagerImpl implements IEventConfigManager {
    final
    AiraConfigMapper configMapper;

    public IEventConfigManagerImpl(AiraConfigMapper configMapper) {
        this.configMapper = configMapper;
    }

    @Override
    @Cacheable("fetchEventConfig")
    public EventConfig fetchEventConfig() {
        QueryWrapper<AiraConfig> statusQuery = new QueryWrapper<>();
        statusQuery.eq("config_key", "CURRENT_EVENT_STATUS");
        AiraConfig statusConfig = configMapper.selectOne(statusQuery);
        Objects.requireNonNull(statusConfig);
        EventConfig eventConfig = new EventConfig();
        eventConfig.setEventStatus(EventStatus.valueOf(statusConfig.getConfigValue()));
        QueryWrapper<AiraConfig> idQuery = new QueryWrapper<>();
        idQuery.eq("config_key", "CURRENT_EVENT_ID");
        AiraConfig idConfig = configMapper.selectOne(idQuery);
        Objects.requireNonNull(idConfig);
        Integer eventId = Integer.valueOf(idConfig.getConfigValue());
        eventConfig.setEventId(eventId);
        return eventConfig;
    }

    @CacheEvict("fetchEventConfig")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEventConfig(EventConfig eventConfig) {
        String eventStatus = eventConfig.getEventStatus().toString();
        UpdateWrapper<AiraConfig> statusUpdate = new UpdateWrapper<>();
        statusUpdate.eq("config_key", "CURRENT_EVENT_STATUS");
        configMapper.update(new AiraConfig().setConfigValue(eventStatus), statusUpdate);
        Integer eventId = eventConfig.getEventId();
        if (eventId != null) {
            UpdateWrapper<AiraConfig> idUpdate = new UpdateWrapper<>();
            idUpdate.eq("config_key", "CURRENT_EVENT_ID");
            configMapper.update(new AiraConfig().setConfigValue(eventId.toString()), idUpdate);
        }
    }
}
