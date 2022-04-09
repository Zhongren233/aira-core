package moe.aira.onebot.plugin;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.WholeMessageEvent;
import lombok.extern.slf4j.Slf4j;
import moe.aira.config.EventConfig;
import moe.aira.entity.api.ApiResult;
import moe.aira.exception.AiraException;
import moe.aira.onebot.client.AiraEventClient;
import moe.aira.onebot.util.AiraAwardImageUtil;
import moe.aira.onebot.util.AiraContext;
import moe.aira.onebot.util.ImageUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static moe.aira.onebot.util.AiraSendMessageUtil.sendMessage;

@Slf4j
@Component
public class EventAwardPlugin extends BotPlugin {
    final
    AiraEventClient eventClient;

    public EventAwardPlugin(AiraEventClient eventClient) {
        this.eventClient = eventClient;
    }

    @Override
    public int onWholeMessage(@NotNull Bot bot, @NotNull WholeMessageEvent event) {
        event.setMessage(event.getMessage().replaceFirst("！", "!"));

        if (!event.getMessage().trim().startsWith("!event")) {
            return MESSAGE_IGNORE;
        }
        EventConfig eventConfig = AiraContext.getEventConfig();
        CompletableFuture<MsgUtils> task = CompletableFuture.supplyAsync(() -> {
            ApiResult<Map<Integer, Integer>> apiResult = eventClient.countEventPoint();
            if (apiResult.getCode() != 0) {
                throw new RuntimeException(apiResult.getMessage());
            }
            Map<Integer, Integer> map = apiResult.getData();
            try {
                MsgUtils builder = MsgUtils.builder();
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
                builder.img(ImageUtil.bufferImageToBase64(AiraAwardImageUtil.generateAwardImage(eventConfig, map)));
                return builder;
            } catch (IOException e) {
                throw new AiraException("图片转换失败", e);
            }
        });
        task.thenAcceptAsync(msgUtils -> sendMessage(bot, event, msgUtils));
        try {
            task.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | TimeoutException e) {
            log.error("", e);
        } catch (ExecutionException e) {
            log.error("", e);
            sendMessage(bot, event, MsgUtils.builder().text("未预料的错误:" + e.getMessage()));
        }
        return MESSAGE_BLOCK;
    }
}
