package moe.aira.onebot.plugin;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.WholeMessageEvent;
import lombok.extern.slf4j.Slf4j;
import moe.aira.entity.aira.AiraEventRanking;
import moe.aira.entity.api.ApiResult;
import moe.aira.enums.AiraEventRankingStatus;
import moe.aira.onebot.clent.AiraUserClient;
import moe.aira.onebot.util.AiraUserContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

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
    private static final String pattern = """
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
        } else {
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
                                    patBuild.text(MessageFormat.format(pattern,
                                            data.getUserProfile().getUserName(),
                                            data.getPointRanking().getEventPoint(),
                                            data.getPointRanking().getEventRank(),
                                            data.getScoreRanking().getEventPoint(),
                                            data.getScoreRanking().getEventRank()));
                                }
                                default -> patBuild.text("未知错误");
                            }
                            sendMessage(bot, event, patBuild);
                        }
                    }
            );
            try {
                future.get(2, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                sendMessage(bot, event, MsgUtils.builder().text("正在获取中...请稍后"));
            }
        }
        return MESSAGE_BLOCK;


    }
}
