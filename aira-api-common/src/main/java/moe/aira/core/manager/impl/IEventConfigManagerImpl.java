package moe.aira.core.manager.impl;

import lombok.extern.slf4j.Slf4j;
import moe.aira.config.EventConfig;
import moe.aira.core.client.es.EventsClient;
import moe.aira.core.dao.AiraConfigMapper;
import moe.aira.core.manager.IEventConfigManager;
import moe.aira.entity.aira.AiraEventAward;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class IEventConfigManagerImpl implements IEventConfigManager {
    final
    AiraConfigMapper configMapper;
    final
    EventsClient eventsClient;

    public IEventConfigManagerImpl(AiraConfigMapper configMapper, EventsClient eventsClient) {
        this.configMapper = configMapper;
        this.eventsClient = eventsClient;
    }

    @Override
    @Cacheable(value = "fetchEventConfig", key = "'1'")
    public EventConfig fetchEventConfig() {
        EventConfig eventConfig = configMapper.selectCurrentEventConfig();
        if (eventConfig.getEventId() != null) {
            List<AiraEventAward> eventAwards = configMapper.findAiraEventAwardByEventId(eventConfig.getEventId());
            eventConfig.setEventAwards(eventAwards);
        }
        return eventConfig;
    }

    @CacheEvict(value = "fetchEventConfig", key = "'1'")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public EventConfig updateEventConfig(EventConfig eventConfig) {
        log.info("更新EventConfig:{}", eventConfig);
        Objects.requireNonNull(eventConfig);
//        if (eventConfig.getEventStatus() == EventStatus.OPEN) {
//            int eventId = eventsClient.index().get("event_id").intValue();
//            eventConfig.setEventId(eventId);
//        }
        if (eventConfig.getEventStatus() != null) {
            String eventStatus = eventConfig.getEventStatus().toString();
            configMapper.updateConfigValueByConfigKey("CURRENT_EVENT_STATUS", eventStatus);
        }
        if (eventConfig.getEventId() != null) {
            Integer eventId = eventConfig.getEventId();
            configMapper.updateConfigValueByConfigKey("CURRENT_EVENT_ID", eventId.toString());
        }
        return fetchEventConfig();

    }
}
