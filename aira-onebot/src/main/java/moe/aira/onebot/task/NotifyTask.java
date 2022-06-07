package moe.aira.onebot.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotContainer;
import com.mikuac.shiro.dto.action.common.ActionData;
import com.mikuac.shiro.dto.action.common.ActionList;
import com.mikuac.shiro.dto.action.common.MsgId;
import com.mikuac.shiro.dto.action.response.GroupInfoResp;
import lombok.extern.slf4j.Slf4j;
import moe.aira.onebot.entity.AiraGroupSubscribe;
import moe.aira.onebot.entity.AiraNotify;
import moe.aira.onebot.mapper.AiraGroupSubscribeMapper;
import moe.aira.onebot.mapper.AiraNotifyMapper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@EnableScheduling
@Component
public class NotifyTask {
    final
    AiraGroupSubscribeMapper airaGroupSubscribeMapper;
    final
    AiraNotifyMapper noteMapper;
    final
    BotContainer botContainer;

    public NotifyTask(AiraNotifyMapper noteMapper, BotContainer botContainer, AiraGroupSubscribeMapper airaGroupSubscribeMapper) {
        this.noteMapper = noteMapper;
        this.botContainer = botContainer;
        this.airaGroupSubscribeMapper = airaGroupSubscribeMapper;
    }

    /**
     * 每五分钟执行
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void task() {
        log.info("开始执行定时任务");
        QueryWrapper<AiraNotify> notifyQuery = new QueryWrapper<>();
        notifyQuery.eq("sent", false);
        notifyQuery.lt("start_time", new Date());
        List<AiraNotify> airaNotifies = noteMapper.selectList(notifyQuery);
        if (airaNotifies.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("没有需要发送的通知");
            }
            return;
        }
        for (AiraNotify airaNotify : airaNotifies) {
            QueryWrapper<AiraGroupSubscribe> subscribeQuery = new QueryWrapper<>();
            String channel = airaNotify.getNotifyChannel();
            subscribeQuery.eq("channel_name", channel);
            Set<Long> collect = airaGroupSubscribeMapper.selectList(subscribeQuery).stream().map(AiraGroupSubscribe::getGroupId).collect(Collectors.toSet());
            for (Bot bot : botContainer.robots.values()) {
                ActionList<GroupInfoResp> groupList = bot.getGroupList();
                for (GroupInfoResp groupInfo : groupList.getData()) {
                    long groupId = groupInfo.getGroupId();
                    if (channel.equals("ALL") || collect.remove(groupId)) {
                        ActionData<MsgId> actionData = bot.sendGroupMsg(groupId, airaNotify.getNotifyContent(), false);
                        if (actionData.getStatus().equals("failed")) {
                            log.warn("发送通知到群:{} 失败", groupId);
                        }
                    }
                }
            }
            if (!collect.isEmpty()) {
                log.warn("以下群没有发送通知:{},Bot不在线?", collect);
            }
            airaNotify.setSent(true);
            noteMapper.updateById(airaNotify);
            log.info("发送通知:{}成功", airaNotify.getNotifyTitle());
        }

    }
}
