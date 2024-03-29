package moe.aira.onebot.plugin;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.action.common.ActionData;
import com.mikuac.shiro.dto.action.common.MsgId;
import com.mikuac.shiro.dto.event.message.AnyMessageEvent;
import lombok.extern.slf4j.Slf4j;
import moe.aira.config.EventConfig;
import moe.aira.entity.aira.AiraEventRanking;
import moe.aira.entity.aira.AiraSSFEventRanking;
import moe.aira.entity.api.ApiResult;
import moe.aira.enums.AiraEventRankingStatus;
import moe.aira.onebot.client.AiraUserClient;
import moe.aira.onebot.util.AiraContext;
import moe.aira.onebot.util.AiraMeImageUtil;
import moe.aira.onebot.util.ImageUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static moe.aira.onebot.util.AiraSendMessageUtil.sendMessage;

@Component
@Slf4j
public class MePlugin extends BotPlugin {
    private static final String PATTERN = """
            昵称:{0}
            活动点数:{1}
            活动点数排名:{2}
            歌曲点数:{3}
            歌曲点数排名:{4}
            """;
    final
    AiraUserClient airaUserClient;

    public MePlugin(AiraUserClient airaUserClient) {
        this.airaUserClient = airaUserClient;
    }

    @Override
    public int onWholeMessage(@NotNull Bot bot, @NotNull AnyMessageEvent event) {
        event.setMessage(event.getMessage().replaceFirst("！", "!"));

        if (!event.getMessage().startsWith("!me")) {
            return MESSAGE_IGNORE;
        }
        Integer userId = AiraContext.currentUser().getUserId();
        if (!AiraContext.getEventConfig().checkAvailable()) {
            sendMessage(bot, event, MsgUtils.builder().text("功能暂不可用"));
        } else if (userId == null || userId == 0) {
            sendMessage(bot, event, MsgUtils.builder().text("似乎还没有绑定... 请使用!bind绑定"));
        } else {
            doCommand(bot, event, userId);
        }
        return MESSAGE_BLOCK;
    }

    private void doCommand(@NotNull Bot bot, @NotNull AnyMessageEvent event, Integer userId) {
        EventConfig eventConfig = AiraContext.getEventConfig();
        CompletableFuture<Void> future = CompletableFuture.runAsync(
                () -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    if (eventConfig.getEventId() == 243) {
                        ApiResult<AiraSSFEventRanking> airaEventRankingApiResult = airaUserClient.fetchRealTimeAiraSSFEventRanking(userId);
                        log.info("从api获取数据:{}", airaEventRankingApiResult);
                        if (airaEventRankingApiResult.getCode() != 0) {
                            stringBuilder.append("接口错误:").append(airaEventRankingApiResult.getMessage());
                            throw new RuntimeException(airaEventRankingApiResult.getMessage());
                        } else {
                            AiraSSFEventRanking data = airaEventRankingApiResult.getData();
                            switch (data.getStatus()) {
                                case NO_DATA -> stringBuilder.append("未获取到数据...");
                                case NOT_REALTIME_POINT_RANKING, NOT_REALTIME_SCORE_RANKING, REALTIME_DATA -> {
                                    if (data.getStatus() == AiraEventRankingStatus.NOT_REALTIME_POINT_RANKING) {
                                        stringBuilder.append("警告:非实时数据\n");
                                        stringBuilder.append("上次更新于:").append(new SimpleDateFormat("MM-dd HH:mm").format(data.getPointUpdateTime())).append("\n");
                                    }

                                    if (!sendSSF(bot, stringBuilder.toString(), data, event)) {
                                        log.error("发送消息失败");
                                    }

                                }
                                default -> sendMessage(bot, event, MsgUtils.builder().text("未知错误"));
                            }
                        }
                    } else {

                        ApiResult<AiraEventRanking> airaEventRankingApiResult = airaUserClient.fetchRealTimeAiraEventRanking(userId);
                        log.info("从api获取数据:{}", airaEventRankingApiResult);
                        if (airaEventRankingApiResult.getCode() != 0) {
                            stringBuilder.append("接口错误:").append(airaEventRankingApiResult.getMessage());
                            throw new RuntimeException(airaEventRankingApiResult.getMessage());
                        } else {
                            AiraEventRanking data = airaEventRankingApiResult.getData();
                            switch (data.getStatus()) {
                                case NO_DATA -> stringBuilder.append("未获取到数据...");
                                case NOT_REALTIME_POINT_RANKING, NOT_REALTIME_SCORE_RANKING, REALTIME_DATA -> {
                                    if (data.getStatus() == AiraEventRankingStatus.NOT_REALTIME_POINT_RANKING) {
                                        stringBuilder.append("警告:非实时数据\n");
                                        stringBuilder.append("上次更新于:").append(new SimpleDateFormat("MM-dd HH:mm").format(data.getPointUpdateTime())).append("\n");
                                    }

                                    if (!send(bot, stringBuilder.toString(), data, event)) {
                                        log.error("发送消息失败");
                                    }

                                }
                                default -> sendMessage(bot, event, MsgUtils.builder().text("未知错误"));
                            }
                        }

                    }
                }
        );
        try {
            future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | TimeoutException e) {
            log.error("", e);
            sendMessage(bot, event, MsgUtils.builder().text("正在获取中...请稍后"));
        } catch (ExecutionException e) {
            sendMessage(bot, event, MsgUtils.builder().text("获取失败:\n").text(e.getCause().toString()));
        }
    }

    private boolean send(Bot bot, String pre, AiraEventRanking eventRanking, AnyMessageEvent event) {
        MsgUtils msgUtils = MsgUtils.builder().text(pre);
        try {
            long l = System.currentTimeMillis();
            BufferedImage bufferedImage = AiraMeImageUtil.generatorImage(eventRanking);
            msgUtils.img(ImageUtil.bufferImageToBase64(ImageUtil.bufferedImageToJpg(bufferedImage, 0.8), "jpg"));
            log.info("生成图片耗时{} ms", System.currentTimeMillis() - l);
            ActionData<MsgId> actionData = sendMessage(bot, event, msgUtils);
            if (actionData != null && actionData.getRetCode() == 0) {
                return true;
            } else {
                log.warn("发送图片失败");
                String patternString = MessageFormat.format(PATTERN,
                        eventRanking.getUserProfile().getUserName(),
                        eventRanking.getPointRanking().getEventPoint(),
                        eventRanking.getPointRanking().getEventRank(),
                        eventRanking.getScoreRanking().getEventPoint(),
                        eventRanking.getScoreRanking().getEventRank());
                ActionData<MsgId> actionData1 = sendMessage(bot, event, MsgUtils.builder().text(pre).text(patternString));
                return actionData1 != null && actionData1.getRetCode() != 0;
            }
        } catch (IOException e) {
            log.error("解析图片出错", e);
        }
        return false;
    }

    private boolean sendSSF(Bot bot, String pre, AiraSSFEventRanking eventRanking, AnyMessageEvent event) {
        MsgUtils msgUtils = MsgUtils.builder().text(pre);
        try {
            long l = System.currentTimeMillis();
            BufferedImage bufferedImage = AiraMeImageUtil.generatorImage(eventRanking);
            msgUtils.img(ImageUtil.bufferImageToBase64(ImageUtil.bufferedImageToJpg(bufferedImage, 0.6), "jpg"));
            log.info("生成图片耗时{} ms", System.currentTimeMillis() - l);
            ActionData<MsgId> actionData = sendMessage(bot, event, msgUtils);
            if (actionData != null && actionData.getRetCode() == 0) {
                return true;
            } else {
                log.warn("发送图片失败");
                ActionData<MsgId> actionData1 = sendMessage(bot, event, MsgUtils.builder().text(pre).text("发送图片失败"));
                return actionData1 != null && actionData1.getRetCode() != 0;
            }
        } catch (IOException e) {
            log.error("解析图片出错", e);
        }
        return false;
    }

}
