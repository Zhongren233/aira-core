package moe.aira.core.plugin;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import moe.aira.core.service.IGachaService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GachaPoolPlugin extends BotPlugin {
    final
    IGachaService gachaService;

    public GachaPoolPlugin(IGachaService gachaService) {
        this.gachaService = gachaService;
    }

    @Override
    public int onPrivateMessage(@NotNull Bot bot, @NotNull PrivateMessageEvent event) {
        if (event.getMessage().trim().equals("卡池列表")) {
            MsgUtils msgUtils = MsgUtils.builder().text("当前卡池列表\n").text(gachaService.fetchCurrentGacha().toString());
            bot.sendPrivateMsg(event.getUserId(), msgUtils.build(), true);
            return MESSAGE_BLOCK;
        }
        return super.onPrivateMessage(bot, event);
    }

    @Override
    public int onGroupMessage(@NotNull Bot bot, @NotNull GroupMessageEvent event) {
        if (event.getMessage().trim().equals("卡池列表")) {
            MsgUtils msgUtils = MsgUtils.builder().at(event.getUserId()).text("\n当前卡池列表:\n").text(gachaService.fetchCurrentGacha().toString());

            bot.sendGroupMsg(event.getGroupId(), msgUtils.build(), true);
            return MESSAGE_BLOCK;
        }

        return super.onGroupMessage(bot, event);
    }
}
