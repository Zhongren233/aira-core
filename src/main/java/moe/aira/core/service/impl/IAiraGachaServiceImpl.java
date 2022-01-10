package moe.aira.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import moe.aira.core.entity.aira.GachaRecord;
import moe.aira.core.entity.aira.GachaResult;
import moe.aira.core.entity.dto.GachaInfo;
import moe.aira.core.entity.dto.GachaPool;
import moe.aira.core.dao.AiraGachaRecordMapper;
import moe.aira.core.dao.AiraGachaResultMapper;
import moe.aira.core.dao.AiraUserCardMapper;
import moe.aira.core.service.IAiraGachaService;
import moe.aira.core.service.IGachaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SplittableRandom;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IAiraGachaServiceImpl implements IAiraGachaService {

    private final Lazy<SplittableRandom> splittableRandomLazy = Lazy.of(SplittableRandom::new);
    final
    IGachaService gachaService;
    @Autowired
    AiraGachaRecordMapper airaGachaRecordMapper;
    @Autowired
    AiraUserCardMapper airaUserCardMapper;
    @Autowired
    AiraGachaResultMapper airaGachaResultMapper;
    public IAiraGachaServiceImpl(IGachaService gachaService) {
        this.gachaService = gachaService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<GachaResult> gachaTenCount(String gachaId, Long qqNumber) {
        if (gachaId == null || gachaId.isEmpty()) {
            List<String> strings = gachaService.fetchCurrentGacha();
            gachaId = strings.get(0);
        }
        gachaId = gachaId.trim();
        List<Integer> gacha = gacha(gachaId);
        Date date = new Date();
        String finalGachaId = gachaId;
        List<GachaRecord> gachaRecords = gacha.stream().map(
                cardId -> {
                    GachaRecord gachaRecord = new GachaRecord();
                    gachaRecord.setQqNumber(qqNumber);
                    gachaRecord.setQuickHash((int) (System.currentTimeMillis() % 1_000_000));
                    gachaRecord.setCreateTime(date);
                    gachaRecord.setCardId(cardId);
                    gachaRecord.setGachaId(finalGachaId);
                    return gachaRecord;
                }
        ).collect(Collectors.toList());
        gachaRecords.forEach(gachaRecord -> airaGachaRecordMapper.insert(gachaRecord));
        List<GachaResult> gachaResults = new ArrayList<>();
        for (GachaRecord gachaRecord : gachaRecords) {
            airaUserCardMapper.insertOrUpdateUserCardByGachaRecord(gachaRecord);
            QueryWrapper<GachaResult> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("qq_number", gachaRecord.getQqNumber());
            queryWrapper.eq("card_id", gachaRecord.getCardId());
            GachaResult gachaResult = airaGachaResultMapper.selectOne(queryWrapper);
            gachaResults.add(gachaResult);
        }
        return gachaResults;
    }

    private List<Integer> gacha(String gachaId) {
        SplittableRandom splittableRandom = splittableRandomLazy.get();
        HashMap<String, Integer> levelResultMap = randomCardLevel(gachaId, splittableRandom);
        System.out.println("level=" + levelResultMap);
        return randomCard(gachaId, splittableRandom, levelResultMap);
    }


    private String doSingleGacha(SplittableRandom splittableRandom, Map<String, Integer> probabilityMap) {
        int pro = splittableRandom.nextInt(1, 101);
        Integer integer = probabilityMap.values().stream()
                .filter(probi -> probi != 0)
                .filter(probi -> probi >= pro)
                .min(Integer::compareTo)
                .orElseThrow(RuntimeException::new);
        for (Map.Entry<String, Integer> entry : probabilityMap.entrySet()) {
            if (entry.getValue().equals(integer)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private HashMap<String, Integer> randomCardLevel(String gachaId, SplittableRandom splittableRandom) {
        GachaInfo gachaInfo = gachaService.fetchGachaProbability(gachaId);
        Map<String, Integer> pickupTagIdRatioMap = gachaInfo.getPickupTagIdRatioMap();
        Map<String, Integer> fixPickupTagIdRatioMap = gachaInfo.getFixPickupTagIdRatioMap();
        initMap(pickupTagIdRatioMap);
        initMap(fixPickupTagIdRatioMap);
        HashMap<String, Integer> levelResultMap = new HashMap<>();
        for (int i = 0; i < 9; i++) {
            putGachaLevelResult(levelResultMap, doSingleGacha(splittableRandom, pickupTagIdRatioMap));
        }
        //万恶的保底机制
        if (levelResultMap.get("5") == null && levelResultMap.get("105") == null && levelResultMap.get("4") == null && levelResultMap.get("104") == null) {
            putGachaLevelResult(levelResultMap, doSingleGacha(splittableRandom, fixPickupTagIdRatioMap));
        } else {
            log.info("触发保底");
            putGachaLevelResult(levelResultMap, doSingleGacha(splittableRandom, pickupTagIdRatioMap));
        }
        return levelResultMap;
    }

    private void initMap(Map<String, Integer> fixPickupTagIdRatioMap) {
        int tmp = 0;
        for (Map.Entry<String, Integer> stringIntegerEntry : fixPickupTagIdRatioMap.entrySet()) {
            tmp += stringIntegerEntry.getValue();
            stringIntegerEntry.setValue(tmp);
        }
    }

    private ArrayList<Integer> randomCard(String gachaId, SplittableRandom splittableRandom, HashMap<String, Integer> levelResultMap) {
        GachaPool gachaPool = gachaService.fetchGachaPool(gachaId);
        Map<String, List<String>> pickupTagIdCardBallsMap = gachaPool.getPickupTagIdCardBallsMap();
        ArrayList<Integer> gachaResult = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : levelResultMap.entrySet()) {
            List<String> strings = pickupTagIdCardBallsMap.get(entry.getKey());
            int size = strings.size();
            for (int i = 0; i < entry.getValue(); i++) {
                String cardId = strings.get(splittableRandom.nextInt(0, size));
                gachaResult.add(Integer.valueOf(cardId));
            }
        }
        Collections.shuffle(gachaResult);
        return gachaResult;
    }

    private void putGachaLevelResult(HashMap<String, Integer> levelResultMap, String gachaLevel) {
        Integer value = levelResultMap.getOrDefault(gachaLevel, 0);
        value++;
        levelResultMap.put(gachaLevel, value);
    }

}
