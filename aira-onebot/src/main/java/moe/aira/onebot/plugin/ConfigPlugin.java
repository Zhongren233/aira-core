package moe.aira.onebot.plugin;

import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mikuac.shiro.dto.event.message.MessageEvent;
import moe.aira.onebot.mapper.AiraConfigMapper;
import moe.aira.onebot.util.AiraBotPlugin;
import moe.aira.onebot.util.AiraContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class ConfigPlugin extends AiraBotPlugin {
    final
    AiraConfigMapper airaConfigMapper;

    public ConfigPlugin(AiraConfigMapper airaConfigMapper) {
        this.airaConfigMapper = airaConfigMapper;
    }

    @Override
    public boolean checkMessage(@NotNull Bot bot, @NotNull MessageEvent event) {
        return AiraContext.currentUser().getPermLevel() > 5 && event.getMessage().startsWith("*config");
    }

    @Override
    public Runnable doCommand(Bot bot, MessageEvent event) {

        return () -> {
            String message = event.getMessage();
            String[] split = message.split(" ");
            String returnMessage = "QAQ";
            if (split.length == 2) {
                returnMessage = airaConfigMapper.selectConfigValueByConfigKey(split[1]);
            }
            if (split.length == 3) {
                airaConfigMapper.updateConfigValueByConfigKey(split[1], split[2]);
                returnMessage = "更新成功";
            }
            if (event instanceof GroupMessageEvent) {
                bot.sendGroupMsg(((GroupMessageEvent) event).getGroupId(), returnMessage, false);
            } else {
                bot.sendPrivateMsg(event.getUserId(), returnMessage, false);
            }
        };
    }

}
