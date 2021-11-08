package moe.aira.core.plugin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import moe.aira.core.entity.aira.Help;
import moe.aira.core.mapper.AiraHelpMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HelpPlugin extends BotPlugin {

    final
    AiraHelpMapper airaHelpMapper;

    public HelpPlugin(AiraHelpMapper airaHelpMapper) {
        this.airaHelpMapper = airaHelpMapper;
    }

    private boolean check(String message) {
        return !message.startsWith("/help");
    }

    @Override
    public int onPrivateMessage(@NotNull Bot bot, @NotNull PrivateMessageEvent event) {
        List<String> collect = processCommand(event);
        if (collect == null) return MESSAGE_IGNORE;
        if (collect.isEmpty()) {
            bot.sendPrivateMsg(event.getUserId(), fetchLevel1Help(), true);
            return MESSAGE_BLOCK;
        }

        String query = String.join(" ", collect);
        QueryWrapper<Help> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("help_name", query);
        Help help = airaHelpMapper.selectOne(queryWrapper);
        if (help == null) {
            bot.sendPrivateMsg(event.getUserId(), "没有找到相关help,呜呜.", true);
            return MESSAGE_BLOCK;
        }
        StringBuilder stringBuilder = buildHelpMessahe(queryWrapper, help);
        bot.sendPrivateMsg(event.getUserId(), stringBuilder.toString(), true);
        return MESSAGE_BLOCK;
    }

    @Nullable
    private List<String> processCommand(@NotNull PrivateMessageEvent event) {
        String message = event.getMessage();
        if (check(message)) {
            return null;
        }
        String[] split = message.split(" ");
        List<String> collect = Arrays.stream(split).collect(Collectors.toList());
        collect.remove(0);
        return collect;
    }

    private String fetchLevel1Help() {
        StringBuilder stringBuilder = new StringBuilder();
        QueryWrapper<Help> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_help_id", 0);
        List<Help> helps = airaHelpMapper.selectList(queryWrapper);
        stringBuilder.append("以下为所有可用的help,可使用/help <指令> 来获得具体帮助.\n");
        stringBuilder.append("如/help /pr.\n");
        helps.forEach(help ->
                stringBuilder.append(help.getHelpName()).append(": ").append(help.getHelpInfo()).append("\n")
        );
        return stringBuilder.toString();
    }

    @Override
    public int onGroupMessage(@NotNull Bot bot, @NotNull GroupMessageEvent event) {
        String message = event.getMessage();
        if (check(message)) {
            return MESSAGE_IGNORE;
        }
        String[] split = message.split(" ");
        List<String> collect = Arrays.stream(split).collect(Collectors.toList());
        collect.remove(0);
        if (collect.isEmpty()) {
            bot.sendGroupMsg(event.getGroupId(), fetchLevel1Help(), true);
            return MESSAGE_BLOCK;
        }

        String query = String.join(" ", collect);
        QueryWrapper<Help> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("help_name", query);
        Help help = airaHelpMapper.selectOne(queryWrapper);
        if (help == null) {
            bot.sendGroupMsg(event.getGroupId(), "没有找到相关help,呜呜.", true);
            return MESSAGE_BLOCK;
        }

        StringBuilder stringBuilder = buildHelpMessahe(queryWrapper, help);
        bot.sendGroupMsg(event.getGroupId(), stringBuilder.toString(), true);
        return MESSAGE_BLOCK;

    }

    @NotNull
    private StringBuilder buildHelpMessahe(QueryWrapper<Help> queryWrapper, Help help) {
        Integer id = help.getId();
        queryWrapper.clear();
        queryWrapper.eq("parent_help_id", id);
        List<Help> subHelps = airaHelpMapper.selectList(queryWrapper);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(help.getHelpName()).append("的相关帮助:\n").append(help.getHelpDetail()).append("\n");
        if (!subHelps.isEmpty()) {
            stringBuilder.append("\n");
            stringBuilder.append("此帮助的子级帮助:\n");
            subHelps.forEach(subHelp -> stringBuilder.append(subHelp.getHelpName()).append(": ").append(subHelp.getHelpInfo()).append("\n"));
        } return stringBuilder;
    }
}
