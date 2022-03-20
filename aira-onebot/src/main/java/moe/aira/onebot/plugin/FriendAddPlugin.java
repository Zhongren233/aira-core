package moe.aira.onebot.plugin;

import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.request.FriendAddRequestEvent;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FriendAddPlugin extends BotPlugin {
    @Override
    public int onFriendAddRequest(@NotNull Bot bot, @NotNull FriendAddRequestEvent event) {
        log.info("收到好友请求：{}", event.getUserId());
        bot.setFriendAddRequest(event.getFlag(), true, null);
        return super.onFriendAddRequest(bot, event);
    }
}
