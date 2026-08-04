package moe.aira.onebot.plugin;

import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.MessageEvent;
import moe.aira.entity.aira.AiraObserverDetail;
import moe.aira.entity.aira.AiraObserverInfo;
import moe.aira.onebot.entity.AiraUser;
import moe.aira.onebot.manager.IAiraObserverManager;
import moe.aira.onebot.util.AiraBotPlugin;
import moe.aira.onebot.util.AiraContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.time.Instant;
import java.util.Date;

import static moe.aira.onebot.util.AiraSendMessageUtil.sendMessage;

@Component
public class ObserverPlugin extends AiraBotPlugin {
    private final IAiraObserverManager iAiraObserverManager;

    public ObserverPlugin(IAiraObserverManager iAiraObserverManager) {
        super();
        this.iAiraObserverManager = iAiraObserverManager;
    }

    @Override
    public boolean checkMessage(@NotNull Bot bot, @NotNull MessageEvent event) {
        String message = event.getRawMessage().replaceFirst("！", "!");
        return message.startsWith("!ob");
    }

    @Override
    public Runnable doCommand(Bot bot, MessageEvent event) {
        AiraUser airaUser = AiraContext.currentUser();
        return () -> {
            String[] split = event.getRawMessage().split(" ");
            Integer userId = airaUser.getUserId();
            if (userId == null || userId == 0) {
                sendMessage(bot, event, "你还没有绑定过Aira哦");
                return;
            }
            String subCommand = "info";
            if (split.length > 1) {
                subCommand = split[1];
            }
            String result = switch (subCommand) {
                case "info" -> handleInfo(userId);
                case "reg" -> handleReg(userId);
//                case "detail" -> handleDetail(userId);
                default -> handleInfo(userId);
            };
            sendMessage(bot, event, result);
        };


    }

    private String handleReg(Integer userId) {
        AiraObserverInfo airaObserverInfo = iAiraObserverManager.getAiraObserverInfo(userId);
        if (airaObserverInfo == null) {
            if (System.currentTimeMillis() < 1749391200000L) {
                AiraObserverInfo airaObserverInfo1 = iAiraObserverManager.regAiraObserver(userId);
                return "注册成功，有效期截至" + DateFormat.getInstance().format(airaObserverInfo1.getExpiredTime());
            } else {
                return "暂时不开放注册";
            }
        } else {
            return "已经注册过了";
        }

    }

    private String handleDetail(Integer userId) {
        return "WIP";
    }

    private String handleInfo(Integer userId) {
        AiraObserverInfo airaObserverInfo = iAiraObserverManager.getAiraObserverInfo(userId);
        if (airaObserverInfo == null) {
            return "你还没有注册服务";
        }
        if (airaObserverInfo.getExpiredTime().after(new Date())) {

            AiraObserverDetail lastestAiraObserverDetail = iAiraObserverManager.getLastestAiraObserverDetail(userId);
            if (lastestAiraObserverDetail != null) {
                return "观测时间: " + lastestAiraObserverDetail.getObTime() + "\n" +
                        "观测pt:" + lastestAiraObserverDetail.getObEventPoint();
            }else {
                return "暂无观测记录";
            }


        } else {
            return "观测服务已过期";
        }
    }
}
