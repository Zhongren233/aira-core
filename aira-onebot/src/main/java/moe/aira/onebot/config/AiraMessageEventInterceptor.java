package moe.aira.onebot.config;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotMessageEventInterceptor;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mikuac.shiro.dto.event.message.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import moe.aira.onebot.entity.AiraUser;
import moe.aira.onebot.manager.IAiraUserManager;
import moe.aira.onebot.manager.IEventConfigManager;
import moe.aira.onebot.util.AiraContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AiraMessageEventInterceptor extends BotMessageEventInterceptor {
    final
    IAiraUserManager airaUserManager;
    final
    IEventConfigManager eventConfigManager;

    public AiraMessageEventInterceptor(IAiraUserManager airaUserManager, IEventConfigManager eventConfigManager) {
        this.airaUserManager = airaUserManager;
        this.eventConfigManager = eventConfigManager;
    }

    @Override
    public boolean preHandle(Bot bot, MessageEvent event) throws Exception {
        log.debug("Bot{}收到{}讯息:{}", bot.getSelfId(), event.getUserId(), event.getMessage());
        AiraUser airaUser = airaUserManager.findAiraUser(event.getUserId());
        if (checkBan(bot, event, airaUser))
            return false;
        AiraContext.setUser(airaUser);
        AiraContext.setEventConfig(eventConfigManager.fetchEventConfig());

        return super.preHandle(bot, event);
    }

    private boolean checkBan(Bot bot, MessageEvent event, AiraUser airaUser) {
        if (airaUser.getPermLevel() < 0) {
            if (event instanceof GroupMessageEvent) {
                if (event.getMessage().startsWith("!")) {
                    bot.sendGroupMsg(((GroupMessageEvent) event).getGroupId(), MsgUtils.builder().at(event.getUserId()).text("你没有权限使用本机器人").build(), false);
                }
            } else {
                bot.sendPrivateMsg(event.getUserId(), "你没有权限使用本机器人", false);
            }
            return true;
        }
        return false;
    }

    @Override
    public void afterCompletion(Bot bot, MessageEvent event) throws Exception {
        AiraContext.clear();
        super.afterCompletion(bot, event);
    }
}
