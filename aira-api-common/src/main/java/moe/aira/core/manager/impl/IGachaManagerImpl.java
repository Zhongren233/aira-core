package moe.aira.core.manager.impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import moe.aira.core.client.es.GachasClient;
import moe.aira.core.manager.IGachaManager;
import moe.aira.entity.aira.AiraGachaInfo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class IGachaManagerImpl implements IGachaManager {
    final
    GachasClient gachaClient;

    public IGachaManagerImpl(GachasClient gachaClient) {
        this.gachaClient = gachaClient;
    }

    @Cacheable(cacheNames = "currentGacha")
    @Override
    public Set<Integer> currentGacha() {
        List<JsonNode> values = gachaClient.gachas().get("gacha_infos").findValues("gachaId");
        return values.stream().map(JsonNode::asInt).collect(Collectors.toSet());
    }

    @Cacheable(cacheNames = "gachaInfo", key = "#gachaId")
    @Override
    public AiraGachaInfo gachaInfo(Integer gachaId) {
        JsonNode cards = gachaClient.cards(String.valueOf(gachaId));
        JsonNode node = cards.get("pickup_tag_id_card_balls_map");
        HashMap<String, List<Integer>> cardMap = new HashMap<>();
        node.fields().forEachRemaining(stringJsonNodeEntry -> {
            String key = stringJsonNodeEntry.getKey();
            List<Integer> collect = stringJsonNodeEntry.getValue().get("card_balls").findValues("card_id").stream().map(JsonNode::asInt).collect(Collectors.toList());
            cardMap.put(key, collect);
        });
        JsonNode pick = cards.get("gacha").get("pickup_tag_id_ratio_map");
        Map<String, Integer> pickMap = new HashMap<>();
        pick.fields().forEachRemaining(
                stringJsonNodeEntry -> pickMap.put(stringJsonNodeEntry.getKey(), stringJsonNodeEntry.getValue().asInt())
        );
        HashMap<String, Integer> fixedMap = new HashMap<>();
        JsonNode fixed = cards.get("gacha").get("fixed_pickup_tag_id_ratio_map");
        fixed.fields().forEachRemaining(
                stringJsonNodeEntry -> fixedMap.put(stringJsonNodeEntry.getKey(), stringJsonNodeEntry.getValue().asInt())
        );
        AiraGachaInfo airaGachaInfo = new AiraGachaInfo();
        airaGachaInfo.setGachaId(376);
        airaGachaInfo.setCards(cardMap);
        airaGachaInfo.setPickedMap(pickMap);
        airaGachaInfo.setFixedMap(fixedMap);
        return airaGachaInfo;
    }
}
