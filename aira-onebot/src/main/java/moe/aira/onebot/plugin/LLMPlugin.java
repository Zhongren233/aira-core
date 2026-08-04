package moe.aira.onebot.plugin;

import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mikuac.shiro.dto.event.notice.PokeNoticeEvent;
import com.openai.client.OpenAIClient;
import com.openai.models.chat.completions.*;
import lombok.extern.slf4j.Slf4j;
import moe.aira.onebot.util.AiraSendMessageUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class LLMPlugin extends BotPlugin {

    private static final long ACTIVATE_DURATION_MS = 10 * 60 * 1000L;
    private static final int MAX_CONTEXT_SIZE = 12;
    private static final Duration CONTEXT_TTL = Duration.ofHours(12);

    private final OpenAIClient openAIClient;
    private final String model;
    private final StringRedisTemplate stringRedisTemplate;

    private final Map<String, Long> activeUntil = new ConcurrentHashMap<>();

    public LLMPlugin(OpenAIClient openAIClient,
                     StringRedisTemplate stringRedisTemplate,
                     @Value("${openai.model}") String model) {
        this.openAIClient = openAIClient;
        this.stringRedisTemplate = stringRedisTemplate;
        this.model = model;
    }

    @Override
    public int onGroupPokeNotice(Bot bot, PokeNoticeEvent event) {
        Long senderId = event.getSenderId();
        Long groupId = event.getGroupId();
        if (senderId == null || groupId == null) {
            return MESSAGE_IGNORE;
        }
        String key = sessionKey(groupId, senderId);
        activeUntil.put(key, System.currentTimeMillis() + ACTIVATE_DURATION_MS);
        AiraSendMessageUtil.sendMessage(bot, event, "我在");
        return MESSAGE_BLOCK;
    }

    @Override
    public int onGroupMessage(Bot bot, GroupMessageEvent event) {
        String raw = event.getRawMessage();
        if (raw == null || raw.isBlank()) {
            return MESSAGE_IGNORE;
        }
        String message = raw.trim();
        String key = sessionKey(event.getGroupId(), event.getUserId());
        if (message.equals("小爱同学")) {
            activeUntil.put(key, System.currentTimeMillis() + ACTIVATE_DURATION_MS);
            AiraSendMessageUtil.sendMessage(bot, event, "我在");
            return MESSAGE_BLOCK;
        }


        boolean isActive = Objects.requireNonNullElse(activeUntil.get(key), 0L) > System.currentTimeMillis();
        boolean commandMode = message.startsWith("!llm ");
        if (!isActive && !commandMode) {
            return MESSAGE_IGNORE;
        }

        String prompt = commandMode ? message.substring(5).trim() : message;
        if (prompt.isBlank()) {
            return MESSAGE_IGNORE;
        }

        try {
            String answer = askLLM(key, prompt);
            AiraSendMessageUtil.sendMessage(bot, event, answer);
            activeUntil.remove(key);
            return MESSAGE_BLOCK;
        } catch (Exception e) {
            log.error("调用LLM失败 group={} user={}", event.getGroupId(), event.getUserId(), e);
            AiraSendMessageUtil.sendMessage(bot, event, "调用失败，请稍后重试");
            return MESSAGE_BLOCK;
        }
    }

    private String askLLM(String key, String prompt) {
        Deque<ChatCompletionMessageParam> context = loadContext(key);
        addUserMessage(context, prompt);
        saveContext(key, context);


        ChatCompletionCreateParams.Builder builder = ChatCompletionCreateParams.builder()
                .model(model)
                .addMessage(ChatCompletionSystemMessageParam.builder()
                        .content("你是一个活跃在QQ群的可爱AI助手，昵称“Aira”。请严格遵循以下规则：\n" +
                                "\n" +
                                "安全策略：绝不回答任何违法、色情、暴力、政治敏感、人身攻击、隐私泄露、诱导诈骗或涉及未成年不当内容。遇到此类问题，请用卖萌语气礼貌拒绝并转移话题（如“这个问题不好呢~我们聊点别的叭(｡•́︿•̀｡)”）。不透露任何真实个人信息，不参与争吵或煽动对立。\n" +
                                "\n" +
                                "回复风格：每条回复控制在1-2句话，不超过200字。语气软萌亲切，常使用颜文字(｡･ω･｡)ﾉ♡、(/ω＼)、(˘▾˘)等。可添加“喵~”“嗷呜~”“嘤嘤嘤”等拟声词，适当使用波浪线、爱心符号。对于正常提问，先友好回应再给出简洁帮助；允许适度卖萌，但不要过度刷屏。")
                        .build());
        context.forEach(builder::addMessage);
        builder.maxCompletionTokens(1024);
        ChatCompletion completion = openAIClient.chat().completions().create(builder.build());
        String content = completion.choices().get(0).message().content().orElse("😅");

        addAssistantMessage(context, content);
        saveContext(key, context);
        return content;
    }

    private void trimContext(Deque<ChatCompletionMessageParam> context) {
        while (context.size() > MAX_CONTEXT_SIZE) {
            context.pollFirst();
        }
    }

    private String sessionKey(Long groupId, Long userId) {
        return groupId + ":" + userId;
    }

    private String contextRedisKey(String key) {
        return "llm:context:" + key;
    }

    private Deque<ChatCompletionMessageParam> loadContext(String key) {
        String redisKey = contextRedisKey(key);
        List<String> values = stringRedisTemplate.opsForList().range(redisKey, 0, -1);
        Deque<ChatCompletionMessageParam> context = new ArrayDeque<>();
        if (values == null) {
            return context;
        }
        for (String value : values) {
            if (value == null || value.length() < 3 || value.charAt(1) != ':') {
                continue;
            }
            String content = value.substring(2);
            if (content.isBlank()) {
                continue;
            }
            char role = value.charAt(0);
            if (role == 'u') {
                addUserMessage(context, content);
            } else if (role == 'a') {
                addAssistantMessage(context, content);
            }
        }
        trimContext(context);
        return context;
    }

    private void saveContext(String key, Deque<ChatCompletionMessageParam> context) {
        trimContext(context);
        String redisKey = contextRedisKey(key);
        stringRedisTemplate.delete(redisKey);
        for (ChatCompletionMessageParam message : context) {
            stringRedisTemplate.opsForList().rightPush(redisKey, serializeMessage(message));
        }
        stringRedisTemplate.expire(redisKey, CONTEXT_TTL);
    }

    private String serializeMessage(ChatCompletionMessageParam message) {
        if (message.isUser()) {
            return "u:" + message.asUser().content().asText();
        }
        if (message.isAssistant()) {
            return "a:" + message.asAssistant().content().get().asText();
        }
        return "";
    }

    private void addUserMessage(Deque<ChatCompletionMessageParam> context, String content) {
        context.addLast(ChatCompletionMessageParam.ofUser(
                ChatCompletionUserMessageParam.builder().content(content).build())
        );
        trimContext(context);
    }

    private void addAssistantMessage(Deque<ChatCompletionMessageParam> context, String content) {
        context.addLast(ChatCompletionMessageParam.ofAssistant(
                ChatCompletionAssistantMessageParam.builder().content(content).build())
        );
        trimContext(context);
    }
}
