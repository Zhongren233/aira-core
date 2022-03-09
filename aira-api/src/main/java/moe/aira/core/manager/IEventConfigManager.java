package moe.aira.core.manager;

import moe.aira.config.EventConfig;

public interface IEventConfigManager {

    EventConfig fetchEventConfig();

    void updateEventConfig(EventConfig eventConfig);
}
