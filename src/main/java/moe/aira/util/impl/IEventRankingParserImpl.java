package moe.aira.util.impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import moe.aira.core.entity.dto.UserRanking;
import moe.aira.core.entity.es.EventRanking;
import moe.aira.core.entity.es.UserProfile;
import moe.aira.util.IEventRankingParser;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Component
public class IEventRankingParserImpl implements IEventRankingParser {
    @Override
    public <T extends EventRanking> List<UserRanking<T>> parseToUserRankings(JsonNode node,
                                                                             Class<T> clazz) {
        int eventId = node.get("eventId").intValue();
        JsonNode rankingsNode = node.get("ranking");
        ArrayList<UserRanking<T>> userRankings = new ArrayList<>();
        rankingsNode.forEach(rankingNode -> userRankings.add(parseToUserRanking(rankingNode, clazz, eventId))
        );
        return userRankings;
    }

    @SneakyThrows
    private <T extends EventRanking> UserRanking<T> parseToUserRanking(JsonNode rankingNode,
                                                                       Class<T> clazz,
                                                                       int eventId) {
        int userId = rankingNode.get("user_id").intValue();
        T eventRanking = parseEventRanking(rankingNode, clazz, eventId, userId);
        UserProfile userProfile = parseUserProfile(eventId, userId, rankingNode.get("user_profile"));
        UserRanking<T> userRanking = new UserRanking<>();
        userRanking.setRanking(eventRanking);
        userRanking.setProfile(userProfile);
        userRanking.setUserId(userId);
        return userRanking;
    }

    private <T extends EventRanking> T parseEventRanking(JsonNode rankingNode,
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
        return eventRanking;
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


}
