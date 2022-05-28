package moe.aira.core.manager.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxl.job.core.context.XxlJobHelper;
import lombok.extern.slf4j.Slf4j;
import moe.aira.annotation.EventAvailable;
import moe.aira.annotation.RecordToDataBase;
import moe.aira.core.client.es.PointRankingClient;
import moe.aira.core.client.es.ScoreRankingClient;
import moe.aira.core.entity.dto.UserRanking;
import moe.aira.core.manager.IEventRankingManager;
import moe.aira.core.util.CryptoUtils;
import moe.aira.entity.es.PointRanking;
import moe.aira.entity.es.ScoreRanking;
import moe.aira.util.INodeParser;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.springframework.beans.factory.annotation.Qualifier;
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
    INodeParser eventRankingParser;
    final
    ObjectMapper objectMapper;
    final
    CryptoUtils cryptoUtils;
    final
    CloseableHttpAsyncClient closeableHttpAsyncClient;

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
    @EventAvailable
    public List<UserRanking<PointRanking>> fetchPointRankings(Integer page) {
        JsonNode node = pointRankingClient.page(page);
        return eventRankingParser.parseToUserRankings(node, PointRanking.class);
    }

    @Override
    @EventAvailable
    @RecordToDataBase
    public List<UserRanking<ScoreRanking>> fetchScoreRankings(Integer page) {
        JsonNode node = scoreRankingClient.page(page);
        return eventRankingParser.parseToUserRankings(node, ScoreRanking.class);
    }

    private final ObjectMapper messagePackMapper;

    public IEventRankingManagerImpl(PointRankingClient pointRankingClient,
                                    ScoreRankingClient scoreRankingClient, INodeParser eventRankingParser, ObjectMapper objectMapper, @Qualifier("messagePackMapper") ObjectMapper messagePackMapper, CryptoUtils cryptoUtils, CloseableHttpAsyncClient closeableHttpAsyncClient) {
        this.pointRankingClient = pointRankingClient;
        this.scoreRankingClient = scoreRankingClient;
        this.eventRankingParser = eventRankingParser;
        this.objectMapper = objectMapper;
        this.messagePackMapper = messagePackMapper;
        this.cryptoUtils = cryptoUtils;
        this.closeableHttpAsyncClient = closeableHttpAsyncClient;
    }

    @Override
    public CompletableFuture<List<UserRanking<PointRanking>>> fetchPointRankingsAsync(Integer page) {
        CompletableFuture<List<UserRanking<PointRanking>>> completableFuture = new CompletableFuture<>();
        pointRankingClient.asyncPage(page,
                (data, req, res) ->
                        completableFuture.complete(eventRankingParser.parseToUserRankings(data, PointRanking.class)),
                (ex, req, res) -> {
                    completableFuture.complete(null);
                });
        return completableFuture;
    }

    @Override
    public CompletableFuture<List<UserRanking<ScoreRanking>>> fetchScoreRankingsAsync(Integer page) {
        CompletableFuture<List<UserRanking<ScoreRanking>>> completableFuture = new CompletableFuture<>();
        scoreRankingClient.asyncPage(page,
                (data, req, res) ->
                        completableFuture.complete(eventRankingParser.parseToUserRankings(data, ScoreRanking.class)),
                (ex, req, res) -> {
                    log.error("on error in {}", req);
                    XxlJobHelper.log("skip {} point ranking request", req);
                    completableFuture.complete(null);
                });
        return completableFuture;
    }


}
