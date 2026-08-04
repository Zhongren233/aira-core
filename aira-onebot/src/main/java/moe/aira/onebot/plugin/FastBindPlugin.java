package moe.aira.onebot.plugin;

import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.MessageEvent;
import moe.aira.onebot.entity.AiraUser;
import moe.aira.onebot.util.AiraBotPlugin;
import moe.aira.onebot.util.AiraContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

import static moe.aira.onebot.util.AiraSendMessageUtil.sendMessage;

@Component
public class FastBindPlugin extends AiraBotPlugin {
    public FastBindPlugin(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean checkMessage(@NotNull Bot bot, @NotNull MessageEvent event) {
        return event.getRawMessage().startsWith("!迁移至新版Bot");
    }

    final
    StringRedisTemplate stringRedisTemplate;

    @Override
    public Runnable doCommand(Bot bot, MessageEvent event) {
        AiraUser airaUser = AiraContext.currentUser();

        return () -> {

            Integer userId = airaUser.getUserId();
            if (userId == null || userId == 0) {
                sendMessage(bot, event, "没有绑定记录，不能使用快速迁移");
                return;
            }

            String accessCode = UUID.randomUUID().toString();
            stringRedisTemplate.opsForValue().set("FastBind:" + accessCode, userId.toString(), Duration.ofMinutes(2));

            sendMessage(bot, event, "请在官bot内使用:\n" +
                    "/fast-bind " + accessCode + "\n" +
                    "进行绑定，2分钟内有效");

        };
    }

}
