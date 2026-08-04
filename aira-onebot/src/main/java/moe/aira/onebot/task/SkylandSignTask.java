package moe.aira.onebot.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import moe.aira.onebot.entity.AiraUser;
import moe.aira.onebot.mapper.AiraUserMapper;
import moe.aira.onebot.service.SkylandSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@EnableScheduling
public class SkylandSignTask {
    @Autowired
    AiraUserMapper airaUserMapper;
    @Autowired
    SkylandSignService skylandSignService;

    @Scheduled(cron = "0 0 23 * * *")
    public void signTask() {
        QueryWrapper<AiraUser> wrapper = new QueryWrapper<>();
        wrapper.isNotNull("yj_token");
        List<AiraUser> users = airaUserMapper.selectList(wrapper);
        log.info("[SkylandSignTask] 开始鹰角签到，待签到用户数: {}", users.size());
        for (AiraUser user : users) {
            try {
                skylandSignService.sign(user);
            } catch (Exception e) {
                log.error("[SkylandSignTask] 用户 {} 签到异常", user.getQqNumber(), e);
            }
        }
    }
}
