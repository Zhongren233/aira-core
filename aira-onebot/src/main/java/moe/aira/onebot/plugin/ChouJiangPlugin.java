package moe.aira.onebot.plugin;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class ChouJiangPlugin extends BotPlugin {
    final
    StringRedisTemplate redisTemplate;

    public ChouJiangPlugin(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public int onGroupMessage(@NotNull Bot bot, @NotNull GroupMessageEvent event) {
        if (event.getGroupId() != 345540873) {
            return MESSAGE_IGNORE;
        }
        MsgUtils at = MsgUtils.builder().at(event.getUserId());

        if (event.getMessage().equals("抽我")) {
            Long add = redisTemplate.opsForSet().add("choujiang:345540873", String.valueOf(event.getUserId()));
            Objects.requireNonNull(add);
            if (add == 0) {
                bot.sendGroupMsg(345540873, at.text("你已经参与过了").build(), false);
            } else {
                bot.sendGroupMsg(345540873, at.text("参与成功").build(), false);
            }
        } else if (event.getMessage().startsWith("开奖")) {
            if (!event.getSender().getRole().equals("owner") && event.getUserId() != 732713726) {
                bot.sendGroupMsg(345540873, at.text("你配吗").build(), false);
            } else {
                Set<String> members = redisTemplate.opsForSet().members("choujiang:345540873");
                if (members == null || members.isEmpty()) {
                    bot.sendGroupMsg(345540873, at.text("没人参与").build(), false);
                    return MESSAGE_BLOCK;
                }
                String[] strings = members.toArray(String[]::new);
                String pop = strings[ThreadLocalRandom.current().nextInt(strings.length)];
                redisTemplate.opsForSet().remove("choujiang:345540873", pop);
                String substring = event.getMessage().substring(2);
                bot.sendGroupMsg(345540873, MsgUtils.builder().text("获得" + substring + "的人是：").at(Long.parseLong(pop)).build(), false);
            }
        }
        return MESSAGE_BLOCK;
    }
}