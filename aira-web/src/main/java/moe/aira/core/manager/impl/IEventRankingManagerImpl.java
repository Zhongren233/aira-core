package moe.aira.core.manager.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxl.job.core.context.XxlJobHelper;
import lombok.extern.slf4j.Slf4j;
import moe.aira.annotation.RecordToDataBase;
import moe.aira.core.client.es.PointRankingClient;
import moe.aira.core.client.es.ScoreRankingClient;
import moe.aira.core.entity.dto.UserRanking;
import moe.aira.core.manager.IEventRankingManager;
import moe.aira.entity.es.PointRanking;
import moe.aira.entity.es.ScoreRanking;
import moe.aira.util.IEventRankingParser;
import moe.aira.util.RankPageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class IEventRankingManagerImpl implements IEventRankingManager {

    final
    PointRankingClient pointRankingClient;

    final
    ScoreRankingClient scoreRankingClient;

    final
    IEventRankingParser eventRankingParser;

    private IEventRankingManager iocEventRankingManager;

    public IEventRankingManagerImpl(PointRankingClient pointRankingClient,
                                    ScoreRankingClient scoreRankingClient, IEventRankingParser eventRankingParser) {
        this.pointRankingClient = pointRankingClient;
        this.scoreRankingClient = scoreRankingClient;
        this.eventRankingParser = eventRankingParser;
    }

    @Override
    public Integer fetchTotalPointRankingPage() {
        JsonNode node = pointRankingClient.page(1);
        return node.get("total_pages").intValue();
    }

    @Override
    public Integer fetchTotalScoreRankingPage() {
        JsonNode node = scoreRankingClient.page(1);
        return node.get("total_pages").intValue();
    }

    @Override
    @RecordToDataBase
    public List<UserRanking<PointRanking>> fetchPointRankings(Integer page) {
        JsonNode node = pointRankingClient.page(page);
        return eventRankingParser.parseToUserRankings(node, PointRanking.class);
    }


    @Override
    @RecordToDataBase
    public List<UserRanking<ScoreRanking>> fetchScoreRankings(Integer page) {
        JsonNode node = scoreRankingClient.page(page);
        return eventRankingParser.parseToUserRankings(node, ScoreRanking.class);
    }

    @Override
    public CompletableFuture<List<UserRanking<PointRanking>>> fetchPointRankingsAsync(Integer page) {
        CompletableFuture<List<UserRanking<PointRanking>>> completableFuture = new CompletableFuture<>();
        pointRankingClient.asyncPage(page,
                (data, req, res) ->
                        completableFuture.complete(eventRankingParser.parseToUserRankings(data, PointRanking.class)),
                (ex, req, res) -> {
                    Object body = req.getAttachment("body");
                    log.error("on error in {}", body);
                    XxlJobHelper.log("skip {} point ranking request", body);
                    completableFuture.complete(null);
                });
        return completableFuture;
    }

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public CompletableFuture<List<UserRanking<ScoreRanking>>> fetchScoreRankingsAsync(Integer page) {
        CompletableFuture<List<UserRanking<ScoreRanking>>> completableFuture = new CompletableFuture<>();
        scoreRankingClient.asyncPage(page,
                (data, req, res) ->
                        completableFuture.complete(eventRankingParser.parseToUserRankings(data, ScoreRanking.class)),
                (ex, req, res) -> {
                    Object body = req.getAttachment("body");
                    log.error("on error in {}", body);
                    XxlJobHelper.log("skip {} point ranking request", body);
                    completableFuture.complete(null);
                });
        return completableFuture;
    }

    @Override
    public UserRanking<PointRanking> fetchPointRankingByRank(Integer rank) {
        int page = RankPageCalculator.calcPage(rank);
        int index = RankPageCalculator.calcIndex(rank);
        return iocEventRankingManager.fetchPointRankings(page).get(index);
    }

    @Override
    public UserRanking<ScoreRanking> fetchScoreRankingByRank(Integer rank) {
        int page = RankPageCalculator.calcPage(rank);
        int index = RankPageCalculator.calcIndex(rank);
        return iocEventRankingManager.fetchScoreRankings(page).get(index);
    }


    @Autowired
    public void setIocEventRankingManager(IEventRankingManager iocEventRankingManager) {
        this.iocEventRankingManager = iocEventRankingManager;
    }
}
