package moe.aira.core.manager.impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import moe.aira.core.client.es.PointRankingClient;
import moe.aira.core.client.es.SongRankingClient;
import moe.aira.core.entity.dto.UserRanking;
import moe.aira.core.entity.es.EventRanking;
import moe.aira.core.entity.es.PointRanking;
import moe.aira.core.entity.es.ScoreRanking;
import moe.aira.core.entity.es.UserProfile;
import moe.aira.core.manager.IEventRankingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Component
public class IEventRankingManagerImpl implements IEventRankingManager {

    final
    PointRankingClient pointRankingClient;

    final
    SongRankingClient songRankingClient;

    public IEventRankingManagerImpl(PointRankingClient pointRankingClient,
                                    SongRankingClient songRankingClient) {
        this.pointRankingClient = pointRankingClient;
        this.songRankingClient = songRankingClient;
    }

    @Override
    public List<UserRanking<PointRanking>> fetchPointRankings(Integer page) {
        JsonNode node = pointRankingClient.page(page);
        List<UserRanking<PointRanking>> userRankings = parseToUserRankings(node, PointRanking.class);
        return null;
    }

    public <T extends EventRanking> List<UserRanking<T>> parseToUserRankings(JsonNode node,
                                                                             Class<T> clazz) {
        int eventId = node.get("event_id").intValue();
        JsonNode rankingsNode = node.get("rankings");
        ArrayList<UserRanking<T>> userRankings = new ArrayList<>();
        rankingsNode.forEach(rankingNode ->
                userRankings.add(parseToUserRanking(rankingNode, clazz, eventId))
        );
        return userRankings;
    }

    @SneakyThrows
    private <T extends EventRanking> UserRanking<T> parseToUserRanking(JsonNode rankingNode,
                                                                       Class<T> clazz,
                                                                       int eventId) {
        int userId = rankingNode.get("user_id").intValue();


        parseEventRanking(rankingNode, clazz, eventId, userId);

        UserProfile userProfile = parseUserProfile(eventId, userId, rankingNode.get("user_profile"));


        return null;
    }

    private <T extends EventRanking> void parseEventRanking(JsonNode rankingNode,
                                                            Class<T> clazz,
                                                            int eventId,
                                                            int userId) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        UserRanking<T> userRanking = new UserRanking<>();
        T eventRanking = clazz.getDeclaredConstructor().newInstance();
        eventRanking.setEventId(eventId);
        eventRanking.setEventPoint(rankingNode.get("point").intValue());
        eventRanking.setEventRank(rankingNode.get("rank").intValue());
        eventRanking.setUserId(userId);
        userRanking.setRanking(eventRanking);
    }

    private UserProfile parseUserProfile(Integer eventId, int userId, JsonNode profileNode) {
        UserProfile userProfile = new UserProfile();
        JsonNode userAward1 = profileNode.get("user_award1");
        JsonNode userAward2 = profileNode.get("user_award2");
        userProfile.setUserId(userId);
        userProfile.setEventId(eventId);
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
        return userProfile;
    }

    @Override
    public List<UserRanking<ScoreRanking>> fetchScoreRankings(Integer page) {
        return null;
    }

    @Override
    public UserRanking<PointRanking> fetchPointRankingByRank(Integer rank) {
        return null;
    }

    @Override
    public UserRanking<ScoreRanking> fetchScoreRankingByRank(Integer rank) {
        return null;
    }

}
