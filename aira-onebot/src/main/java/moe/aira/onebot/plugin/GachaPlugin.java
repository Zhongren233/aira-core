package moe.aira.onebot.plugin;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
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
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class GachaPlugin extends AiraBotPlugin {
    final
    IAiraGachaManager airaGachaManager;

    public GachaPlugin(IAiraGachaManager airaGachaManager) {
        List<FlowRule> rules = FlowRuleManager.getRules();
        FlowRule rule = new FlowRule();
        rule.setResource("Gacha");
        rule.setCount(0.1);
        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);

        this.airaGachaManager = airaGachaManager;
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
            try (Entry entry = SphU.entry("Gacha")) {
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
            } catch (BlockException ex) {
                builder.text("限流中");
            }

            if (event instanceof GroupMessageEvent) {
                bot.sendGroupMsg(((GroupMessageEvent) event).getGroupId(), builder.build(), false);
            } else {
                bot.sendPrivateMsg(userId, builder.build(), false);
            }
        };
    }
}
