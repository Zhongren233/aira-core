package moe.aira.api.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import moe.aira.config.EventConfig;
import moe.aira.core.client.es.MyPageClient;
import moe.aira.core.manager.IEventConfigManager;
import moe.aira.enums.EventStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
public class AiraEventTask {

    final
    MyPageClient client;
    final
    IEventConfigManager eventConfigManager;

    public AiraEventTask(MyPageClient client, IEventConfigManager eventConfigManager) {
        this.client = client;
        this.eventConfigManager = eventConfigManager;
    }

    @Scheduled(cron = "0 0 12 * * THU ")
    public void updateEventId() {
        log.info("开始更新活动状态");
        EventConfig eventConfig = new EventConfig();
        JsonNode value = client.myPage().get("my_page_banners").findValue("EventId");
        if (value != null) {
            eventConfig.setEventId(value.asInt());
            log.info("更新活动状态:{}", value.asInt());
        }
        eventConfigManager.updateEventConfig(eventConfig);
        log.info("更新活动状态完成");
    }

    @Scheduled(cron = "0 0 12 * * SAT")
    public void openEvent() {
        log.info("开始更新活动状态");
        eventConfigManager.updateEventConfig(new EventConfig().setEventStatus(EventStatus.OPEN));
        log.info("更新活动状态完成");
    }

    @Scheduled(cron = "0 0 22 * * MON")
    public void countingEndEvent() {
        EventConfig eventConfig = eventConfigManager.fetchEventConfig();
        if (eventConfig.getEventStatus() == EventStatus.END) {
            log.info("开始更新活动状态");
            eventConfig.setEventStatus(EventStatus.COUNTING_END);
            eventConfigManager.updateEventConfig(eventConfig);
        }
    }


}
