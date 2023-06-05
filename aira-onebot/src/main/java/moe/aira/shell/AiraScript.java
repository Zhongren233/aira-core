package moe.aira.shell;

import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.MessageEvent;
import groovy.lang.Script;
import moe.aira.onebot.util.AiraSendMessageUtil;
import org.springframework.context.ApplicationContext;

public abstract class AiraScript extends Script {


    public void say(Object message) {
        Bot bot = (Bot) this.getBinding().getVariable("bot");
        MessageEvent event = (MessageEvent) this.getBinding().getVariable("messageEvent");
        AiraSendMessageUtil.sendMessage(bot, event, message.toString());
    }

}
