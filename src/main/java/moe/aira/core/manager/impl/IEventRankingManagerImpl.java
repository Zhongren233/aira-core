package moe.aira.core.manager.impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import moe.aira.core.client.es.PointRankingClient;
import moe.aira.core.client.es.SongRankingClient;
import moe.aira.core.dao.PointRankingMapper;
import moe.aira.core.entity.dto.UserRanking;
import moe.aira.core.entity.es.EventRanking;
import moe.aira.core.entity.es.PointRanking;
import moe.aira.core.entity.es.ScoreRanking;
import moe.aira.core.entity.es.UserProfile;
import moe.aira.core.manager.IEventRankingManager;
import moe.aira.util.IEventRankingParser;
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

    final
    IEventRankingParser eventRankingParser;

    public IEventRankingManagerImpl(PointRankingClient pointRankingClient,
                                    SongRankingClient songRankingClient, IEventRankingParser eventRankingParser) {
        this.pointRankingClient = pointRankingClient;
        this.songRankingClient = songRankingClient;
        this.eventRankingParser = eventRankingParser;
    }

    @Override
    public List<UserRanking<PointRanking>> fetchPointRankings(Integer page) {
        JsonNode node = pointRankingClient.page(page);
        return eventRankingParser.parseToUserRankings(node, PointRanking.class);
    }


    @Override
    public List<UserRanking<ScoreRanking>> fetchScoreRankings(Integer page) {
        JsonNode node = songRankingClient.page(page);
        return eventRankingParser.parseToUserRankings(node, ScoreRanking.class);
    }

    @Override
    public UserRanking<PointRanking> fetchPointRankingByRank(Integer rank) {
        int page = 1;
        int index;
        if (rank <= 19) {
            index = rank - 1;
        } else {
            page += ((rank - 19) / 20);
            index = rank % 20;
        }
        return fetchPointRankings(page).get(index);
    }

    @Override
    public UserRanking<ScoreRanking> fetchScoreRankingByRank(Integer rank) {
        int page = 1;
        int index;
        if (rank <= 19) {
            index = rank - 1;
        } else {
            page += ((rank - 19) / 20);
            index = rank % 20;
        }
        return fetchScoreRankings(page).get(index);
    }

}
