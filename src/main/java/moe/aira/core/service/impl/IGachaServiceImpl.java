package moe.aira.core.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import moe.aira.core.client.es.GachasClient;
import moe.aira.core.entity.dto.GachaInfo;
import moe.aira.core.entity.dto.GachaPool;
import moe.aira.core.service.IGachaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.util.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.SplittableRandom;

@Service
@Slf4j
public class IGachaServiceImpl implements IGachaService {
    final
    GachasClient gachasClient;
    @Autowired
    ObjectMapper mapper;


    public IGachaServiceImpl(GachasClient gachasClient) {
        this.gachasClient = gachasClient;
    }

    @Override
    @Cacheable(value = "currentGacha")
    public List<String> fetchCurrentGacha() {
        log.info("获取卡池.....");
        JsonNode gachas = gachasClient.gachas();
        JsonNode gachaInfos = gachas.get("gacha_infos");
        return gachaInfos.findValuesAsText("gachaId");
    }

    @Override
    @Cacheable(value = "gachaPool",key = "#gachaId")
    public GachaPool fetchGachaPool(String gachaId) {
        log.info("获取卡池深度.....");
        GachaPool gachaPool = new GachaPool();
        JsonNode cards = gachasClient.cards(gachaId);
        JsonNode pickupTagIdCardBallsMap = cards.get("pickup_tag_id_card_balls_map");
        Iterator<String> stringIterator = pickupTagIdCardBallsMap.fieldNames();
        while (stringIterator.hasNext()) {
            String next = stringIterator.next();
            JsonNode cardBalls = pickupTagIdCardBallsMap.get(next).get("card_balls");
            List<String> cardId = cardBalls.findValuesAsText("card_id");
            gachaPool.getPickupTagIdCardBallsMap().put(next, cardId);
        }

        JsonNode fixedPickupTagIdCardBallsMap = cards.get("fixed_pickup_tag_id_card_balls_map");
        Iterator<String> iterator = fixedPickupTagIdCardBallsMap.fieldNames();
        while (iterator.hasNext()) {
            String next = iterator.next();
            JsonNode cardBalls = pickupTagIdCardBallsMap.get(next).get("card_balls");
            List<String> cardId = cardBalls.findValuesAsText("card_id");
            gachaPool.getFixedPickupTagIdCardBallsMap().put(next, cardId);
        }
        return gachaPool;
    }

    @Override
    @Cacheable(value = "gachaProbability",key = "#gachaId")
    public GachaInfo fetchGachaProbability(String gachaId) {
        JsonNode gachas = gachasClient.gachas();
        JsonNode gachaInfos = gachas.get("gacha_infos");
        ArrayList<GachaInfo> gachaInfos1 = mapper.convertValue(gachaInfos, new TypeReference<ArrayList<GachaInfo>>() {
        });
        Optional<GachaInfo> first = gachaInfos1.stream().filter(gachaInfo -> gachaInfo.getGachaId().equals(gachaId)).findFirst();
        return first.orElseThrow(() -> new NoSuchElementException("没有对应的卡池ID!"));
    }


}
