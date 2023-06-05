package moe.aira.onebot.plugin;

import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.MessageEvent;
import moe.aira.entity.es.CasinoInfo;
import moe.aira.onebot.client.JackpotClient;
import moe.aira.onebot.util.AiraBotPlugin;
import moe.aira.onebot.util.AiraSendMessageUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JackpotPlugin extends AiraBotPlugin {
    @Override
    public boolean checkMessage(@NotNull Bot bot, @NotNull MessageEvent event) {
        String trim = event.getMessage().trim();
        return trim.startsWith("!777")||trim.startsWith("！777");
    }

    @Autowired
    JackpotClient jackpotClient;

    @Override
    public Runnable doCommand(Bot bot, MessageEvent event) {
        CasinoInfo data = jackpotClient.casino().getData();
        return ()->{
            AiraSendMessageUtil.sendMessage(bot, event, data.formatText());
        };
    }
}
