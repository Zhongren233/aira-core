package moe.aira.onebot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class PushService {

    @Value("${aira.push-url}")
    private String pushUrl;
    @Autowired
    ObjectMapper objectMapper;

    /**
     * 向 push-url 推送一条文本消息(GET 请求,text 参数 URL 编码)
     */
    public void push(String message) {
        if (pushUrl == null || pushUrl.isEmpty()) {
            return;
        }
        try {
            URL url = new URL(pushUrl + "&text=" + URLEncoder.encode(message, StandardCharsets.UTF_8));
            JsonNode jsonNode = objectMapper.readTree(url);
            log.info("push service return:{}", jsonNode);
        } catch (Exception e) {
            log.error("exception in push event", e);
        }
    }
}
