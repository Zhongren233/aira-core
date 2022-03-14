package moe.aira.onebot.util;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.action.common.ActionData;
import com.mikuac.shiro.dto.action.common.MsgId;
import com.mikuac.shiro.dto.event.message.WholeMessageEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AiraSendMessageUtil {
    private AiraSendMessageUtil() {
    }

    public static ActionData<MsgId> sendMessage(Bot bot, WholeMessageEvent event, MsgUtils builder) {
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
        return msgIdActionData;
    }

}
