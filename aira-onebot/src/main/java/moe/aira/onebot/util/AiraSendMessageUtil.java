package moe.aira.onebot.util;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.action.common.ActionData;
import com.mikuac.shiro.dto.action.common.MsgId;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mikuac.shiro.dto.event.message.MessageEvent;
import com.mikuac.shiro.dto.event.message.AnyMessageEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AiraSendMessageUtil {
    private AiraSendMessageUtil() {
    }

    public static ActionData<MsgId> sendMessage(Bot bot, AnyMessageEvent event, String message) {
        return sendMessage(bot, event, MsgUtils.builder().text(message));
    }

    public static ActionData<MsgId> sendMessage(Bot bot, AnyMessageEvent event, MsgUtils builder) {
        long l = System.currentTimeMillis();
        String messageType = event.getMessageType();
        String message = builder.build();
        log.debug("回复{}:{}", event.getUserId(), message);
        ActionData<MsgId> msgIdActionData;
        if (messageType.equals("group")) {
            msgIdActionData = bot.sendGroupMsg(event.getGroupId(), MsgUtils.builder().at(event.getUserId()).text("\n" + message).build(), false);
        } else {
            msgIdActionData = bot.sendPrivateMsg(event.getUserId(), message, false);
        }
        log.debug("响应信息:{}", msgIdActionData);
        log.info("客户端发送用时:{} ms", System.currentTimeMillis() - l);
        return msgIdActionData;
    }

    public static ActionData<MsgId> sendMessage(Bot bot, MessageEvent event, String message) {
        long l = System.currentTimeMillis();
        log.debug("回复{}:{}", event.getUserId(), message);
        ActionData<MsgId> msgIdActionData;
        if (event instanceof GroupMessageEvent) {
            msgIdActionData = bot.sendGroupMsg(((GroupMessageEvent) event).getGroupId(), MsgUtils.builder().at(event.getUserId()).text("\n" + message).build(), false);
        } else {
            msgIdActionData = bot.sendPrivateMsg(event.getUserId(), message, false);
        }
        log.debug("响应信息:{}", msgIdActionData);
        log.info("客户端发送用时:{} ms", System.currentTimeMillis() - l);
        return msgIdActionData;
    }
}
