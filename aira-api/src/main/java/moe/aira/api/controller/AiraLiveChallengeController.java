package moe.aira.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import moe.aira.core.client.es.LiveChallengesClient;
import moe.aira.core.dao.AiraConfigMapper;
import moe.aira.entity.aira.AiraLiveChallengeInfo;
import moe.aira.entity.api.ApiResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;
import java.util.Map;

import static moe.aira.util.RankPageCalculator.calcPage;

@Controller
public class AiraLiveChallengeController {
    final
    AiraConfigMapper configMapper;
    private final LiveChallengesClient client;

    public AiraLiveChallengeController(LiveChallengesClient client, AiraConfigMapper configMapper) {
        this.client = client;
        this.configMapper = configMapper;
    }

    @ResponseBody
    @GetMapping("/liveChallenge/info")
    public ApiResult<AiraLiveChallengeInfo> info() {
        JsonNode ranking = client.ranking(client.ranking(1).get("total_pages").intValue());
        ArrayNode jsonNodes = (ArrayNode) ranking.get("ranking");
        int availableRank = jsonNodes.get(jsonNodes.size() - 1).get("rank").intValue();
        int platRank = (int) (availableRank * 0.005);
        int goldRank = (int) (availableRank * 0.02);
        int sliverRank = (int) (availableRank * 0.2);
        int bronzeRank = (int) (availableRank * 0.5);
        Integer liveChallengeMaxScore = Integer.valueOf(configMapper.selectConfigValueByConfigKey("live_challenge_max_score").orElse("0"));
        int liveChallengeLevel = Integer.parseInt(configMapper.selectConfigValueByConfigKey("live_challenge_level").orElse("2"));
        AiraLiveChallengeInfo airaLiveChallengeInfo = new AiraLiveChallengeInfo();
        switch (liveChallengeLevel) {
            case 4:
                airaLiveChallengeInfo.setBronzeRank(new AiraLiveChallengeInfo.AiraLiveChallengeDetail(findRank(bronzeRank), liveChallengeMaxScore));
            case 3:
                airaLiveChallengeInfo.setSliverRank(new AiraLiveChallengeInfo.AiraLiveChallengeDetail(findRank(sliverRank), liveChallengeMaxScore));
            case 2:
                airaLiveChallengeInfo.setGoldRank(new AiraLiveChallengeInfo.AiraLiveChallengeDetail(findRank(goldRank), liveChallengeMaxScore));
            case 1:
                airaLiveChallengeInfo.setPlatRank(new AiraLiveChallengeInfo.AiraLiveChallengeDetail(findRank(platRank), liveChallengeMaxScore));
        }
        System.out.println(airaLiveChallengeInfo);
        return ApiResult.success(airaLiveChallengeInfo);
    }

    private Map.Entry<Integer, Integer> findRank(int platRank) {
        ArrayNode nodes = (ArrayNode) client.ranking(calcPage(platRank)).get("ranking");
        Iterator<JsonNode> elements = nodes.elements();
        int prevRank = 0;
        int preScore = 0;
        while (elements.hasNext()) {
            JsonNode next = elements.next();
            int rank = next.get("rank").intValue();
            int point = next.get("point").intValue();
            if (rank > platRank) {
                break;
            }
            prevRank = rank;
            preScore = point;
        }
        return Map.entry(prevRank, preScore);
    }
}
