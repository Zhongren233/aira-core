package moe.aira.core.biz.impl;

import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.core.biz.IAiraTricolorBiz;
import moe.aira.core.client.es.TriColorMatsuriClient;
import moe.aira.core.service.IAiraTricolorLogService;
import moe.aira.entity.aira.AiraTriColorMatsuriInfo;
import moe.aira.entity.aira.AiraTricolorLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class IAiraTricolorBizImpl implements IAiraTricolorBiz {
    final
    TriColorMatsuriClient triColorMatsuriClient;

    public IAiraTricolorBizImpl(TriColorMatsuriClient triColorMatsuriClient) {
        this.triColorMatsuriClient = triColorMatsuriClient;
    }

    @Override
    public AiraTriColorMatsuriInfo fetchInfo() {
        JsonNode node = triColorMatsuriClient.tricolorFestival2023(false);
        JsonNode node1 = node.get("within_period_period_ranking");
        JsonNode battleRankings = node1.get("battle_rankings");
        AiraTriColorMatsuriInfo airaTriColorMatsuriInfo = new AiraTriColorMatsuriInfo();
        airaTriColorMatsuriInfo.setPeriodId(node1.get("tricolor_festival2023_period_id").asInt());
        ArrayList<AiraTriColorMatsuriInfo.AiraTriColorBattleInfo> battleInfos = new ArrayList<>();
        airaTriColorMatsuriInfo.setBattleInfos(battleInfos);
        for (JsonNode battleRanking : battleRankings) {
            battleRanking.get("team_battle_scores").forEach(a -> {
                AiraTriColorMatsuriInfo.AiraTriColorBattleInfo airaTriColorBattleInfo = new AiraTriColorMatsuriInfo.AiraTriColorBattleInfo();
                airaTriColorBattleInfo.setBattleId(a.get("tricolor_festival2023_battle_id").asInt());
                airaTriColorBattleInfo.setTeamId(a.get("tricolor_festival2023_team_id").asInt());
                airaTriColorBattleInfo.setScore(a.get("score").asInt());
                battleInfos.add(airaTriColorBattleInfo);
            });
        }
        return airaTriColorMatsuriInfo;
    }

    @Autowired
    IAiraTricolorLogService airaTricolorLogService;
    @Override
    public boolean saveLog(List<AiraTricolorLog> logs) {
        return airaTricolorLogService.saveBatch(logs);
    }
}
