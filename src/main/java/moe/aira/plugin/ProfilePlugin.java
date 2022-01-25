package moe.aira.plugin;

import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import moe.aira.core.biz.IAiraEventRankingBiz;
import moe.aira.core.entity.aira.AiraBindRelation;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProfilePlugin extends BotPlugin {

    @Autowired
    IAiraEventRankingBiz airaEventRankingBiz;

    @Override
    public int onPrivateMessage(@NotNull Bot bot, @NotNull PrivateMessageEvent event) {
        if (!event.getMessage().startsWith("!me")) {
            return MESSAGE_IGNORE;
        }
//        airaEventRankingBiz.fetchAiraEventRanking();


        return super.onPrivateMessage(bot, event);
    }

    @Override
    public int onGroupMessage(@NotNull Bot bot, @NotNull GroupMessageEvent event) {
        return super.onGroupMessage(bot, event);
    }
}
