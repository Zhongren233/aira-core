package moe.aira.util;

import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.core.entity.dto.UserRanking;
import moe.aira.entity.es.EventRanking;

import java.util.List;

public interface IEventRankingParser {
    <T extends EventRanking> List<UserRanking<T>> parseToUserRankings(JsonNode node,
                                                                      Class<T> clazz);
}
