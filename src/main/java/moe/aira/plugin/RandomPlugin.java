package moe.aira.plugin;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class RandomPlugin extends BotPlugin {

    @Override
    public int onGroupMessage(@NotNull Bot bot, @NotNull GroupMessageEvent event) {
        String rawMessage = event.getRawMessage();
        if (!rawMessage.startsWith(".r")) {
            return MESSAGE_IGNORE;
        }
        MsgUtils at = MsgUtils.builder().at(event.getUserId());
        String[] s = rawMessage.split(" ");
        String arg;
        if (s.length == 1) {
            arg="100";
        }else {
            arg = s[1];
        }
        try {
            int i = Integer.parseInt(arg);
            int nextInt = ThreadLocalRandom.current().nextInt(i);
            bot.sendGroupMsg(event.getGroupId(), at.text(String.valueOf(nextInt)).build(),false);
        } catch (NumberFormatException e) {
            bot.sendGroupMsg(event.getGroupId(), at.text("参数" + arg + "有误").build(), false);
        }
        return MESSAGE_BLOCK;
    }
}
