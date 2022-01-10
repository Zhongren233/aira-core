package moe.aira.plugin;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class SetuPlugin extends BotPlugin {
    @Override
    public int onPrivateMessage(@NotNull Bot bot, @NotNull PrivateMessageEvent event) {
        if (event.getMessage().startsWith("/setu")) {
            bot.sendPrivateMsg(event.getUserId(), "bilibili.com/video/BV1VF411Y7MP", false);
            return MESSAGE_BLOCK;
        }
        return super.onPrivateMessage(bot, event);
    }

    @Override
    public int onGroupMessage(@NotNull Bot bot, @NotNull GroupMessageEvent event) {
        if (event.getMessage().startsWith("/setu")) {
            MsgUtils text = MsgUtils.builder().at(event.getUserId()).text("\n").text("bilibili.com/video/BV1VF411Y7MP");
            bot.sendGroupMsg(event.getGroupId(), text.build(), false);
            return MESSAGE_BLOCK;
        }
        return super.onGroupMessage(bot, event);
    }
}
