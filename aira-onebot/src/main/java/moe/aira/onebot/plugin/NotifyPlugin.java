package moe.aira.onebot.plugin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mikuac.shiro.dto.event.message.MessageEvent;
import moe.aira.onebot.entity.AiraGroupSubscribe;
import moe.aira.onebot.mapper.AiraGroupSubscribeMapper;
import moe.aira.onebot.util.AiraBotPlugin;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class NotifyPlugin extends AiraBotPlugin {

    private final AiraGroupSubscribeMapper groupSubscribeMapper;

    public NotifyPlugin(AiraGroupSubscribeMapper groupSubscribeMapper) {
        this.groupSubscribeMapper = groupSubscribeMapper;
    }

    private static boolean check(Bot bot, GroupMessageEvent groupMessageEvent, String[] split) {
        String role = groupMessageEvent.getSender().getRole();
        if (role.equalsIgnoreCase("member")) {
            bot.sendGroupMsg(groupMessageEvent.getGroupId(), "Permission Denied! 请寻求绿帽子获取帮助。", false);
            return true;
        }
        if (split.length < 3) {
            bot.sendGroupMsg(groupMessageEvent.getGroupId(), "参数不足", false);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkMessage(@NotNull Bot bot, @NotNull MessageEvent event) {
        return event instanceof GroupMessageEvent && event.getMessage().replaceFirst("！", "!").startsWith("!notify");
    }

    @Override
    public Runnable doCommand(Bot bot, MessageEvent event) {
        return () -> {
            GroupMessageEvent groupMessageEvent = (GroupMessageEvent) event;
            String[] split = groupMessageEvent.getMessage().split(" ");
            if (split.length < 2) {
                bot.sendGroupMsg(groupMessageEvent.getGroupId(), "参数不足", false);
            }
            QueryWrapper<AiraGroupSubscribe> queryWrapper = new QueryWrapper<>();
            List<AiraGroupSubscribe> subscribes = groupSubscribeMapper.selectList(queryWrapper);
            switch (split[1]) {
                case "list" ->
                        bot.sendGroupMsg(groupMessageEvent.getGroupId(), "当前订阅列表：" + subscribes.stream().map(AiraGroupSubscribe::getChannelName).collect(Collectors.toSet()), false);
                case "sub" -> {
                    if (check(bot, groupMessageEvent, split)) return;
                    String channelName = split[2].toUpperCase();
                    if (subscribes.stream().anyMatch(airaGroupSubscribe -> airaGroupSubscribe.getChannelName().equals(channelName))) {
                        bot.sendGroupMsg(groupMessageEvent.getGroupId(), "之前已经订阅了" + channelName, false);
                        return;
                    }
                    AiraGroupSubscribe airaGroupSubscribe = new AiraGroupSubscribe();
                    airaGroupSubscribe.setGroupId(groupMessageEvent.getGroupId());
                    airaGroupSubscribe.setChannelName(channelName);
                    groupSubscribeMapper.insert(airaGroupSubscribe);
                    bot.sendGroupMsg(groupMessageEvent.getGroupId(), "订阅成功", false);
                }
                case "unsub" -> {
                    if (check(bot, groupMessageEvent, split)) return;
                    String channelName = split[2].toUpperCase();
                    Optional<AiraGroupSubscribe> any = subscribes.stream().filter(airaGroupSubscribe -> airaGroupSubscribe.getChannelName().equals(channelName)).findAny();
                    if (any.isEmpty()) {
                        bot.sendGroupMsg(groupMessageEvent.getGroupId(), "之前没有订阅过" + channelName, false);
                    } else {
                        groupSubscribeMapper.deleteById(any.get().getId());
                        bot.sendGroupMsg(groupMessageEvent.getGroupId(), "取消订阅成功", false);
                    }

                }
            }
        };
    }

}
