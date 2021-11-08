package moe.aira.core.plugin;import com.google.common.util.concurrent.RateLimiter;import com.mikuac.shiro.common.utils.MsgUtils;import com.mikuac.shiro.core.Bot;import com.mikuac.shiro.core.BotPlugin;import com.mikuac.shiro.dto.event.message.GroupMessageEvent;import com.mikuac.shiro.dto.event.message.MessageEvent;import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;import com.mikuac.shiro.dto.event.notice.PokeNoticeEvent;import lombok.extern.slf4j.Slf4j;import moe.aira.core.entity.aira.GachaResult;import moe.aira.core.service.IAiraGachaService;import org.jetbrains.annotations.NotNull;import org.springframework.data.redis.core.StringRedisTemplate;import org.springframework.stereotype.Component;import java.time.Duration;import java.time.LocalDate;import java.time.Month;import java.util.List;import java.util.Objects;import java.util.StringJoiner;import java.util.concurrent.ThreadLocalRandom;@Component@Slf4jpublic class GachaPlugin extends BotPlugin {    final    IAiraGachaService airaGachaService;    final    StringRedisTemplate stringRedisTemplate;    private final ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();    private final RateLimiter rateLimiter;    private final String[] returnMsg = {            "呜哇", "QAQ", "喵呜", "OwO", "?"    };    public GachaPlugin(IAiraGachaService airaGachaService, StringRedisTemplate stringRedisTemplate) {        this.airaGachaService = airaGachaService;        this.stringRedisTemplate = stringRedisTemplate;        rateLimiter = RateLimiter.create(0.5);    }    @Override    public int onGroupPokeNotice(@NotNull Bot bot, @NotNull PokeNoticeEvent event) {        if (event.getTargetId() == event.getSelfId()) {            int i = threadLocalRandom.nextInt(101);            log.info("poke random:{}", i);            if (i > 95) {                bot.sendGroupMsg(event.getGroupId(), MsgUtils.builder().poke(event.getUserId()).build(), false);                return MESSAGE_BLOCK;            }            if (i > 50) {                LocalDate now = LocalDate.now();                if (now.getMonth().equals(Month.OCTOBER)) {                    if (now.getDayOfMonth()==31) {                        if (threadLocalRandom.nextBoolean()) {                            bot.sendGroupMsg(event.getGroupId(), "👻不给糖就捣蛋👻", false);                            return MESSAGE_BLOCK;                        }                    }                }                bot.sendGroupMsg(event.getGroupId(), returnMsg[threadLocalRandom.nextInt(0, returnMsg.length)], false);                return MESSAGE_BLOCK;            }        }        return MESSAGE_BLOCK;    }    @Override    public int onPrivateMessage(@NotNull Bot bot, @NotNull PrivateMessageEvent event) {        long userId = event.getUserId();        MsgUtils builder = processMessage(event, userId);        if (builder != null) {            bot.sendPrivateMsg(userId, builder.build(), false);            return MESSAGE_BLOCK;        } else {            return MESSAGE_IGNORE;        }    }    private MsgUtils processMessage(@NotNull MessageEvent event, long userId) {        String key = "GachaCd::";        String keyTmp = key + userId;        String message = event.getMessage();        String[] split = message.split(" ", 2);        MsgUtils builder = MsgUtils.builder();        Long expire = stringRedisTemplate.getExpire(keyTmp);        if (event instanceof GroupMessageEvent) {            builder.at(userId).text("\n");        }        if (split[0].equals("十连抽五星")) {            if (expire == null) {                return builder.text("发生了神秘错误...");            }            if (expire != -2) {                builder.text("为避免刷屏,本功能设有冷却时间.当前您还有" + expire + "秒冷却结束.\n");                builder.text("在普通群使用冷却时间为:180秒.\n");                return builder;            }            stringRedisTemplate.opsForValue().set(keyTmp, "1", Duration.ofMinutes(120));            builder.text("bilibili.com/video/BV1GJ411x7h7");            return builder;        }        if (Objects.equals(split[0], "十连抽")) {            if (expire == null) {                return builder.text("发生了神秘错误...");            }            if (expire != -2) {                builder.text("为避免刷屏,本功能设有冷却时间.当前您还有" + expire + "秒冷却结束.\n");                builder.text("在普通群使用冷却时间为:180秒.\n");                builder.text("在私聊使用冷却时间为:150秒.\n");                return builder;            }            if (!rateLimiter.tryAcquire()) {                return builder.text("限流中...请稍后再试");            }            String gachaId = null;            if (split.length > 1) {                gachaId = split[1];            }            try {                LocalDate now = LocalDate.now();                if (now.getMonth().equals(Month.OCTOBER)) {                    if (now.getDayOfMonth()==31) {                        if (threadLocalRandom.nextInt(4)==1) {                            builder.text("见鬼了,什么也没抽到.");                            stringRedisTemplate.opsForValue().set(keyTmp, "1", Duration.ofSeconds(30));                            return builder;                        }                    }                }                List<GachaResult> gachaResults = airaGachaService.gachaTenCount(gachaId, userId);                StringJoiner stringJoiner = new StringJoiner("\n");                gachaResults.forEach(gachaResult -> stringJoiner.add(gachaResult.toViewString()));                builder.text(stringJoiner.toString());                builder.text("\n");                int sum = gachaResults.stream().mapToInt(GachaResult::getRarity).sum();                builder.text("抽卡总星数:★").text(String.valueOf(sum));                if (sum == 31) {                    String text = "\n又保底了,看了感觉真可怜!";                    builder.text(text);                } else if (sum >= 38) {                    builder.text("\n为欧皇的诞生献上礼炮!");                } else if (sum >= 34) {                    builder.text("\n哇,金色传说!");                }            } catch (Exception e) {                builder.text(e.getClass().toString()).text("\n").text(e.getMessage());            }            if (event instanceof GroupMessageEvent) {                if (((GroupMessageEvent) event).getGroupId() == 907634127) {                    stringRedisTemplate.opsForValue().set(keyTmp, "1", Duration.ofSeconds(90));                } else {                    stringRedisTemplate.opsForValue().set(keyTmp, "1", Duration.ofSeconds(180));                }            } else {                stringRedisTemplate.opsForValue().set(keyTmp, "1", Duration.ofSeconds(150));            }            return builder;        }        return null;    }    @Override    public int onGroupMessage(@NotNull Bot bot, @NotNull GroupMessageEvent event) {        long userId = event.getUserId();        MsgUtils builder = processMessage(event, userId);        if (builder != null) {            bot.sendGroupMsg(event.getGroupId(), builder.build(), false);            return MESSAGE_BLOCK;        } else {            return MESSAGE_IGNORE;        }    }}