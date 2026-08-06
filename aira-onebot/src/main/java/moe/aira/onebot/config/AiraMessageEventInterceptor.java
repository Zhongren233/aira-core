package moe.aira.onebot.config;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotMessageEventInterceptor;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mikuac.shiro.dto.event.message.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import moe.aira.onebot.entity.AiraUser;
import moe.aira.onebot.manager.IAiraUserManager;
import moe.aira.onebot.manager.IEventConfigManager;
import moe.aira.onebot.service.PushService;
import moe.aira.onebot.util.AiraContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;

@Slf4j
@Component
public class AiraMessageEventInterceptor implements BotMessageEventInterceptor {
    private static final String REPLAY_KEY_PREFIX = "bot:replay:";
    private static final String MULTI_BOT_PUSH_KEY_PREFIX = "bot:replay:push:";
    private static final Duration REPLAY_WINDOW = Duration.ofSeconds(2);
    private static final Duration LOG_WINDOWS = Duration.ofDays(1);

    final
    IAiraUserManager airaUserManager;
    final
    IEventConfigManager eventConfigManager;
    final
    StringRedisTemplate stringRedisTemplate;
    final
    PushService pushService;

    public AiraMessageEventInterceptor(IAiraUserManager airaUserManager, IEventConfigManager eventConfigManager, StringRedisTemplate stringRedisTemplate, PushService pushService) {
        this.airaUserManager = airaUserManager;
        this.eventConfigManager = eventConfigManager;
        this.stringRedisTemplate = stringRedisTemplate;
        this.pushService = pushService;
    }

    @Override
    public boolean preHandle(Bot bot, MessageEvent event) {

        if (isReplay(bot, event)) {
            log.debug("拦截防重放消息 bot={} user={} msg={}", bot.getSelfId(), event.getUserId(), event.getRawMessage());
            if (event instanceof GroupMessageEvent groupEvent) {
                log.warn("群组:{}可能存在多bot问题", groupEvent.getGroupId());
                pushMultiBotAlert(groupEvent);
            }
            return false;
        }
        log.debug("Bot{}收到{}讯息:{}", bot.getSelfId(), event.getUserId(), event.getRawMessage());
        AiraUser airaUser = airaUserManager.findAiraUser(event.getUserId());
        if (checkBan(bot, event, airaUser))
            return false;
        AiraContext.setUser(airaUser);
        log.debug("AiraUser:{}", airaUser);
        AiraContext.setEventConfig(eventConfigManager.fetchEventConfig());
        return true;
    }

    /**
     * 2秒内相同来源发送相同内容的消息视为重放
     */
    private boolean isReplay(Bot bot, MessageEvent event) {
        String rawMessage = event.getRawMessage();
        if (rawMessage == null || rawMessage.isBlank()) {
            return false;
        }
        String key = REPLAY_KEY_PREFIX + ":" + source(event) + ":" +
                DigestUtils.md5DigestAsHex(rawMessage.getBytes(StandardCharsets.UTF_8));
        return Boolean.FALSE.equals(stringRedisTemplate.opsForValue().setIfAbsent(key, "1", REPLAY_WINDOW));
    }

    /**
     * 推送多bot告警,同一群在LOG_WINDOWS(1天)内只推送一次
     */
    private void pushMultiBotAlert(GroupMessageEvent event) {
        String message = "群组:" + event.getGroupId() + "可能存在多bot问题";
        String key = MULTI_BOT_PUSH_KEY_PREFIX + event.getGroupId();
        if (Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(key, "1", LOG_WINDOWS))) {
            pushService.push(message);
        }
    }

    /**
     * 消息来源:群消息为群号+QQ号,私聊为QQ号
     */
    private String source(MessageEvent event) {
        if (event instanceof GroupMessageEvent groupEvent) {
            return "g" + groupEvent.getGroupId() + ":u" + event.getUserId();
        }
        return "u" + event.getUserId();
    }

    private boolean checkBan(Bot bot, MessageEvent event, AiraUser airaUser) {
        if (airaUser.getPermLevel() < 0) {
            if (event instanceof GroupMessageEvent) {
                if (event.getMessage().startsWith("!")) {
                    bot.sendGroupMsg(((GroupMessageEvent) event).getGroupId(), MsgUtils.builder().at(event.getUserId()).text("你没有权限使用本机器人").build(), false);
                }
            } else {
                bot.sendPrivateMsg(event.getUserId(), "你没有权限使用本机器人", false);
            }
            return true;
        }
        return false;
    }

    @Override
    public void afterCompletion(Bot bot, MessageEvent event) {
        AiraContext.clear();
    }
}
