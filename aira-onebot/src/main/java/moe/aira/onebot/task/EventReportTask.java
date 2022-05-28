package moe.aira.onebot.task;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotContainer;
import lombok.extern.slf4j.Slf4j;
import moe.aira.config.EventConfig;
import moe.aira.entity.aira.AiraEventPointDto;
import moe.aira.entity.aira.AiraEventScoreDto;
import moe.aira.entity.api.ApiResult;
import moe.aira.enums.EventRank;
import moe.aira.enums.EventStatus;
import moe.aira.onebot.client.AiraEventClient;
import moe.aira.onebot.entity.EventReportDto;
import moe.aira.onebot.manager.IEventConfigManager;
import moe.aira.onebot.service.WeiboService;
import moe.aira.onebot.util.EventReportTaskImageUtil;
import moe.aira.onebot.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@EnableScheduling
public class EventReportTask {
    final
    IEventConfigManager eventConfigManager;
    final
    AiraEventClient eventClient;
    @Autowired
    BotContainer botContainer;
    @Autowired
    WeiboService weiboService;

    public EventReportTask(IEventConfigManager eventConfigManager, AiraEventClient eventClient) {
        this.eventConfigManager = eventConfigManager;
        this.eventClient = eventClient;
    }

    /**
     * 早九点 晚九点执行
     */
    @Scheduled(cron = "0 0 0,9,21 * * *")
    public void report() throws Exception {
        EventConfig eventConfig = eventConfigManager.fetchEventConfig();
        if (eventConfig.getEventStatus() != EventStatus.OPEN) {
            log.info("暂时不是活动时间");
            return;
        }
        CompletableFuture<ApiResult<Map<Integer, Integer>>> countFuture =
                CompletableFuture.supplyAsync(eventClient::countEventPoint).exceptionally(throwable -> {
                    log.error("获取积分计数失败", throwable);
                    return null;
                });
        CompletableFuture<ApiResult<List<AiraEventPointDto>>> pointFuture = CompletableFuture.supplyAsync(() -> eventClient.fetchCurrentRankPoint(Arrays.stream(EventRank.values()).map(EventRank::getRank).toArray(Integer[]::new))).exceptionally(
                throwable -> {
                    log.error("获取积分失败", throwable);
                    return null;
                }
        );
        CompletableFuture<ApiResult<List<AiraEventScoreDto>>> scoreFuture = CompletableFuture.supplyAsync(() -> eventClient.fetchCurrentRankScore(Arrays.stream(EventRank.values()).map(EventRank::getRank).toArray(Integer[]::new))).exceptionally(
                throwable -> {
                    log.error("获取歌曲积分失败", throwable);
                    return null;
                }
        );
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        CompletableFuture.allOf(countFuture, pointFuture, scoreFuture).join();
        EventReportDto eventReportDto = new EventReportDto();
        eventReportDto.setEventId(eventConfig.getEventId());
        eventReportDto.setFormatDate(format);
        eventReportDto.setCountMap(countFuture.join().getData());
        eventReportDto.setEventPoint(pointFuture.join().getData());
        eventReportDto.setEventScore(scoreFuture.join().getData());
        eventReportDto.setEventConfig(eventConfig);
        BufferedImage bufferedImage = EventReportTaskImageUtil.generateImage(eventReportDto);
        Bot bot1 = botContainer.robots.get(938364861L);
        File output = new File("./" + System.currentTimeMillis() + ".png");
        ImageIO.write(bufferedImage, "png", output);
        String jpg = ImageUtil.bufferImageToBase64(ImageUtil.bufferedImageToJpg(bufferedImage, 0.8), "jpg");
        if (bot1 != null) {
            bot1.sendGroupMsg(717813233L,
                    MsgUtils.builder().text("插播一条活动报告").img(jpg).build(),
                    false);
            log.info("发送成功");
        }
        Bot bot2 = botContainer.robots.get(981363330L);
        if (bot2 != null) {
            bot2.sendGroupMsg(797824188L,
                    MsgUtils.builder().text("插播一条活动报告").img(jpg).build(),
                    false);
            log.info("发送成功");
        }
        LocalDateTime localDate = LocalDateTime.ofInstant(eventConfig.getStartTime().toInstant(), ZoneId.systemDefault());
        LocalDateTime now = LocalDateTime.now();
        if (now.getHour() == 0) {
            long l = Duration.between(now, localDate).toDays();
            weiboService.sendWeibo(eventConfig.getEventCnName() + "Day 0" + (Math.abs(l) + 1), output);
        }
    }
}
