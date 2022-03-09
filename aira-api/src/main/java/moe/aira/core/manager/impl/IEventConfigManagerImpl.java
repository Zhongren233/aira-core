package moe.aira.core.manager.impl;

import lombok.extern.slf4j.Slf4j;
import moe.aira.config.EventConfig;
import moe.aira.core.dao.AiraConfigMapper;
import moe.aira.core.manager.IEventConfigManager;
import moe.aira.enums.EventStatus;
import moe.aira.enums.EventType;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

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

    @CacheEvict("fetchEventConfig")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEventConfig(EventConfig eventConfig) {
        log.info("更新EventConfig:{}", eventConfig);
        Objects.requireNonNull(eventConfig);
        if (eventConfig.getEventStatus() != null) {
            String eventStatus = eventConfig.getEventStatus().toString();
            configMapper.updateConfigValueByConfigKey("CURRENT_EVENT_STATUS", eventStatus);
        }
        if (eventConfig.getEventId() != null) {
            Integer eventId = eventConfig.getEventId();
            configMapper.updateConfigValueByConfigKey("CURRENT_EVENT_ID", eventId.toString());
        }
        if (eventConfig.getEventType() != null) {
            EventType eventType = eventConfig.getEventType();
            configMapper.updateConfigValueByConfigKey("CURRENT_EVENT_TYPE", eventType.toString());
        }


    }
}
