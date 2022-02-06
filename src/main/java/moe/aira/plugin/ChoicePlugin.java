package moe.aira.plugin;

import com.mikuac.shiro.bean.MsgChainBean;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Component
public class ChoicePlugin extends BotPlugin {
    @Override
    public int onGroupMessage(@NotNull Bot bot, @NotNull GroupMessageEvent event) {
        List<MsgChainBean> arrayMsg = event.getArrayMsg();

        if (arrayMsg.stream().noneMatch(a -> a.getType().equals("at") && a.getData().get("qq").equals(bot.getSelfId() + ""))) {
            return MESSAGE_IGNORE;
        }
        Optional<String> first = arrayMsg.stream().filter(a -> a.getType().equals("text")).map(a -> a.getData().get("text")).findFirst();
        if (first.isEmpty()) {
            return MESSAGE_IGNORE;
        }
        String rawMessage = first.get().trim();
        if (!rawMessage.contains("还是")) {
            return MESSAGE_IGNORE;
        }
        if (rawMessage.startsWith("还是"))
            return MESSAGE_IGNORE;

        MsgUtils at = MsgUtils.builder().at(event.getUserId());

        List<String> choices = Arrays.asList(rawMessage.split("还是"));
        if (choices.size() < 2) {
            bot.sendGroupMsg(event.getGroupId(), "OwO", false);
            return MESSAGE_BLOCK;
        }

        HashSet<String> strings = new HashSet<>(choices);
        if (strings.size() != choices.size()) {
            bot.sendGroupMsg(event.getGroupId(), at.text("建议您遵循内心").build(), false);
            return MESSAGE_BLOCK;
        }

        ThreadLocalRandom current = ThreadLocalRandom.current();
        int nextInt = current.nextInt(choices.size());
        String choice = choices.get(nextInt);

        if (current.nextInt(100) == 0) {
            choice = "全都要";
        }
        String build = at.text("建议您在" + choices + "中选择:\n").text(choice).build();
        bot.sendGroupMsg(event.getGroupId(), build, false);
        return MESSAGE_BLOCK;
    }

}
