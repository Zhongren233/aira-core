package moe.aira.onebot.config;

import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotMessageEventInterceptor;
import com.mikuac.shiro.dto.event.message.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import moe.aira.onebot.entity.AiraUser;
import moe.aira.onebot.manager.IAiraUserManager;
import moe.aira.onebot.util.AiraUserContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AiraMessageEventInterceptor extends BotMessageEventInterceptor {
    final
    IAiraUserManager airaUserManager;

    public AiraMessageEventInterceptor(IAiraUserManager airaUserManager) {
        this.airaUserManager = airaUserManager;
    }

    @Override
    public boolean preHandle(Bot bot, MessageEvent event) throws Exception {
        log.debug("Bot{}收到{}讯息:{}", bot.getSelfId(), event.getUserId(), event.getMessage());
        AiraUser airaUser = airaUserManager.findAiraUser(event.getUserId());
        AiraUserContext.set(airaUser);
        return super.preHandle(bot, event);
    }

    @Override
    public void afterCompletion(Bot bot, MessageEvent event) throws Exception {
        AiraUserContext.clear();
        super.afterCompletion(bot, event);
    }
}
