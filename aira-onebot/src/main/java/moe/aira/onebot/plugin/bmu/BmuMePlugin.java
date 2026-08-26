package moe.aira.onebot.plugin.bmu;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.action.common.ActionData;
import com.mikuac.shiro.dto.action.common.MsgId;
import com.mikuac.shiro.dto.event.message.AnyMessageEvent;
import lombok.extern.slf4j.Slf4j;
import moe.aira.entity.aira.AiraEventRanking;
import moe.aira.entity.es.PointRanking;
import moe.aira.entity.es.ScoreRanking;
import moe.aira.entity.es.UserProfile;
import moe.aira.onebot.client.AiraBmuUserClient;
import moe.aira.onebot.client.dto.BmuAiraRanking;
import moe.aira.onebot.util.AiraContext;
import moe.aira.onebot.util.AiraMeImageUtil;
import moe.aira.onebot.util.ImageUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static moe.aira.onebot.util.AiraSendMessageUtil.sendMessage;

@Component
@Slf4j
public class BmuMePlugin extends BotPlugin {
    private static final String PATTERN = """
            昵称:{0}
            活动点数:{1}
            活动点数排名:{2}
            歌曲点数:{3}
            歌曲点数排名:{4}
            """;
    final
    AiraBmuUserClient airaUserClient;

    public BmuMePlugin(AiraBmuUserClient airaUserClient) {
        this.airaUserClient = airaUserClient;
    }

    public int onAnyMessage(@NotNull Bot bot, @NotNull AnyMessageEvent event) {
        event.setMessage(event.getRawMessage().replaceFirst("！", "!"));

        if (!event.getMessage().startsWith("!me") && !event.getMessage().startsWith("!め")) {
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
        log.info("开始执行me：{}", userId);
        CompletableFuture<Void> future = CompletableFuture.runAsync(
                () -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    {

                        BmuAiraRanking bmuAiraRanking = airaUserClient.fetchRealTimeAiraEventRanking(userId);
                        log.info("从api获取数据:{}", bmuAiraRanking);
                        if (bmuAiraRanking.getError() != null) {
                            stringBuilder.append("接口错误:").append(bmuAiraRanking.getError());
                            throw new RuntimeException(bmuAiraRanking.getError());
                        } else {
                            BmuAiraRanking.Warnings warnings = bmuAiraRanking.getWarnings();
                            if (warnings != null) {
                                SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
                                stringBuilder.append("警告:\n");
                                String pointWarning = warnings.getPointRanking();
                                BmuAiraRanking.Ranking pointRankingData = bmuAiraRanking.getPointRanking();
                                String scoreWarning = warnings.getScoreRanking();
                                BmuAiraRanking.Ranking scoreRankingData = bmuAiraRanking.getScoreRanking();
                                HashSet<String> warnStrings
                                        = new HashSet<>();

                                if (pointWarning != null) {
                                    warnStrings.add(pointWarning);
                                    stringBuilder.append("积分数据:");
                                    if (pointRankingData != null && pointRankingData.getUpdatedAt() != null) {
                                        stringBuilder.append("(更新于 ").append(format.format(pointRankingData.getUpdatedAt())).append(')');
                                    }
                                    stringBuilder.append('\n');
                                }

                                if (scoreWarning != null) {
                                    warnStrings.add(scoreWarning);
                                    stringBuilder.append("分数数据:");
                                    if (scoreRankingData != null && scoreRankingData.getUpdatedAt() != null) {
                                        stringBuilder.append("(更新于 ").append(format.format(scoreRankingData.getUpdatedAt())).append(')');
                                    }
                                    stringBuilder.append('\n');
                                    stringBuilder.append(String.join("\n", warnStrings));
                                    stringBuilder.append('\n');


                                }


                            }

                            if (!send(bot, stringBuilder.toString(), bmuAiraRanking, event)) {
                                log.error("发送消息失败");
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
            log.error("", e);
            sendMessage(bot, event, MsgUtils.builder().text("获取失败:\n").text(e.getCause().toString()));
        }
    }

    private boolean send(Bot bot, String pre, BmuAiraRanking eventRanking, AnyMessageEvent event) {
        MsgUtils msgUtils = MsgUtils.builder().text(pre);
        try {
            long l = System.currentTimeMillis();
            BufferedImage bufferedImage = AiraMeImageUtil.generatorImage(convert(eventRanking));
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

    public AiraEventRanking convert(BmuAiraRanking eventRanking) {
        AiraEventRanking airaEventRanking = new AiraEventRanking();


        BmuAiraRanking.Ranking pointRanking1 = eventRanking.getPointRanking();
        if (pointRanking1 != null) {

            PointRanking pointRanking = new PointRanking();
            airaEventRanking.setPointRanking(pointRanking);
            pointRanking.setEventRank(pointRanking1.getEventRank());
            pointRanking.setEventPoint(pointRanking1.getEventPoint());
        }

        BmuAiraRanking.Ranking scoreRanking1 = eventRanking.getScoreRanking();
        if (scoreRanking1 != null) {
            ScoreRanking scoreRanking = new ScoreRanking();
            scoreRanking.setEventRank(scoreRanking1.getEventRank());
            scoreRanking.setEventPoint(scoreRanking1.getEventPoint());
            airaEventRanking.setScoreRanking(scoreRanking);
        }


        UserProfile userProfile = new UserProfile();
        userProfile.setUserName(eventRanking.getUserProfile().getUserName());
        airaEventRanking.setUserProfile(userProfile);

        return airaEventRanking;
    }


}
