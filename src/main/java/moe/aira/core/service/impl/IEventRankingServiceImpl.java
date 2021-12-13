package moe.aira.core.service.impl;

import com.dtflys.forest.callback.OnSuccess;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mikuac.shiro.core.BotContainer;
import lombok.extern.slf4j.Slf4j;
import moe.aira.core.client.es.EventsClient;
import moe.aira.core.client.es.PointRankingClient;
import moe.aira.core.entity.es.Christmas2020Tree;
import moe.aira.core.entity.es.PointRanking;
import moe.aira.core.entity.es.UserProfile;
import moe.aira.core.mapper.EsChristmasTreeRecordMapper;
import moe.aira.core.mapper.PointRankingMapper;
import moe.aira.core.service.IEventRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

@Service
@Slf4j
public class IEventRankingServiceImpl implements IEventRankingService {
    final
    PointRankingClient pointRankingClient;
    final
    PointRankingMapper pointRankingMapper;

    public IEventRankingServiceImpl(PointRankingClient pointRankingClient, PointRankingMapper pointRankingMapper) {
        this.pointRankingClient = pointRankingClient;
        this.pointRankingMapper = pointRankingMapper;
    }

    @Override
    public void fetchAllEventRanking() throws InterruptedException {
        long start = System.currentTimeMillis();
        JsonNode page = pointRankingClient.page(1);
        int totalPages = page.get("total_pages").intValue();
//        int totalPages = 10;
        int eventId = page.get("eventId").intValue();
        CountDownLatch countDownLatch = new CountDownLatch(totalPages);
        OnSuccess<JsonNode> onSuccess = (data, request, response) -> {
            ArrayList<PointRanking> pointRankings = new ArrayList<>(20);
            ArrayList<UserProfile> userProfiles = new ArrayList<>(20);
            ArrayNode arrayNode = (ArrayNode) data.get("ranking");
            for (JsonNode jsonNode : arrayNode) {
                JsonNode profileNode = jsonNode.get("user_profile");
                JsonNode userAward1 = profileNode.get("user_award1");
                JsonNode userAward2 = profileNode.get("user_award2");

                UserProfile userProfile = new UserProfile();
                int userId = jsonNode.get("user_id").intValue();
                PointRanking pointRanking = new PointRanking();
                pointRanking.setEventId(eventId);
                pointRanking.setEventPoint(jsonNode.get("point").intValue());
                pointRanking.setEventRank(jsonNode.get("rank").intValue());
                pointRanking.setUserId(userId);
                pointRankings.add(pointRanking);

                userProfile.setUserId(userId);
                userProfile.setUserName(profileNode.get("name").textValue());
                userProfile.setUserFavoriteCardEvolved(profileNode.get("favorite_card_evolved").booleanValue());
                userProfile.setUserFavoriteCardId(profileNode.get("favorite_card_id").intValue());
                if (userAward1 != null) {
                    userProfile.setUserAward1Id(userAward1.get("award_id").intValue());
                    userProfile.setUserAward1Value(userAward1.get("value").intValue());
                }
                if (userAward2 != null) {
                    userProfile.setUserAward2Id(userAward2.get("award_id").intValue());
                    userProfile.setUserAward2Value(userAward2.get("value").intValue());
                }

                userProfiles.add(userProfile);
            }

            pointRankingMapper.upsertPointRanking(pointRankings);
            countDownLatch.countDown();
            log.info("ok");

        };
        for (int currentPage = 1; currentPage <= totalPages; currentPage++) {
            pointRankingClient.asyncPage(currentPage, onSuccess);
        }
        countDownLatch.await();
        long end = System.currentTimeMillis();
        log.info("本次用时 {} ms", end - start);

    }

    @Override
    public void fetchUserEventStat(Integer userId) {

    }

    @Override
    public Integer countAchievePointUser(Integer point) {
        return null;
    }

    @Autowired
    EventsClient eventsClient;

    @Override
    public Christmas2020Tree fetchChristmas2020Tree() {
        JsonNode jsonNode = eventsClient.christmas2020GameTree();
        System.out.println(jsonNode);
        JsonNode christmas2020_tree = jsonNode.get("christmas2020_tree");
        Christmas2020Tree christmas2020Tree = new Christmas2020Tree();
        christmas2020Tree.setTreeId(christmas2020_tree.get("id").intValue());
        christmas2020Tree.setRequiredPoint(christmas2020_tree.get("required_point").intValue());
        christmas2020Tree.setCurrentPoint(christmas2020_tree.get("current_point").intValue());
        christmas2020Tree.setCreateTime(jsonNode.get("current_timestamp").asLong());
        return christmas2020Tree;
    }
}
