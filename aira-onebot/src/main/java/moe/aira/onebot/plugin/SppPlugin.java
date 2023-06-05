package moe.aira.onebot.plugin;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.AnyMessageEvent;
import lombok.extern.slf4j.Slf4j;
import moe.aira.onebot.entity.AiraCardSppDto;
import moe.aira.onebot.manager.IAiraSppManager;
import moe.aira.onebot.util.AiraSppImageUtil;
import moe.aira.onebot.util.ImageUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static moe.aira.onebot.util.AiraSendMessageUtil.sendMessage;

@Slf4j
@Component
public class SppPlugin extends BotPlugin {

    final
    IAiraSppManager sppManager;

    public SppPlugin(IAiraSppManager sppManager) {
        this.sppManager = sppManager;
    }

    public int onAnyMessage(@NotNull Bot bot, @NotNull AnyMessageEvent event) {
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
        params.sort(String::compareTo);
        params.remove("!spp");
        int size = params.size();
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            List<AiraCardSppDto> cards = sppManager.searchCardsSpp(params);
            if (cards.isEmpty()) {
                builder.text("[错误]未找到相关卡片\n");
                sendMessage(bot, event, builder.build());
                return;
            }
            if (!params.isEmpty()) {
                builder.text("[警告]未识别的过滤词:" + params + "\n");
            }
            if (params.size() == size) {
                builder.text("[错误]没有找到任何有效的过滤词\n");
                sendMessage(bot, event, builder.build());
                return;
            }
            if (cards.size() > 5) {
                builder.text("[提示]结果超过5张卡片，请尝试添加更多搜索条件\n");
            }
            try {
                BufferedImage image = AiraSppImageUtil.generateImage(cards);
                BufferedImage image1 = ImageUtil.bufferedImageToJpg(image, 0.8);
                builder.img(ImageUtil.bufferImageToBase64(image1, "jpg"));
                sendMessage(bot, event, builder.build());
            } catch (IOException e) {
                sendMessage(bot, event, "生成图片失败:\n" + e.getMessage());
                log.error("生成图片失败", e);
            }
        });
        try {
            future.get(5, java.util.concurrent.TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException e) {
            sendMessage(bot, event, "发生错误:\n" + e.getMessage());
            log.error("发生错误", e);
        } catch (TimeoutException e) {
            sendMessage(bot, event, "正在查询...");
        }

        return MESSAGE_BLOCK;
    }

}
