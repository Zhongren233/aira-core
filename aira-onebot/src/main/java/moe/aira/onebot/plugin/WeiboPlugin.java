package moe.aira.onebot.plugin;

import com.fasterxml.jackson.databind.JsonNode;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mikuac.shiro.dto.event.message.MessageEvent;
import moe.aira.onebot.service.WeiboService;
import moe.aira.onebot.util.AiraBotPlugin;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;

@Component
public class WeiboPlugin extends AiraBotPlugin {
    final
    WeiboService weiboService;

    public WeiboPlugin(WeiboService weiboService) {
        this.weiboService = weiboService;
    }

    @Override
    public boolean checkMessage(@NotNull Bot bot, @NotNull MessageEvent event) {
        return event.getUserId() == 732713726 && event.getMessage().startsWith("*weibo");
    }

    @Override
    public Runnable doCommand(Bot bot, MessageEvent event) {
        return () -> {
            String trim = event.getMessage().substring(7).trim();
            String[] split = trim.split(" ");
            switch (split[0].toUpperCase(Locale.ROOT)) {
                case "SEND" -> {
                    try {
                        weiboService.sendWeibo(split[1], null);
                        if (event instanceof GroupMessageEvent) {
                            bot.sendGroupMsg(((GroupMessageEvent) event).getGroupId(), "发送成功", false);
                        } else {
                            bot.sendPrivateMsg(event.getUserId(), "发送成功", false);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                case "CODE" -> {
                    try {
                        weiboService.updateToken(split[1]);
                        if (event instanceof GroupMessageEvent) {
                            bot.sendGroupMsg(((GroupMessageEvent) event).getGroupId(), "更新成功", false);
                        } else {
                            bot.sendPrivateMsg(event.getUserId(), "更新成功", false);
                        }
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "INFO" -> {
                    JsonNode node = weiboService.getTokenInfo();
                    if (node != null) {
                        if (event instanceof GroupMessageEvent) {
                            bot.sendGroupMsg(((GroupMessageEvent) event).getGroupId(), node.toPrettyString(), false);
                        } else {
                            bot.sendPrivateMsg(event.getUserId(), node.toPrettyString(), false);
                        }
                    }
                }
            }
        };
    }
}
