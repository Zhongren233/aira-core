package moe.aira.onebot.plugin;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.AnyMessageEvent;
import lombok.extern.slf4j.Slf4j;
import moe.aira.config.EventConfig;
import moe.aira.entity.aira.AiraEventPointDto;
import moe.aira.entity.aira.AiraEventScoreDto;
import moe.aira.entity.api.ApiResult;
import moe.aira.enums.EventRank;
import moe.aira.onebot.client.AiraEventClient;
import moe.aira.onebot.util.AiraContext;
import moe.aira.onebot.util.AiraRankingImageUtil;
import moe.aira.onebot.util.ImageUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static moe.aira.onebot.util.AiraSendMessageUtil.sendMessage;

@Slf4j
@Component
public class RankingPlugin extends BotPlugin {
    final
    AiraEventClient eventClient;

    public RankingPlugin(AiraEventClient eventClient) {
        this.eventClient = eventClient;
    }

    private static String calcColorType(String[] split) {
        if (split.length < 2) {
            return "RED";
        }
        if (split[1].equalsIgnoreCase("WHITE")) {
            return "WHITE";
        } else {
            return "RED";
        }
    }

    public int onAnyMessage(@NotNull final Bot bot, @NotNull final AnyMessageEvent event) {
        event.setMessage(event.getMessage().replaceFirst("！", "!"));
        String message = event.getMessage().trim();
        if (!(message.startsWith("!pr") || message.startsWith("!sr"))) {
            return MESSAGE_IGNORE;
        }
        EventConfig eventConfig = AiraContext.getEventConfig();
        if (!eventConfig.checkAvailable()) {
            sendMessage(bot, event, MsgUtils.builder().text("功能暂不可用"));
            return MESSAGE_IGNORE;
        }
        String[] split = message.split(" ");
        CompletableFuture<Void> future =
                switch (message.substring(0, 3)) {
                    case "!pr" -> CompletableFuture.runAsync(
                            () -> {
                                Integer[] ranks = Arrays.stream(EventRank.values()).map(EventRank::getRank).toList().toArray(new Integer[0]);
                                ApiResult<List<AiraEventPointDto>> listApiResult = eventClient.fetchCurrentRankPoint(ranks);
                                if (listApiResult == null || listApiResult.getCode() != 0) {
                                    sendMessage(bot, event, MsgUtils.builder().text("获取排名积分失败"));
                                    return;
                                }
                                List<AiraEventPointDto> data = listApiResult.getData();
                                BufferedImage image = AiraRankingImageUtil.generatorPointImage(data);
                                try {
                                    sendMessage(bot, event, MsgUtils.builder().img(ImageUtil.bufferImageToBase64(image)));
                                } catch (IOException e) {
                                    log.error("", e);
                                    sendMessage(bot, event, MsgUtils.builder().text("生成图片失败: " + e.getMessage()));
                                }
                            });
                    case "!sr" -> CompletableFuture.runAsync(() -> {
                        Integer[] ranks = Arrays.stream(EventRank.values()).map(EventRank::getRank).toList().toArray(new Integer[0]);
                        ApiResult<List<AiraEventScoreDto>> listApiResult;
                        String colorType = null;
                        if (eventConfig.getEventId() == 243) {
                            colorType = calcColorType(split);
                            listApiResult = eventClient.ssfFetchCurrentRankScore(colorType, ranks);
                        } else {
                            listApiResult = eventClient.fetchCurrentRankScore(ranks);
                        }
                        if (listApiResult == null || listApiResult.getCode() != 0) {
                            sendMessage(bot, event, MsgUtils.builder().text("获取排名歌曲失败"));
                            return;
                        }
                        List<AiraEventScoreDto> data = listApiResult.getData();
                        try {
                            BufferedImage image;
                            if (colorType != null) {
                                image = AiraRankingImageUtil.generatorScoreImage(data, colorType.equals("RED") ?
                                                new Color(197, 3, 3) :
                                                new Color(95, 91, 93),
                                        "image/template/Sr-Ranking-" + colorType + ".png");
                            } else {
                                image = AiraRankingImageUtil.generatorScoreImage(data);

                            }
                            sendMessage(bot, event, MsgUtils.builder().img(ImageUtil.bufferImageToBase64(ImageUtil.bufferedImageToJpg(image, 0.8), "jpg")));
                        } catch (IOException e) {
                            log.error("", e);
                            sendMessage(bot, event, MsgUtils.builder().text("生成图片失败: " + e.getMessage()));
                        }
                    });
                    default -> null;
                };
        if (future == null) {
            sendMessage(bot, event, MsgUtils.builder().text("未知错误"));
            return MESSAGE_BLOCK;
        }
        try {
            future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            log.error("", e);
            sendMessage(bot, event, MsgUtils.builder().text("正在获取，请稍侯..."));
        }


        return MESSAGE_BLOCK;
    }
}
