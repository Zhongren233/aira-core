package moe.aira.onebot.util;

import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mikuac.shiro.dto.event.message.MessageEvent;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public abstract class AiraBotPlugin extends BotPlugin {

    @Override
    public int onPrivateMessage(@NotNull Bot bot, @NotNull PrivateMessageEvent event) {
        if (!checkPrivateMessage(bot, event)) {
            return MESSAGE_IGNORE;
        } else {
            defaultHandle(bot, event);
            return MESSAGE_BLOCK;
        }
    }

    @Override
    public int onGroupMessage(@NotNull Bot bot, @NotNull GroupMessageEvent event) {
        if (!checkGroupMessage(bot, event)) {
            return MESSAGE_IGNORE;
        } else {
            defaultHandle(bot, event);
            return MESSAGE_BLOCK;
        }
    }

    public boolean checkGroupMessage(@NotNull Bot bot, @NotNull GroupMessageEvent event) {
        return checkMessage(bot, event);
    }

    public boolean checkPrivateMessage(Bot bot, PrivateMessageEvent event) {
        return checkMessage(bot, event);
    }

    /**
     * 返回true即处理消息
     */
    public abstract boolean checkMessage(@NotNull Bot bot, @NotNull MessageEvent event);


    private void defaultHandle(@NotNull Bot bot, @NotNull MessageEvent event) {
        long l = System.currentTimeMillis();
        Logger logger = LoggerFactory.getLogger(getClass());
        logger.info("开始处理消息");
        CompletableFuture.runAsync(doCommand(bot, event)).whenComplete((r, e) -> {
            if (e == null) {
                logger.info("消息处理完成，耗时：" + (System.currentTimeMillis() - l) + "ms");
            } else {
                logger.error("消息处理失败", e);
            }
        });
    }

    public abstract Runnable doCommand(Bot bot, MessageEvent event);

}
