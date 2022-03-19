package moe.aira.onebot.manager.impl;

import lombok.extern.slf4j.Slf4j;
import moe.aira.config.EventConfig;
import moe.aira.enums.EventStatus;
import moe.aira.enums.EventType;
import moe.aira.onebot.manager.IEventConfigManager;
import moe.aira.onebot.mapper.AiraConfigMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Slf4j
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
        String currentEventType = configMapper.selectConfigValueByConfigKey("CURRENT_EVENT_TYPE");
        eventConfig.setEventType(EventType.valueOf(currentEventType));
        return eventConfig;
    }
}
