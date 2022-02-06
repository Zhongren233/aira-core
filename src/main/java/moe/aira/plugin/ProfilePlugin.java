package moe.aira.plugin;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import moe.aira.config.EventConfig;
import moe.aira.core.biz.IAiraEventRankingBiz;
import moe.aira.core.entity.aira.AiraBindRelation;
import moe.aira.core.entity.aira.AiraEventRanking;
import moe.aira.core.entity.es.UserProfile;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ProfilePlugin extends BotPlugin {
    @Autowired
    EventConfig config;
    @Autowired
    IAiraEventRankingBiz airaEventRankingBiz;
    @Override
    public int onPrivateMessage(@NotNull Bot bot, @NotNull PrivateMessageEvent event) {
        if (!event.getMessage().startsWith("!me")) {
            return MESSAGE_IGNORE;
        }
        return super.onPrivateMessage(bot, event);
    }

    @Override
    public int onGroupMessage(@NotNull Bot bot, @NotNull GroupMessageEvent event) {
        String message = event.getMessage();
        if (!message.startsWith("!me")) {
            return MESSAGE_IGNORE;
        }
        boolean debugMode = message.contains("-debug");
        String userId = event.getSender().getUserId();
        MsgUtils builder = MsgUtils.builder();
        builder.at(event.getUserId());
        AiraBindRelation airaBindRelation = airaEventRankingBiz.selectAiraEventBind(userId);
        if (airaBindRelation == null) {
            builder.text("[ERROR]当前尚未绑定");
        } else if (config.isAvailable()){
            long l = System.currentTimeMillis();
            AiraEventRanking airaEventRanking = airaEventRankingBiz.fetchAiraEventRanking(airaBindRelation.getUserId());
            switch (airaEventRanking.getStatus()) {
                case NO_DATA:
                    builder.text("[ERROR]暂无数据");
                    break;
                case NOT_REALTIME_POINT_RANKING:
                    builder.text("[WARN]非实时数据");
                case NOT_REALTIME_SCORE_RANKING:
                case REALTIME_DATA:
                    UserProfile userProfile = airaEventRanking.getUserProfile();
                    builder.text("昵称:").text(userProfile.getUserName()).text("\n");
                    if (debugMode) {
                        ArrayList<Integer> objects = new ArrayList<>(2);
                        objects.add(userProfile.getUserAward1Id());
                        objects.add(userProfile.getUserAward2Id());
                        builder.text("[DEBUG]个人id:").text(String.valueOf(airaEventRanking.getUserId())).text("\n")
                                .text("[DEBUG]头像id:").text((userProfile.getUserFavoriteCardEvolved() ? "[开花]" : "[未觉]" )+ " " + userProfile.getUserFavoriteCardId()).text("\n")
                                .text("[DEBUG]称号:").text(objects.toString()).text("\n")
                                .text("[DEBUG]用时:" + (System.currentTimeMillis() - l)).text("\n");
                    }
                    builder.text("活动点数:").text(String.valueOf(airaEventRanking.getPointRanking().getEventPoint())).text("\n");
                    builder.text("活动点数排名:").text(String.valueOf(airaEventRanking.getPointRanking().getEventRank())).text("\n");
                    builder.text("歌曲点数:").text(String.valueOf(airaEventRanking.getScoreRanking().getEventPoint())).text("\n");
                    builder.text("活动歌曲排名:").text(String.valueOf(airaEventRanking.getScoreRanking().getEventRank())).text("\n");
            }
        }else {
            builder.text("[INFO]当前功能暂不可用");
        }

        bot.sendGroupMsg(event.getGroupId(), builder.build(), false);

        return super.onGroupMessage(bot, event);
    }
}
