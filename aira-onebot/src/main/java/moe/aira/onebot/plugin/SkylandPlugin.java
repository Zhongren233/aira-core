package moe.aira.onebot.plugin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import moe.aira.onebot.client.skyland.SkylandApiException;
import moe.aira.onebot.client.skyland.SkylandClient;
import moe.aira.onebot.entity.AiraUser;
import moe.aira.onebot.entity.SkylandSignLog;
import moe.aira.onebot.manager.IAiraUserManager;
import moe.aira.onebot.mapper.AiraUserMapper;
import moe.aira.onebot.mapper.SkylandSignLogMapper;
import moe.aira.onebot.service.SkylandSignService;
import moe.aira.onebot.util.AiraBotPlugin;
import moe.aira.onebot.util.AiraContext;
import moe.aira.onebot.util.AiraSendMessageUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
@Component
public class SkylandPlugin extends AiraBotPlugin {
    private static final String BIND_PREFIX = "#鹰角绑定";
    private static final String SIGN_PREFIX = "#鹰角签到";
    private static final String LOG_PREFIX = "#鹰角日志";

    @Autowired
    SkylandClient skylandClient;
    @Autowired
    AiraUserMapper airaUserMapper;
    @Autowired
    SkylandSignLogMapper skylandSignLogMapper;
    @Autowired
    SkylandSignService skylandSignService;
    @Autowired
    IAiraUserManager airaUserManager;

    @Override
    public boolean checkMessage(@NotNull Bot bot, @NotNull MessageEvent event) {
        String rawMessage = event.getRawMessage();
        return rawMessage.startsWith(BIND_PREFIX)
                || rawMessage.startsWith(SIGN_PREFIX)
                || rawMessage.startsWith(LOG_PREFIX);
    }

    @Override
    public Runnable doCommand(Bot bot, MessageEvent event) {
        String rawMessage = event.getRawMessage();
        AiraUser currentUser = AiraContext.currentUser();
        if (rawMessage.startsWith(BIND_PREFIX)) {
            String token = rawMessage.substring(BIND_PREFIX.length()).trim();
            return () -> bindAction(bot, event, currentUser, token);
        }
        if (rawMessage.startsWith(SIGN_PREFIX)) {
            return () -> signAction(bot, event, currentUser);
        }
        return () -> logAction(bot, event, currentUser);
    }

    private void bindAction(Bot bot, MessageEvent event, AiraUser user, String token) {
        if (token.isEmpty()) {
            AiraSendMessageUtil.sendMessage(bot, event, "请提供鹰角通行证token，格式：#鹰角绑定 <token>");
            return;
        }
        if (user == null) {
            AiraSendMessageUtil.sendMessage(bot, event, "请先绑定QQ号");
            return;
        }
        try {
            skylandClient.getCredByToken(token);
            AiraUser entity = new AiraUser().setId(user.getId()).setYjToken(token);
            airaUserMapper.updateById(entity);
            // 清除用户缓存，避免后续命令读到未更新前的数据
            airaUserManager.cleanCache(user.getQqNumber());
            AiraSendMessageUtil.sendMessage(bot, event, "鹰角token校验成功，已绑定，将于每天23点自动签到");
        } catch (SkylandApiException e) {
            log.warn("鹰角token校验失败: {}", e.getMessage());
            AiraSendMessageUtil.sendMessage(bot, event, "鹰角token校验失败：" + e.getMessage());
        } catch (Exception e) {
            log.error("鹰角绑定异常", e);
            AiraSendMessageUtil.sendMessage(bot, event, "鹰角绑定失败：" + e.getMessage());
        }
    }

    private void signAction(Bot bot, MessageEvent event, AiraUser user) {
        if (user == null) {
            AiraSendMessageUtil.sendMessage(bot, event, "请先绑定QQ号");
            return;
        }
        // 从数据库读取最新 token（当前用户对象可能来自 30 分钟缓存）
        AiraUser fresh = airaUserMapper.selectById(user.getId());
        if (fresh == null || fresh.getYjToken() == null || fresh.getYjToken().isBlank()) {
            AiraSendMessageUtil.sendMessage(bot, event, "尚未绑定鹰角token，请先使用 #鹰角绑定 <token>");
            return;
        }
        try {
            String result = skylandSignService.sign(fresh);
            AiraSendMessageUtil.sendMessage(bot, event, result);
        } catch (Exception e) {
            log.error("鹰角签到异常", e);
            AiraSendMessageUtil.sendMessage(bot, event, "签到失败：" + e.getMessage());
        }
    }

    private void logAction(Bot bot, MessageEvent event, AiraUser user) {
        if (user == null) {
            AiraSendMessageUtil.sendMessage(bot, event, "请先绑定QQ号");
            return;
        }
        QueryWrapper<SkylandSignLog> wrapper = new QueryWrapper<>();
        wrapper.eq("qq_number", user.getQqNumber())
                .orderByDesc("create_time")
                .last("limit 1");
        List<SkylandSignLog> logs = skylandSignLogMapper.selectList(wrapper);
        if (logs.isEmpty()) {
            AiraSendMessageUtil.sendMessage(bot, event, "暂无签到日志");
            return;
        }
        SkylandSignLog log = logs.get(0);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(log.getCreateTime());
        AiraSendMessageUtil.sendMessage(bot, event, "签到时间：" + time + "\n" + log.getResult());
    }
}
