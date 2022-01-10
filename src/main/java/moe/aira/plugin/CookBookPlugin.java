package moe.aira.plugin;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import moe.aira.core.entity.aira.AiraCookBook;
import moe.aira.core.service.IAiraCookBookService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CookBookPlugin extends BotPlugin {
    @Autowired
    IAiraCookBookService airaCookBookService;
    @Override
    public int onGroupMessage(@NotNull Bot bot, @NotNull GroupMessageEvent event) {
        MsgUtils builder = MsgUtils.builder();
        String message = event.getMessage();
        String[] split = message.split(" ");
        switch (split[0]) {
            case "加菜":
                builder.at(event.getUserId());
                builder.text("\n");
                if (split.length==1) {
                    builder.text("加什么?");
                }else {
//TODO
                }
                return MESSAGE_BLOCK;
            case "吃啥":
            case "吃什么":
                AiraCookBook airaCookBook = airaCookBookService.fetchCookBook();
                builder.at(event.getUserId());
                builder.text("\n");
                builder.text("吃 ").text(airaCookBook.getName());
                return MESSAGE_BLOCK;
        }
        return MESSAGE_IGNORE;
    }
}
