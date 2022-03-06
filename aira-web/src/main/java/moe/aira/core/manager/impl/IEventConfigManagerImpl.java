package moe.aira.core.manager.impl;

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
        String currentEventStatus = configMapper.selectConfigValueByConfigKey("CURRENT_EVENT_STATUS");
        EventConfig eventConfig = new EventConfig();
        eventConfig.setEventStatus(EventStatus.valueOf(currentEventStatus));
        String currentEventId = configMapper.selectConfigValueByConfigKey("CURRENT_EVENT_ID");
        eventConfig.setEventId(Integer.valueOf(currentEventId));
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
