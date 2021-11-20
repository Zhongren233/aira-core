package moe.aira.core.plugin;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import moe.aira.core.entity.aira.Card;
import moe.aira.core.service.ICardService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CardPlugin extends BotPlugin {
    final
    ICardService cardService;

    public CardPlugin(ICardService cardService) {
        this.cardService = cardService;
    }

    @Override
    public int onGroupMessage(@NotNull Bot bot, @NotNull GroupMessageEvent event) {
        String message = event.getMessage();
        if (!message.startsWith("/card")) {
            return MESSAGE_IGNORE;
        }
        MsgUtils msgUtils = MsgUtils.builder().at(event.getUserId()).text("\n");
        String[] split = message.split(" ");
        ArrayList<String> strings = new ArrayList<>(Arrays.asList(split));
        strings.remove(0);
        if (strings.isEmpty()) {
            msgUtils.text("\n请输入搜索词!");
            bot.sendGroupMsg(event.getGroupId(), msgUtils.build(), true);
            return MESSAGE_BLOCK;
        }
        List<Card> cards;
        try {
            cards = cardService.searchCard(strings);
        } catch (Exception e) {
            msgUtils.text("[ERROR]").text(e.getMessage());
            bot.sendGroupMsg(event.getGroupId(), msgUtils.build(), true);
            return MESSAGE_BLOCK;

        }
        if (!strings.isEmpty()) {
            msgUtils.text("[INFO]出现了Aira不知道的搜索词:\n");
            msgUtils.text(strings.toString());
            msgUtils.text("\n可以尝试将此词交给开发者");
            msgUtils.text("\n");
        }
        int size = cards.size();
        if (size >10) {
            cards = cards.subList(0, 10);
            msgUtils.text("[WARN]搜索条目超过10个仅会显示前十个\n");
        }
        for (Card card : cards) {
            msgUtils.text(card.buildAiraReturnText()).text("\n");
        }
        bot.sendGroupMsg(event.getGroupId(), msgUtils.build(), true);

        return MESSAGE_BLOCK;
    }
}
