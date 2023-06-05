package moe.aira.onebot.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.CoreEvent;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

@Primary
@Component
@Slf4j
public class AiraCoreEvent extends CoreEvent {

    @Value("${aira.push-url}")
    private String pushUrl;
    @Autowired
    ObjectMapper objectMapper;
    @Override
    public void online(@NotNull Bot bot) {
        if (!pushUrl.isEmpty()) {

        try {
            String pushMessage = MessageFormat.format("Bot {0} 已上线", bot.getSelfId());
            URL url = new URL(pushUrl + "&text=" + URLEncoder.encode(pushMessage, StandardCharsets.UTF_8));
            JsonNode jsonNode = objectMapper.readTree(url);
            log.info("push service return:{}", jsonNode);
        } catch (Exception e) {
            log.error("exception in push event", e);
        }
        }
        super.online(bot);
    }

    @Override
    public void offline(long account) {
        try {
            String pushMessage = MessageFormat.format("Bot {0} 已离线", account);
            URL url = new URL(pushUrl + "&text=" + URLEncoder.encode(pushMessage, StandardCharsets.UTF_8));
            JsonNode jsonNode = objectMapper.readTree(url);
        } catch (Exception e) {
            log.error("exception in push event", e);
        }
        super.offline(account);
    }

    @Override
    public boolean session(@NotNull WebSocketSession session) {
        return super.session(session);
    }
}
