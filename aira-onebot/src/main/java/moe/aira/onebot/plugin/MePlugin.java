package moe.aira.onebot.plugin;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.action.common.ActionData;
import com.mikuac.shiro.dto.action.common.MsgId;
import com.mikuac.shiro.dto.event.message.WholeMessageEvent;
import lombok.extern.slf4j.Slf4j;
import moe.aira.entity.aira.AiraEventRanking;
import moe.aira.entity.api.ApiResult;
import moe.aira.enums.AiraEventRankingStatus;
import moe.aira.onebot.clent.AiraUserClient;
import moe.aira.onebot.util.AiraUserContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
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
    public int onWholeMessage(@NotNull Bot bot, @NotNull WholeMessageEvent event) {
        if (!event.getMessage().startsWith("!me")) {
            return MESSAGE_IGNORE;
        }
        Integer userId = AiraUserContext.currentUser().getUserId();
        if (userId == null || userId == 0) {
            sendMessage(bot, event, MsgUtils.builder().text("似乎还没有绑定... 请使用!bind绑定"));
            return MESSAGE_BLOCK;
        } else {
            doCommand(bot, event, userId);
        }
        return MESSAGE_BLOCK;
    }

    private void doCommand(@NotNull Bot bot, @NotNull WholeMessageEvent event, Integer userId) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(
                () -> {
                    MsgUtils patBuild = MsgUtils.builder();
                    ApiResult<AiraEventRanking> airaEventRankingApiResult = airaUserClient.fetchRealTimeAiraEventRanking(userId);
                    log.info("从api获取数据:{}", airaEventRankingApiResult);
                    if (airaEventRankingApiResult.getCode() != 0) {
                        patBuild.text("接口错误:" + airaEventRankingApiResult.getMessage());
                    } else {
                        AiraEventRanking data = airaEventRankingApiResult.getData();
                        switch (data.getStatus()) {
                            case NO_DATA -> patBuild.text("未获取到数据...");
                            case NOT_REALTIME_POINT_RANKING, NOT_REALTIME_SCORE_RANKING, REALTIME_DATA -> {
                                if (data.getStatus() == AiraEventRankingStatus.NOT_REALTIME_POINT_RANKING) {
                                    patBuild.text("警告:非实时数据\n");
                                    patBuild.text("上次更新于:" + new SimpleDateFormat("MM-dd HH:mm").format(data.getPointUpdateTime()) + "\n");
                                }
                                String patternString = MessageFormat.format(PATTERN,
                                        data.getUserProfile().getUserName(),
                                        data.getPointRanking().getEventPoint(),
                                        data.getPointRanking().getEventRank(),
                                        data.getScoreRanking().getEventPoint(),
                                        data.getScoreRanking().getEventRank());

                                if (!send(bot, patBuild.build(), patternString, true, event)) {
                                    log.error("发送消息失败");
                                }

                            }
                            default -> sendMessage(bot, event, MsgUtils.builder().text("未知错误"));
                        }


                    }
                }
        );
        try {
            future.get(4, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            sendMessage(bot, event, MsgUtils.builder().text("正在获取中...请稍后"));
        }
    }

    private boolean send(Bot bot, String pre, String imageContent, boolean asImage, WholeMessageEvent event) {
        MsgUtils msgUtils = MsgUtils.builder().text(pre);
        if (asImage) {
            try {
                long l = System.currentTimeMillis();
                msgUtils.img(parseStringToBase64Image(imageContent));
                log.info("生成图片耗时{} ms", System.currentTimeMillis() - l);
                ActionData<MsgId> actionData = sendMessage(bot, event, msgUtils);
                if (actionData != null && actionData.getRetCode() != 0) {
                    log.warn("发送图片失败");
                    ActionData<MsgId> actionData1 = sendMessage(bot, event, MsgUtils.builder().text(pre).text(imageContent));
                    return actionData1 != null && actionData1.getRetCode() != 0;
                } else {
                    return true;
                }
            } catch (IOException e) {
                log.error("解析图片出错", e);
            }
        }
        ActionData<MsgId> actionData = sendMessage(bot, event, msgUtils.text(imageContent));
        return actionData != null && actionData.getRetCode() != 0;
    }

    private String parseStringToBase64Image(String content) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(700, 370, BufferedImage.TYPE_INT_BGR);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(new Color(209, 248, 245));
        graphics.fillRect(0, 0, 700, 400);
        Font font = new Font("悠哉字体", Font.BOLD, 50);
        graphics.setFont(font);
        String[] split = content.split("\n");
        Stroke tmpStroke = graphics.getStroke();
        for (int i = 0; i < split.length; i++) {
            GlyphVector glyphVector = font.createGlyphVector(graphics.getFontRenderContext(), split[i]);
            Shape outline = glyphVector.getOutline(30, 60f + (i * 70));
            graphics.setStroke(tmpStroke);
            graphics.setColor(new Color(234, 180, 106));
            graphics.fill(outline);
        }
        graphics.dispose();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "PNG", output);


        return "base64://" + Base64.getEncoder().encodeToString(output.toByteArray());

    }

}
