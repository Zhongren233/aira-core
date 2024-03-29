package moe.aira.onebot.plugin;

import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.AnyMessageEvent;
import lombok.extern.slf4j.Slf4j;
import moe.aira.onebot.entity.AiraUser;
import moe.aira.onebot.manager.IAiraUserManager;
import moe.aira.onebot.util.AiraContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SuPlugin extends BotPlugin {
    final
    IAiraUserManager airaUserManager;

    public SuPlugin(IAiraUserManager airaUserManager) {
        this.airaUserManager = airaUserManager;
    }

    @Override
    public int onWholeMessage(@NotNull Bot bot, @NotNull AnyMessageEvent event) {
        AiraUser airaUser = AiraContext.currentUser();
//        log.info("current :{}", airaUser);
        String message = event.getMessage();
        if (airaUser.getPermLevel() > 5 && message.startsWith("#su")) {
            String[] split = message.split("\n");
            String suUser = split[0].substring(3).trim();
            AiraContext.setUser(airaUserManager.findAiraUser(Long.valueOf(suUser)));
            event.setMessage(split[1]);
        }
        return MESSAGE_IGNORE;
    }
}
