package moe.aira.onebot.plugin;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.common.utils.ShiroUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mikuac.shiro.dto.event.message.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import moe.aira.entity.aira.AiraGachaInfo;
import moe.aira.onebot.entity.AiraGachaResultDto;
import moe.aira.onebot.manager.IAiraGachaManager;
import moe.aira.onebot.util.AiraBotPlugin;
import moe.aira.onebot.util.AiraGachaImageUtil;
import moe.aira.onebot.util.ImageUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

@Slf4j
@Component
public class GachaPlugin extends AiraBotPlugin {
    final
    StringRedisTemplate stringRedisTemplate;

    final
    IAiraGachaManager airaGachaManager;
    private final String[] errorNotice = {
            "呜哇，什么也没有抽出来。",
            "抽到鬼了,God Damn!",
            "抽到了[烫烫烫]",
            "抽到了[锟斤拷]",
            "抽到了[ApiFailedException]",
            "抽到了[MemoryAllocationException]",
            "抽到了[UnsupportedOperationException]",
            "抽到了[预料之外的错误]",
            "抽到了[大変申し訳ありません]",
            "十连抽是什么?能吃吗?",
            "那种事不要啊!!",
            "头脑稍微冷静一下吧",
    };

    public GachaPlugin(IAiraGachaManager airaGachaManager, StringRedisTemplate stringRedisTemplate) {
        List<FlowRule> rules = FlowRuleManager.getRules();
        FlowRule rule = new FlowRule();
        rule.setResource("Gacha");
        rule.setCount(0.1);
        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);

        this.airaGachaManager = airaGachaManager;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean checkMessage(@NotNull Bot bot, @NotNull MessageEvent event) {
        return event.getMessage().contains("十连抽") && ShiroUtils.getAtList(event.getArrayMsg()).contains(bot.getSelfId());
    }

    @Override
    public Runnable doCommand(Bot bot, MessageEvent event) {
        return () -> {
            MsgUtils builder = MsgUtils.builder();
            long userId = event.getUserId();
            if (event instanceof GroupMessageEvent) {
                builder.at(userId);
            }
            if (stringRedisTemplate.opsForValue().get("gacha:" + userId) != null) {
                builder.text(
                        errorNotice[(int) (Math.random() * errorNotice.length)]
                );
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                stringRedisTemplate.opsForValue().set("gacha:" + userId, "1", Duration.ofMinutes(1));
                Integer gachaId = airaGachaManager.currentGacha();
                AiraGachaInfo airaGachaInfo = airaGachaManager.gachaInfo(gachaId);
                AiraGachaResultDto gachaResultDto = airaGachaManager.gacha(airaGachaInfo, 10);
                gachaResultDto.setUserId(userId);
                try {
                    BufferedImage image = ImageUtil.bufferedImageToJpg(AiraGachaImageUtil.generateImage(gachaResultDto));
                    builder.img(ImageUtil.bufferImageToBase64(image, "jpg"));
                } catch (IOException e) {
                    log.error("", e);
                    builder.text("生成图片发生了意外错误: " + e.getMessage());
                }
            }


            if (event instanceof GroupMessageEvent) {
                bot.sendGroupMsg(((GroupMessageEvent) event).getGroupId(), builder.build(), false);
            } else {
                bot.sendPrivateMsg(userId, builder.build(), false);
            }
        };
    }
}
