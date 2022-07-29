package moe.aira.onebot.plugin;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mikuac.shiro.dto.event.message.MessageEvent;
import moe.aira.onebot.client.LiveChallengeClient;
import moe.aira.onebot.mapper.AiraConfigMapper;
import moe.aira.onebot.util.AiraBotPlugin;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class LiveChallengePlugin extends AiraBotPlugin {
    final
    LiveChallengeClient client;
    final
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    AiraConfigMapper configMapper;

    public LiveChallengePlugin(LiveChallengeClient client, StringRedisTemplate stringRedisTemplate) {
        this.client = client;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean checkMessage(@NotNull Bot bot, @NotNull MessageEvent event) {
        return event.getMessage().trim().replace('！', '!').equals("!lc") && "1".equals(configMapper.selectConfigValueByConfigKey("live_challenge_enable"));
    }

    @Override
    public Runnable doCommand(Bot bot, MessageEvent event) {
        return () -> {
            String live_challenge_info = stringRedisTemplate.opsForValue().get("live_challenge_info");
            if (live_challenge_info == null) {
                live_challenge_info = client.info().getData().toString();
                stringRedisTemplate.opsForValue().set("live_challenge_info", live_challenge_info, Duration.ofMinutes(1));
            }
            if (event instanceof GroupMessageEvent) {
                bot.sendGroupMsg(((GroupMessageEvent) event).getGroupId(), MsgUtils.builder().at(event.getUserId()).text("\n").text(live_challenge_info).build(), false);
            } else {
                bot.sendPrivateMsg(event.getUserId(), live_challenge_info, false);
            }
        };
    }
}
