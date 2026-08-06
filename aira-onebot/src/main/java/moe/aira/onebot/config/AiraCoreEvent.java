package moe.aira.onebot.config;

import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.CoreEvent;
import lombok.extern.slf4j.Slf4j;
import moe.aira.onebot.service.PushService;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.text.MessageFormat;

@Primary
@Component
@Slf4j
public class AiraCoreEvent extends CoreEvent {

    private final PushService pushService;

    public AiraCoreEvent(PushService pushService) {
        this.pushService = pushService;
    }

    @Override
    public void online(@NotNull Bot bot) {
        pushService.push(MessageFormat.format("Bot {0} 已上线", bot.getSelfId()));
        super.online(bot);
    }

    @Override
    public void offline(long account) {
        pushService.push(MessageFormat.format("Bot {0} 已离线", account));
        super.offline(account);
    }

    @Override
    public boolean session(@NotNull WebSocketSession session) {
        return super.session(session);
    }
}
