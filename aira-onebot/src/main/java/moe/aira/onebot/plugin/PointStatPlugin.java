package moe.aira.onebot.plugin;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.WholeMessageEvent;
import moe.aira.entity.aira.AiraEventPointDto;
import moe.aira.entity.api.ApiResult;
import moe.aira.enums.EventRank;
import moe.aira.onebot.clent.AiraEventClient;
import moe.aira.onebot.util.AiraContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static moe.aira.onebot.util.AiraSendMessageUtil.sendMessage;

@Component
public class PointStatPlugin extends BotPlugin {
    final
    AiraEventClient eventClient;

    public PointStatPlugin(AiraEventClient eventClient) {
        this.eventClient = eventClient;
    }

    @Override
    public int onWholeMessage(@NotNull final Bot bot, @NotNull final WholeMessageEvent event) {
        if (!event.getMessage().startsWith("!point")) {
            return MESSAGE_IGNORE;
        }
        if (!AiraContext.getEventConfig().checkAvailable()) {
            sendMessage(bot, event, MsgUtils.builder().text("功能暂不可用"));
            return MESSAGE_IGNORE;
        }
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            Integer[] ranks = (Integer[]) Arrays.stream(EventRank.values()).map(EventRank::getRank).toArray();
            ApiResult<List<AiraEventPointDto>> listApiResult = eventClient.fetchCurrentRankPoint(ranks);
            if (listApiResult == null || listApiResult.getCode() != 0) {
                sendMessage(bot, event, MsgUtils.builder().text("获取排名积分失败"));
                return;
            }

            List<AiraEventPointDto> data = listApiResult.getData();
            data.sort(Comparator.comparingInt(AiraEventPointDto::getRank));

        });
        try {
            future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            sendMessage(bot, event, MsgUtils.builder().text("正在获取排名积分，请稍侯..."));
        }


        return MESSAGE_BLOCK;
    }
}
