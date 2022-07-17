package moe.aira.onebot.plugin;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mikuac.shiro.dto.event.message.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import moe.aira.config.EventConfig;
import moe.aira.onebot.client.AiraEventClient;
import moe.aira.onebot.task.EventReportTask;
import moe.aira.onebot.util.AiraBotPlugin;
import moe.aira.onebot.util.AiraContext;
import moe.aira.onebot.util.ImageUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class EventAwardPlugin extends AiraBotPlugin {
    final
    AiraEventClient eventClient;
    @Autowired
    EventReportTask eventReportTask;
    @Autowired
    StringRedisTemplate redisTemplate;

    public EventAwardPlugin(AiraEventClient eventClient) {
        this.eventClient = eventClient;
    }

    private static void handleMessage(Bot bot, MessageEvent event, MsgUtils msgUtils) {
        boolean group = event instanceof GroupMessageEvent;
        if (group) {
            bot.sendGroupMsg(((GroupMessageEvent) event).getGroupId(), MsgUtils.builder().at(event.getUserId()).text(msgUtils.build()).build(), false);
        } else {
            bot.sendPrivateMsg(event.getUserId(), msgUtils.build(), false);

        }
    }

    @Override
    public boolean checkMessage(@NotNull Bot bot, @NotNull MessageEvent event) {
        return event.getMessage().replaceFirst("！", "!").startsWith("!event");
    }

    @Override
    public Runnable doCommand(Bot bot, MessageEvent event) {
        EventConfig eventConfig = AiraContext.getEventConfig();
        return () -> {
            if (!eventConfig.checkAvailable()) {
                handleMessage(bot, event, MsgUtils.builder().text("当前非活动状态"));
                return;
            }
            try {
                MsgUtils builder = MsgUtils.builder();
                String eventReportTmp = redisTemplate.opsForValue().get("eventReportTmp");
                if (eventReportTmp == null) {
                    BufferedImage bufferedImage = eventReportTask.getBufferedImage(eventConfig);
                    Instant instant = eventConfig.getStartTime().toInstant();
                    LocalDateTime startTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
                    LocalDateTime endTime = startTime.plusDays(8).plusHours(10);
                    builder.text("当前活动:" + eventConfig.getEventCnName()).text("\n");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
                    builder.text("活动时间:" + formatter.format(startTime) + " - " + formatter.format(endTime)).text("\n");
                    LocalDateTime now = LocalDateTime.now();
                    Duration between = Duration.between(now, endTime);
                    if (between.getSeconds() > 0) {
                        builder.text("距离活动结束还有:\n");
                        if (between.toDays() != 0) {
                            builder.text(between.toDays() + "天" + between.toHours() % 24 + "时" + between.toMinutes() % 60 + "分");
                        } else {
                            long l = between.toHours();
                            if (l != 0) {
                                builder.text(l + "时" + between.toMinutes() % 60 + "分");
                            } else {
                                builder.text(between.toMinutes() + "分");
                            }
                        }
                    }
                    assert bufferedImage != null;
                    String jpg = ImageUtil.bufferImageToBase64(ImageUtil.bufferedImageToJpg(bufferedImage, 0.8), "jpg");
                    handleMessage(bot, event, builder.img(jpg));
                    redisTemplate.opsForValue().set("eventReportTmp", builder.build(), Duration.ofSeconds(30));
                } else {
                    handleMessage(bot, event, MsgUtils.builder().text(eventReportTmp));

                }

            } catch (IOException e) {
                handleMessage(bot, event, MsgUtils.builder().text("未预料的错误:" + e.getMessage()));
            }

        };
    }
}
