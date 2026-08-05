package moe.aira.onebot.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import moe.aira.onebot.client.skyland.SkylandApiException;
import moe.aira.onebot.client.skyland.SkylandClient;
import moe.aira.onebot.client.skyland.SkylandLoginExpiredException;
import moe.aira.onebot.entity.AiraUser;
import moe.aira.onebot.entity.SkylandSignLog;
import moe.aira.onebot.mapper.AiraUserMapper;
import moe.aira.onebot.mapper.SkylandSignLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class SkylandSignService {
    @Autowired
    AiraUserMapper airaUserMapper;
    @Autowired
    SkylandSignLogMapper skylandSignLogMapper;
    @Autowired
    SkylandClient skylandClient;

    /**
     * 对用户执行一次完整签到：校验 token → 拉取绑定角色 → 逐角色签到，结果写入签到日志。
     *
     * @return 结果文本，已同步保存到 skyland_sign_log
     */
    public String sign(AiraUser user) {
        SkylandSignLog signLog = new SkylandSignLog()
                .setQqNumber(user.getQqNumber())
                .setSignDate(new Date());
        StringBuilder result = new StringBuilder();
        boolean success = true;
        try {
            SkylandClient.CredInfo cred = skylandClient.getCredByToken(user.getYjToken());
            SkylandClient.BindingApps apps = skylandClient.getBindingApps(cred);
            List<SkylandClient.Character> characters = apps.arknightsCharacters();
            if (characters.isEmpty()) {
                result.append("明日方舟：无绑定角色");
            }
            for (SkylandClient.Character character : characters) {
                SkylandClient.SignResult signResult = skylandClient.signArknights(cred, character);
                if (!signResult.success()) {
                    success = false;
                }
                result.append(signResult.message()).append('\n');
            }
            List<SkylandClient.EndfieldRole> endfieldRoles = apps.endfieldRoles();
            if (endfieldRoles.isEmpty()) {
                result.append("终末地：无绑定角色").append('\n');
            } else {
                for (SkylandClient.EndfieldRole endfieldRole : endfieldRoles) {
                    SkylandClient.SignResult endfieldResult = skylandClient.signEndfield(cred, endfieldRole);
                    if (!endfieldResult.success()) {
                        success = false;
                    }
                    result.append(endfieldResult.message()).append('\n');
                }
            }
        } catch (SkylandLoginExpiredException e) {
            // 登录失效，清除 token，提示用户重新绑定
            success = false;
            result.append("签到失败：").append(e.getMessage());
            UpdateWrapper<AiraUser> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", user.getId()).set("yj_token", null);
            airaUserMapper.update(updateWrapper);
            log.warn("[SkylandSignService] 用户 {} token 已失效，已清除", user.getQqNumber());
        } catch (SkylandApiException e) {
            success = false;
            result.append("签到失败：").append(e.getMessage());
            log.warn("[SkylandSignService] 用户 {} 签到失败: {}", user.getQqNumber(), e.getMessage());
            log.error(e.getMessage(), e);
        }
        signLog.setSuccess(success ? 1 : 0).setResult(result.toString().trim());
        skylandSignLogMapper.insert(signLog);
        return signLog.getResult();
    }
}
