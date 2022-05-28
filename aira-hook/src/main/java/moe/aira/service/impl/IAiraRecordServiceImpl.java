package moe.aira.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import moe.aira.entity.AiraLive;
import moe.aira.entity.AiraLiveResultDrop;
import moe.aira.mapper.AiraLiveMapper;
import moe.aira.mapper.AiraLiveResultDropMapper;
import moe.aira.service.IAiraRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IAiraRecordServiceImpl implements IAiraRecordService {
    final
    AiraLiveMapper airaLiveMapper;
    @Autowired
    AiraLiveResultDropMapper airaLiveResultDropMapper;

    public IAiraRecordServiceImpl(AiraLiveMapper airaLiveMapper) {
        this.airaLiveMapper = airaLiveMapper;
    }

    @Async
    @Override
    public void recordLiveResult(JsonNode node) {

        Integer liveId = prepareAiraLive(node);
        List<JsonNode> values = node.get("live_result_drops").findValues("content");
        values.forEach(
                value -> {
                    AiraLiveResultDrop airaLiveResultDrop = new AiraLiveResultDrop(
                            value.get("content_type_id").intValue(),
                            value.get("content_id").intValue(),
                            value.get("contents_count").intValue(),
                            value.get("flag").intValue());
                    airaLiveResultDrop.setLiveId(liveId);
                    airaLiveResultDropMapper.insert(airaLiveResultDrop);
                }
        );

    }

    private Integer prepareAiraLive(JsonNode node) {
        JsonNode liveUnit = node.get("user_live_unit");
        int musicId = node.get("musicId").intValue();
        int userId = node.get("user").get("user_id").intValue();

        AiraLive airaLive = new AiraLive();
        airaLive.setUserId(userId);
        airaLive.setMusicId(musicId);
        airaLive.setCard1Id(liveUnit.get("user_card1_id").intValue());
        airaLive.setCard2Id(liveUnit.get("user_card2_id").intValue());
        airaLive.setCard3Id(liveUnit.get("user_card3_id").intValue());
        airaLive.setCard4Id(liveUnit.get("user_card4_id").intValue());
        airaLive.setCard5Id(liveUnit.get("user_card5_id").intValue());

        JsonNode supportUserCards = liveUnit.get("support_user_cards");
        airaLive.setSupportCard1Id(supportUserCards.get(0).get("card_id").intValue());
        airaLive.setSupportCard1Skill(supportUserCards.get(0).get("support_skill_level").intValue());
        airaLive.setSupportCard2Id(supportUserCards.get(1).get("card_id").intValue());
        airaLive.setSupportCard2Skill(supportUserCards.get(1).get("support_skill_level").intValue());
        airaLive.setSppPosition(liveUnit.get("spp_position").intValue());

        airaLive.setBp(node.get("user_music_playing").get("bp").intValue());
        airaLiveMapper.insert(airaLive);
        return airaLive.getId();
    }
}
