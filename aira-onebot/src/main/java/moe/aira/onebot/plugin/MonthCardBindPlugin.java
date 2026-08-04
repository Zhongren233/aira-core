package moe.aira.onebot.plugin;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.MessageEvent;
import moe.aira.onebot.client.AiraUserClient;
import moe.aira.onebot.client.ESWechatClient;
import moe.aira.onebot.entity.AiraUser;
import moe.aira.onebot.entity.MonthCardInfoResponse;
import moe.aira.onebot.mapper.AiraUserMapper;
import moe.aira.onebot.util.AiraBotPlugin;
import moe.aira.onebot.util.AiraContext;
import moe.aira.onebot.util.AiraSendMessageUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MonthCardBindPlugin extends AiraBotPlugin {
    @Override
    public boolean checkMessage(@NotNull Bot bot, @NotNull MessageEvent event) {
        return event.getRawMessage().startsWith("!月卡绑定");
    }

    @Autowired
    AiraUserMapper airaUserMapper;
    @Autowired
    ESWechatClient esWechatClient;

    @Override
    public Runnable doCommand(Bot bot, MessageEvent event) {
        AiraUser airaUser = AiraContext.currentUser();

        return () -> {
            String[] split = event.getRawMessage().split(" ");
            if (split.length<2) {
                AiraSendMessageUtil.sendMessage(bot, event, "请输入openId");
            }
            MonthCardInfoResponse monthCardInfo = esWechatClient.getMonthCardInfo(split[1]);
            if (monthCardInfo.getResult().getState()!=1000) {
                AiraSendMessageUtil.sendMessage(bot, event, "无效的openId");
            }
            AiraUser entity = new AiraUser();
            entity.setId(airaUser.getId());
            entity.setOpenId(split[1]);
            airaUserMapper.updateById(entity);
            AiraSendMessageUtil.sendMessage(bot, event, "绑定成功");

        };
    }

}
