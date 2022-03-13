package moe.aira.onebot.util;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.WholeMessageEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AiraSendMessageUtil {
    private AiraSendMessageUtil() {
    }

    public static void sendMessage(Bot bot, WholeMessageEvent event, MsgUtils builder) {
        String messageType = event.getMessageType();
        String message = builder.build();
        log.info("回复{}:{}", event.getUserId(), message);
        if (messageType.equals("group")) {
            bot.sendGroupMsg(event.getGroupId(), MsgUtils.builder().at(event.getUserId()).text("\n" + message).build(), false);
        } else {
            bot.sendPrivateMsg(event.getUserId(), message, false);
        }

    }
}
