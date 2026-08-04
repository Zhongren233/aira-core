package moe.aira.onebot.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import moe.aira.onebot.client.ESWechatClient;
import moe.aira.onebot.entity.AiraUser;
import moe.aira.onebot.entity.MonthCardInfoResponse;
import moe.aira.onebot.entity.MonthCardRewardResponse;
import moe.aira.onebot.mapper.AiraUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@EnableScheduling

public class MonthCardTask {
    @Autowired
    AiraUserMapper airaUserMapper;
    @Autowired
    ESWechatClient esWechatClient;

    @Scheduled(cron = "0 0 23 * * *")
    public void task() {
        QueryWrapper<AiraUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("open_id");
        List<AiraUser> airaUsers = airaUserMapper.selectList(queryWrapper);
        log.info("[MonthCardTask] 开始月卡任务，待处理用户数: {}", airaUsers.size());
        for (AiraUser airaUser : airaUsers) {
            String openId = airaUser.getOpenId();
            MonthCardInfoResponse monthCardInfo = esWechatClient.getMonthCardInfo(openId);
            log.info("[MonthCardTask] 用户 openid: {}，月卡信息: {}", openId, monthCardInfo);
            if (monthCardInfo.getResult().getState() == 1000) {
                // 普通月卡
                if (monthCardInfo.getResult().getUser_info().getMonthCardInfo().getStatus() == 2) {
                    MonthCardRewardResponse monthCardReward = esWechatClient.getMonthCardReward(openId, "1");
                    log.info("[MonthCardTask] 用户 openid: {}，普通月卡领奖结果: {}", openId, monthCardReward);
                    if ("not in weekday".equals(monthCardReward.getResult().getMsg())) {
                        log.warn("[MonthCardTask] 用户 openid: {}，普通月卡领奖失败：非领奖日", openId);
                        return;
                    }
                }
                // 大月卡
                if (monthCardInfo.getResult().getUser_info().getMonthCardPlusInfo().getStatus() == 2) {
                    MonthCardRewardResponse monthCardReward = esWechatClient.getMonthCardReward(openId, "4");
                    log.info("[MonthCardTask] 用户 openid: {}，大月卡领奖结果: {}", openId, monthCardReward);
                    if ("not in weekday".equals(monthCardReward.getResult().getMsg())) {
                        log.warn("[MonthCardTask] 用户 openid: {}，大月卡领奖失败：非领奖日", openId);
                        return;
                    }
                }
            } else {
                log.warn("[MonthCardTask] 用户 openid: {}，月卡信息获取失败，将清除 openid", openId);
                UpdateWrapper<AiraUser> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("open_id", openId);
                updateWrapper.set("open_id", null);
                airaUserMapper.update(updateWrapper);
            }
        }
    }

}
