package moe.aira.onebot.manager.impl;

import lombok.extern.slf4j.Slf4j;
import moe.aira.entity.aira.AiraGachaInfo;
import moe.aira.entity.api.ApiResult;
import moe.aira.exception.AiraException;
import moe.aira.onebot.client.GachaClient;
import moe.aira.onebot.entity.AiraGachaResultDto;
import moe.aira.onebot.manager.IAiraGachaManager;
import moe.aira.onebot.mapper.AiraConfigMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
public class IAiraGachaManagerImpl implements IAiraGachaManager {

    final GachaClient gachaClient;

    final
    AiraConfigMapper airaConfigMapper;

    public IAiraGachaManagerImpl(GachaClient gachaClient, AiraConfigMapper airaConfigMapper) {
        this.gachaClient = gachaClient;
        this.airaConfigMapper = airaConfigMapper;
    }

    @Cacheable(cacheNames = "gachaInfo", key = "#gachaId")
    @Override
    public AiraGachaInfo gachaInfo(Integer gachaId) {
        ApiResult<AiraGachaInfo> apiResult = gachaClient.poolInfo(gachaId);
        if (apiResult.getCode() == 0) {
            return apiResult.getData();
        }
        throw new AiraException("接口错误:\n" + apiResult.getMessage());
    }

    @Cacheable(cacheNames = "currentGacha")
    @Override
    public Set<Integer> currentGachaList() {
        ApiResult<Set<Integer>> apiResult = gachaClient.gachaPool();
        if (apiResult.getCode() == 0) {
            return apiResult.getData();
        }
        throw new AiraException("接口错误:\n" + apiResult.getMessage());
    }

    @Override
    public AiraGachaResultDto gacha(AiraGachaInfo gachaInfo, Integer count) {
        long l = System.currentTimeMillis();
        AiraGachaResultDto gachaResultDto = new AiraGachaResultDto();
        gachaResultDto.setCardIds(new ArrayList<>(count));
        gachaResultDto.setGachaPoolId(gachaInfo.getGachaId());
        Map<String, Integer> pickedMap = parseGachaMap(gachaInfo.getPickedMap());
        Map<String, Integer> fixedMap = parseGachaMap(gachaInfo.getFixedMap());
        // 固定抽取
        gachaResultDto.setType(AiraGachaResultDto.ResultType.NORMAL);/**/
        for (int i = 0; i < count - 1; i++) {
            Map.Entry<String, Integer> e = signalGacha(gachaInfo.getCards(), pickedMap);
            gachaResultDto.getCardIds().add(e);
            if (e.getKey().contains("5")) {
                gachaResultDto.setType(AiraGachaResultDto.ResultType.RAINBOW);
            }
            if (e.getKey().contains("4") && gachaResultDto.getType() == AiraGachaResultDto.ResultType.NORMAL) {
                gachaResultDto.setType(AiraGachaResultDto.ResultType.GOLDEN);
            }
        }
        Map.Entry<String, Integer> lastGacha;
        if (gachaResultDto.getType() == AiraGachaResultDto.ResultType.NORMAL) {
            lastGacha = signalGacha(gachaInfo.getCards(), fixedMap);
            if (lastGacha.getKey().contains("4")) {
                gachaResultDto.setType(AiraGachaResultDto.ResultType.GOLDEN);
            }
        } else if (count < 10) {
            lastGacha = signalGacha(gachaInfo.getCards(), pickedMap);
            String key = lastGacha.getKey();
            if (key.contains("4")) {
                gachaResultDto.setType(AiraGachaResultDto.ResultType.GOLDEN);
            }
            if (key.contains("3")) {
                gachaResultDto.setType(AiraGachaResultDto.ResultType.NORMAL);
            }
        } else {
            lastGacha = signalGacha(gachaInfo.getCards(), pickedMap);
        }
        if (lastGacha.getKey().contains("5")) {
            gachaResultDto.setType(AiraGachaResultDto.ResultType.RAINBOW);
        }
        gachaResultDto.getCardIds().add(lastGacha);
        log.info("抽卡耗时: {}ms", System.currentTimeMillis() - l);
        return gachaResultDto;
    }

    private Map.Entry<String, Integer> signalGacha(Map<String, List<Integer>> cards, Map<String, Integer> pickedMap) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Integer max = pickedMap.values().stream().max(Integer::compareTo).orElseThrow(() -> new AiraException("意外错误"));
        int randomSeed = random.nextInt(0, max + 1);
        String pool = pickedMap.entrySet().stream().filter(stringIntegerEntry -> stringIntegerEntry.getValue() >= randomSeed)
                .min(Map.Entry.comparingByValue()).orElseThrow(() -> new AiraException("意外错误：seed=" + randomSeed)).getKey();
        List<Integer> cardsList = cards.get(pool);
        int index = ThreadLocalRandom.current().nextInt(cardsList.size());

        return Map.entry(pool, cardsList.get(index));
    }

    private Map<String, Integer> parseGachaMap(Map<String, Integer> gachaMap) {
        Map<String, Integer> pickedMap = new HashMap<>();
        int currentValue = 0;
        for (Map.Entry<String, Integer> entry : gachaMap.entrySet()) {
            Integer value = entry.getValue();
            if (value == 0) {
                continue;
            }
            pickedMap.put(entry.getKey(), value + currentValue);
            currentValue += value;
        }
        return pickedMap;
    }

    @Override
    @Cacheable(cacheNames = "currentGachaConfig")
    public Integer currentGacha() {
        String s = airaConfigMapper.selectConfigValueByConfigKey("CURRENT_GACHA_ID");
        Set<Integer> integers = currentGachaList();
        Integer gachaId = null;
        if (s != null) {
            Integer o = Integer.valueOf(s);
            if (integers.contains(o)) {
                gachaId = o;
            }
        }
        if (gachaId == null) {
            gachaId = integers.stream().filter(integer -> integer < 1000).max(Integer::compareTo).orElse(303);
            log.info("没有设置当前抽卡池，自动设置为{}", gachaId);
            airaConfigMapper.updateConfigValueByConfigKey("CURRENT_GACHA_ID", gachaId.toString());
        }
        return gachaId;
    }
}
