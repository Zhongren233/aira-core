package moe.aira.core.task;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotContainer;
import com.mikuac.shiro.dto.action.common.ActionData;
import com.mikuac.shiro.dto.action.common.ActionList;
import com.mikuac.shiro.dto.action.response.GroupAtAllRemainResp;
import com.mikuac.shiro.dto.action.response.GroupInfoResp;
import lombok.extern.slf4j.Slf4j;
import moe.aira.core.entity.es.Christmas2020Tree;
import moe.aira.core.mapper.EsChristmasTreeRecordMapper;
import moe.aira.core.service.impl.IEventRankingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@EnableScheduling
@Component
@Slf4j
public class ChristmasTask {
    final
    IEventRankingServiceImpl service;
    final
    EsChristmasTreeRecordMapper esChristmasTreeRecordMapper;
    final
    StringRedisTemplate stringRedisTemplate;
    final
    BotContainer botContainer;

    private final Long[] pushGroup = {773891409L, 334842786L, 368433275L};

    public ChristmasTask(IEventRankingServiceImpl service, EsChristmasTreeRecordMapper esChristmasTreeRecordMapper, StringRedisTemplate stringRedisTemplate, BotContainer botContainer) {
        this.service = service;
        this.esChristmasTreeRecordMapper = esChristmasTreeRecordMapper;
        this.stringRedisTemplate = stringRedisTemplate;
        this.botContainer = botContainer;
    }

    @Scheduled(cron = "0 0/1 * * * ? ")
    public void recordChrimasTree() {
        Christmas2020Tree christmas2020Tree = service.fetchChristmas2020Tree();
        esChristmasTreeRecordMapper.insert(christmas2020Tree);
        if (christmas2020Tree.getTreeId() % 16 == 0) {
            Integer treeId = christmas2020Tree.getTreeId();
            SetOperations<String, String> stringStringSetOperations = stringRedisTemplate.opsForSet();
            String cacheKey = "ES_CHRIMAS_GOLDED_TREE";
            Boolean member = stringStringSetOperations.isMember(cacheKey, treeId.toString());
            if (Boolean.FALSE.equals(member)) {
                log.info("金树推送");
                MsgUtils msg = MsgUtils.builder().text("提醒金树小助手:\n").text("当前" + treeId).text("棵树是金树");
                stringStringSetOperations.add(cacheKey, String.valueOf(treeId));
                log.info(botContainer.robots.toString());
                for (Bot bot : botContainer.robots.values()) {
                    ActionList<GroupInfoResp> groupList = bot.getGroupList();
                    List<GroupInfoResp> data = groupList.getData();
                    List<Long> collect = data.stream().map(GroupInfoResp::getGroupId).collect(Collectors.toList());
                    List<Long> collect1 = Arrays.stream(pushGroup).filter(collect::contains).collect(Collectors.toList());
                    for (Long groupNum : collect1) {
                        ActionData<GroupAtAllRemainResp> groupAtAllRemain = bot.getGroupAtAllRemain(groupNum);
                        if (groupAtAllRemain.getData().isCanAtAll()) {
                            if (groupAtAllRemain.getData().getRemainAtAllCountForUin() > 0) {
                                String build = MsgUtils.builder().atAll().text(msg.build()).build();
                                bot.sendGroupMsg(groupNum, build, false);
                            }
                        } else {
                            bot.sendGroupMsg(groupNum, msg.build(), false);
                        }
                    }
                }
            }
        }
    }
}
