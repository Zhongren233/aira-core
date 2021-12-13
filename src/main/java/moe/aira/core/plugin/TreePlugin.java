package moe.aira.core.plugin;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import moe.aira.core.entity.es.Christmas2020Tree;
import moe.aira.core.service.IEventRankingService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.Duration;
@Component
public class TreePlugin extends BotPlugin {
    @Autowired
    IEventRankingService eventRankingService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public int onPrivateMessage(@NotNull Bot bot, @NotNull PrivateMessageEvent event) {
        if (!event.getMessage().startsWith("/tree")) {
            return MESSAGE_IGNORE;
        }
        if (stringRedisTemplate.opsForValue().get("TREE_CD") != null) {
            bot.sendPrivateMsg(event.getUserId(), "TOO MANY REQUEST", false);
            return MESSAGE_IGNORE;
        }
        bot.sendPrivateMsg(event.getUserId(), fetchMessage(), false);
        return MESSAGE_BLOCK;
    }

    private String fetchMessage() {
        Christmas2020Tree christmas2020Tree = eventRankingService.fetchChristmas2020Tree();
        stringRedisTemplate.opsForValue().set("TREE_CD", "1", Duration.ofSeconds(5));
        String format = MessageFormat.format("当前{0}棵树\n({1}/{2})",
                christmas2020Tree.getTreeId(),
                christmas2020Tree.getCurrentPoint(),
                christmas2020Tree.getRequiredPoint());
        if (christmas2020Tree.getTreeId()%16==0) {
            format = "金树time!\n" + format;
        }
        return format;
    }

    @Override
    public int onGroupMessage(@NotNull Bot bot, @NotNull GroupMessageEvent event) {
        if (!event.getMessage().startsWith("/tree")) {
            return MESSAGE_IGNORE;
        }
        if (stringRedisTemplate.opsForValue().get("TREE_CD") != null) {
            bot.sendGroupMsg(event.getGroupId(), "TOO MANY REQUEST", false);
            return MESSAGE_IGNORE;
        }
        bot.sendGroupMsg(event.getGroupId(), MsgUtils.builder().at(event.getUserId()).text("\n").text(fetchMessage()).build(), false);


        return MESSAGE_BLOCK;
    }
}
