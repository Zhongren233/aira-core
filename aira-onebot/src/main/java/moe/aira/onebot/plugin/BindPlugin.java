package moe.aira.onebot.plugin;

import com.alibaba.csp.sentinel.SphO;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.AnyMessageEvent;
import moe.aira.entity.api.ApiResult;
import moe.aira.entity.es.UserInfo;
import moe.aira.exception.AiraException;
import moe.aira.exception.client.AiraIllegalParamsException;
import moe.aira.onebot.client.AiraUserClient;
import moe.aira.onebot.manager.IAiraUserManager;
import moe.aira.onebot.util.AiraContext;
import moe.aira.onebot.util.AiraMessageHolder;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static moe.aira.onebot.util.AiraSendMessageUtil.sendMessage;

@Component
public class BindPlugin extends BotPlugin {
    private static final String HOLDER_KEY = "BIND";
    final
    AiraUserClient airaUserClient;
    final
    IAiraUserManager airaUserManager;
    final
    AiraMessageHolder airaMessageHolder;
    private final String[] randomFirstString =
            {
                    "可爱", "正义", "幽暗",
                    "神圣", "美丽", "害羞",
                    "神秘", "闪耀", "划水",
                    "亮晶晶", "阿妹胫骨", "笑到缺氧",
                    "摸鱼"
            };
    private final String[] randomSecondString =
            {
                    "茄子", "高达", "同学",
                    "老师", "裙带菜", "菠萝",
                    "鮟鱇鱼", "人偶", "兔子",
                    "英雄", "小钢珠",
                    "鸽子", "眼镜", "猫咪",
                    "野狼", "吸血鬼", "卷心菜",
                    "薮猫", "哈士奇", "学生会长"
            };

    public BindPlugin(AiraMessageHolder airaMessageHolder, AiraUserClient airaUserClient, IAiraUserManager airaUserManager) {
        this.airaMessageHolder = airaMessageHolder;
        this.airaUserClient = airaUserClient;
        List<FlowRule> rules = FlowRuleManager.getRules();
        FlowRule rule = new FlowRule();
        rule.setResource("userInfoApiResult");
        rule.setCount(0.2);
        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
        this.airaUserManager = airaUserManager;
    }

    public int onAnyMessage(@NotNull Bot bot, @NotNull AnyMessageEvent event) {
        event.setMessage(event.getMessage().replaceFirst("！", "!"));
        MsgUtils builder = MsgUtils.builder();
        String message = event.getMessage();
        if (message.startsWith("!unbind")) {
            airaUserManager.updateAiraUser(AiraContext.currentUser().setUserId(0));
            builder.text("成功解除绑定");
            sendMessage(bot, event, builder);
            return MESSAGE_BLOCK;
        }

        if (!message.startsWith("!bind")) {
            return MESSAGE_IGNORE;
        }

        if (AiraContext.currentUser().getUserId() == null || AiraContext.currentUser().getUserId() == 0) {
            switch (airaMessageHolder.size(HOLDER_KEY)) {
                case 0 -> {
                    String randomString = createRandomString();
                    airaMessageHolder.put(HOLDER_KEY, randomString);
                    String firstBindMessage = MessageFormat.format("开始进行绑定，请在游戏内简介任意位置包含\n{0}\n后。输入!bind 你的uid。", randomString);
                    builder.text(firstBindMessage);
                }
                case 1 -> bindAction(builder, message);
                default -> builder.text("未知错误");
            }
        } else {
            builder.text("已经绑定过了...解绑请使用!unbind");
        }
        sendMessage(bot, event, builder);
        return MESSAGE_BLOCK;
    }

    private void bindAction(MsgUtils builder, String message) {
        String uid = null;
        try {
            String temp = message.split(" ")[1];
            if (temp.matches("\\d{9}")) {
                uid = temp;
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            builder.text("不正确的uid，请检查后重试。");
        }
        if (uid == null) {
            return;
        }
        if (SphO.entry("userInfoApiResult")) {
            try {
                UserInfo userInfo = tryBind(uid);
                builder.text(MessageFormat.format("成功绑定 {0} !", userInfo.getNickname()));
            } catch (AiraException e) {
                builder.text(e.getMessage());
            }
        } else {
            builder.text("当前绑定人数过多，请稍后再试。");
        }
    }

    private UserInfo tryBind(String uid) {
        try {
            ApiResult<UserInfo> apiResult = airaUserClient.fetchUserInfo(uid);
            if (apiResult.getCode() != 0) {
                throw new AiraException("接口发生了错误,QAQ");
            } else {
                UserInfo data = apiResult.getData();
                String comment = data.getComment();
                String holder = airaMessageHolder.get(HOLDER_KEY).get(0);
                if (comment.contains(holder)) {
                    airaUserManager.updateAiraUser(AiraContext.currentUser().setUserId(data.getUid()));
                } else {
                    throw new AiraIllegalParamsException("似乎还没有修改简介...");
                }
                return data;
            }
        } finally {
            SphO.exit();
        }
    }


    public String createRandomString() {
        ThreadLocalRandom current = ThreadLocalRandom.current();
        int nextInt = current.nextInt(randomFirstString.length);
        String first = randomFirstString[nextInt];
        int nextInt1 = current.nextInt(randomSecondString.length);
        String second = randomSecondString[nextInt1];
        return first + "的" + second;
    }
}
