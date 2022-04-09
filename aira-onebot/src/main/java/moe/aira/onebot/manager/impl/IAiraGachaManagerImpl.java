package moe.aira.onebot.manager.impl;

import moe.aira.entity.aira.AiraGachaInfo;
import moe.aira.entity.api.ApiResult;
import moe.aira.exception.AiraException;
import moe.aira.onebot.client.GachaClient;
import moe.aira.onebot.entity.AiraGachaResultDto;
import moe.aira.onebot.manager.IAiraGachaManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class IAiraGachaManagerImpl implements IAiraGachaManager {

    final GachaClient gachaClient;

    public IAiraGachaManagerImpl(GachaClient gachaClient) {
        this.gachaClient = gachaClient;
    }

    @Cacheable(cacheNames = "currentGacha")
    @Override
    public Set<Integer> currentGacha() {
        ApiResult<Set<Integer>> apiResult = gachaClient.gachaPool();
        if (apiResult.getCode() == 0) {
            return apiResult.getData();
        }
        throw new AiraException("接口错误:\n" + apiResult.getMessage());
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

    @Override
    public AiraGachaResultDto gacha(AiraGachaInfo gachaInfo, Integer count) {
        AiraGachaResultDto gachaResultDto = new AiraGachaResultDto();
        gachaResultDto.setCardIds(new ArrayList<>(count));
        gachaResultDto.setGachaPoolId(gachaInfo.getGachaId());
        Map<String, Integer> pickedMap = parseGachaMap(gachaInfo.getPickedMap());
        Map<String, Integer> fixedMap = parseGachaMap(gachaInfo.getFixedMap());
        // 固定抽取
        for (int i = 0; i < count - 1; i++) {
            Map.Entry<String, Integer> e = signalGacha(gachaInfo.getCards(), pickedMap);
            gachaResultDto.getCardIds().add(e);
            if (e.getKey().contains("5")) {
                gachaResultDto.setType(AiraGachaResultDto.ResultType.RAINBOW);
            }
        }
        Map.Entry<String, Integer> lastGacha;
        if (gachaResultDto.getType() == AiraGachaResultDto.ResultType.RAINBOW || count < 10) {
            lastGacha = signalGacha(gachaInfo.getCards(), pickedMap);
        } else {
            lastGacha = signalGacha(gachaInfo.getCards(), fixedMap);
        }
        if (lastGacha.getKey().contains("5")) {
            gachaResultDto.setType(AiraGachaResultDto.ResultType.RAINBOW);
        }
        gachaResultDto.getCardIds().add(lastGacha);
        return gachaResultDto;
    }

    private Map.Entry<String, Integer> signalGacha(Map<String, List<Integer>> cards, Map<String, Integer> pickedMap) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Integer max = pickedMap.values().stream().max(Integer::compareTo).orElseThrow(() -> new AiraException("意外错误"));
        int randomSeed = random.nextInt(0, max + 1);
        String pool = pickedMap.entrySet().stream().filter(stringIntegerEntry -> stringIntegerEntry.getValue() <= randomSeed).min(Map.Entry.comparingByValue()).orElseThrow(() -> new AiraException("意外错误")).getKey();

        List<Integer> cardsList = cards.get(pool);
        int index = ThreadLocalRandom.current().nextInt(cardsList.size());

        return Map.entry(pool, index);
    }

    private Map<String, Integer> parseGachaMap(Map<String, Integer> gachaMap) {
        Map<String, Integer> pickedMap = new HashMap<>();
        int currentValue = 0;
        for (Map.Entry<String, Integer> entry : gachaMap.entrySet()) {
            pickedMap.put(entry.getKey(), entry.getValue() + currentValue);
            currentValue += entry.getValue();
        }
        return pickedMap;
    }


}
