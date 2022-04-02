package moe.aira.onebot.plugin;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.WholeMessageEvent;
import moe.aira.onebot.entity.AiraCardSppDto;
import moe.aira.onebot.manager.IAiraSppManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static moe.aira.onebot.util.AiraSendMessageUtil.sendMessage;

@Component
public class SppPlugin extends BotPlugin {

    final
    IAiraSppManager sppManager;

    public SppPlugin(IAiraSppManager sppManager) {
        this.sppManager = sppManager;
    }

    @Override
    public int onWholeMessage(@NotNull Bot bot, @NotNull WholeMessageEvent event) {
        if (!event.getMessage().startsWith("!spp")) {
            return MESSAGE_IGNORE;
        }

        String[] split = event.getMessage().split(" ");
        if (split.length == 1) {
            String message = "本指令为查询卡片SPP功能，请输入对应的过滤词，如：!spp 北斗 绿";
            sendMessage(bot, event, message);
            return MESSAGE_BLOCK;
        }
        MsgUtils builder = MsgUtils.builder();

        ArrayList<String> params = new ArrayList<>(List.of(split));
        int page = 1;
        if (params.contains("-p")) {
            int index = params.indexOf("-p");
            if (index + 1 < params.size()) {
                try {
                    page = Math.max(Integer.parseInt(params.get(index + 1)), 1);
                } catch (NumberFormatException e) {
                    sendMessage(bot, event, "页码格式错误，请输入数字");
                    return MESSAGE_BLOCK;
                }
                params.remove(index + 1);
            }
            params.remove(index);
        }
        params.remove(0);
        params.sort(String::compareTo);
        List<AiraCardSppDto> cards = sppManager.searchCardsSpp(params);
        if (!params.isEmpty()) {
            builder.text("未识别的搜索词:").text(params.toString()).text("\n");
        }
        if (cards.isEmpty()) {
            builder.text("[错误]未找到相关卡片");
        }
        List<AiraCardSppDto> subPage;
        try {
            subPage = cards.subList((page - 1) * 10, Math.min(cards.size(), page * 10));
        } catch (IndexOutOfBoundsException e) {
            builder.text("[错误]页码超出范围");
            subPage = cards.subList(0, Math.min(cards.size(), 10));
        }


        return MESSAGE_BLOCK;
    }

}
