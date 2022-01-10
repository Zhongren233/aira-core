package moe.aira.plugin;import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;import com.mikuac.shiro.core.Bot;import com.mikuac.shiro.core.BotPlugin;import com.mikuac.shiro.dto.event.message.GroupMessageEvent;import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;import com.mikuac.shiro.dto.event.notice.GroupIncreaseNoticeEvent;import moe.aira.core.entity.aira.Help;import moe.aira.core.dao.AiraHelpMapper;import org.jetbrains.annotations.NotNull;import org.springframework.stereotype.Component;@Componentpublic class InfoPlugin extends BotPlugin {    private final AiraHelpMapper airaHelpMapper;    public InfoPlugin(AiraHelpMapper airaHelpMapper) {        this.airaHelpMapper = airaHelpMapper;    }    @Override    public int onGroupMessage(@NotNull Bot bot, @NotNull GroupMessageEvent event) {        if (event.getMessage().equals("/info")) {            bot.sendGroupMsg(event.getGroupId(), fetchInfo(), true);            return MESSAGE_BLOCK;        }        return MESSAGE_IGNORE;    }    @Override    public int onPrivateMessage(@NotNull Bot bot, @NotNull PrivateMessageEvent event) {        if (event.getMessage().equals("/info")) {            bot.sendPrivateMsg(event.getUserId(), fetchInfo(), true);            return MESSAGE_BLOCK;        }        return MESSAGE_IGNORE;    }    @Override    public int onGroupIncreaseNotice(@NotNull Bot bot, @NotNull GroupIncreaseNoticeEvent event) {        return super.onGroupIncreaseNotice(bot, event);    }    public String fetchInfo() {        QueryWrapper<Help> queryWrapper = new QueryWrapper<>();        queryWrapper.eq("help_name", "info");        Help help = airaHelpMapper.selectOne(queryWrapper);        return help.getHelpDetail();    }}