package moe.aira.core.manager.impl;

import moe.aira.config.EventConfig;
import moe.aira.core.manager.IEventConfigManager;
import moe.aira.enums.EventStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IEventConfigManagerImplTest {
    @Autowired
    IEventConfigManager eventConfigManager;

    @Test
    void updateEventConfig() {
        eventConfigManager.updateEventConfig(new EventConfig().setEventStatus(EventStatus.ANNOUNCE));
    }
}