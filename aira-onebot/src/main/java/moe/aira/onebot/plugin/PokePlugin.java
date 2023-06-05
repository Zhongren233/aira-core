package moe.aira.onebot.plugin;

import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.AnyMessageEvent;
import com.mikuac.shiro.dto.event.notice.PokeNoticeEvent;
import moe.aira.onebot.util.AiraSendMessageUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class PokePlugin extends BotPlugin {
    final
    StringRedisTemplate stringRedisTemplate;

    public PokePlugin(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public int onGroupPokeNotice(@NotNull Bot bot, @NotNull PokeNoticeEvent event) {
        long targetId = event.getTargetId();
        if (targetId == bot.getSelfId()) {
            stringRedisTemplate.opsForValue().increment("fwHappyElementsCount");
        }
        return super.onGroupPokeNotice(bot, event);
    }

    public int onAnyMessage(@NotNull Bot bot, @NotNull AnyMessageEvent event) {
        String trim = event.getMessage().trim();
        if (trim.startsWith("!bksb") || trim.startsWith("!sbbk")) {
            AiraSendMessageUtil.sendMessage(bot, event, "当前贝壳飞马次数: " + stringRedisTemplate.opsForValue().get("fwHappyElementsCount"));
            return MESSAGE_BLOCK;
        }
        return MESSAGE_IGNORE;
    }
}
