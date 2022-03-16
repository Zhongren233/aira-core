package moe.aira.onebot.plugin;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.WholeMessageEvent;
import moe.aira.entity.api.ApiResult;
import moe.aira.enums.EventRank;
import moe.aira.onebot.clent.AiraEventClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

import static moe.aira.onebot.util.AiraSendMessageUtil.sendMessage;

@Component
public class PointStatPlugin extends BotPlugin {
    final
    AiraEventClient eventClient;

    public PointStatPlugin(AiraEventClient eventClient) {
        this.eventClient = eventClient;
    }

    @Override
    public int onWholeMessage(@NotNull Bot bot, @NotNull WholeMessageEvent event) {
        Integer[] ranks = (Integer[]) Arrays.stream(EventRank.values()).map(EventRank::getRank).toArray();
        ApiResult<Map<Integer, Integer>> mapApiResult = eventClient.fetchCurrentRankPoint(ranks);
        if (mapApiResult == null || mapApiResult.getCode() != 0) {
            sendMessage(bot, event, MsgUtils.builder().text("获取排名积分失败"));
            return MESSAGE_BLOCK;
        }

        return super.onWholeMessage(bot, event);
    }
}
