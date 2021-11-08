package moe.aira.core.plugin;

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
            bot.sendPrivateMsg(event.getUserId(), "bilibili.com/video/BV1GJ411x7h7", true);
            return MESSAGE_BLOCK;
        }
        return super.onPrivateMessage(bot, event);
    }

    @Override
    public int onGroupMessage(@NotNull Bot bot, @NotNull GroupMessageEvent event) {
        if (event.getMessage().startsWith("/setu")) {
            MsgUtils text = MsgUtils.builder().at(event.getUserId()).text("\n").text("bilibili.com/video/BV1GJ411x7h7");
            bot.sendGroupMsg(event.getGroupId(), text.build(), true);
            return MESSAGE_BLOCK;
        }
        return super.onGroupMessage(bot, event);
    }
}
