package moe.aira.api.tasks;

import lombok.extern.slf4j.Slf4j;
import moe.aira.core.biz.IAiraEventBiz;
import moe.aira.core.manager.IEventConfigManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@EnableScheduling
public class AiraRecordTask {
    final
    IEventConfigManager eventConfigManager;
    final
    IAiraEventBiz eventBiz;

    public AiraRecordTask(IEventConfigManager eventConfigManager, IAiraEventBiz eventBiz) {
        this.eventConfigManager = eventConfigManager;
        this.eventBiz = eventBiz;
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void task() {
        if (eventConfigManager.fetchEventConfig().checkAvailable()) {
            log.info("开始记录");
            CompletableFuture<Void> future = CompletableFuture.runAsync(this::recordPointRanking);
            CompletableFuture<Void> future1 = CompletableFuture.runAsync(this::recordScoreRanking);
            CompletableFuture<Void> future2 = CompletableFuture.runAsync(this::recordPointAwardCount);
            CompletableFuture.allOf(future, future1, future2).join();
            log.info("记录完成");
        }
    }

    public void recordPointRanking() {

    }


    public void recordScoreRanking() {

    }

    public void recordPointAwardCount() {

    }
}
