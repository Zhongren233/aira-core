package moe.aira.onebot.manager.impl;

import lombok.extern.slf4j.Slf4j;
import moe.aira.config.EventConfig;
import moe.aira.entity.aira.AiraEventAward;
import moe.aira.onebot.manager.IEventConfigManager;
import moe.aira.onebot.mapper.AiraConfigMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class IEventConfigManagerImpl implements IEventConfigManager {
    final
    AiraConfigMapper configMapper;

    public IEventConfigManagerImpl(AiraConfigMapper configMapper) {
        this.configMapper = configMapper;
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
}
