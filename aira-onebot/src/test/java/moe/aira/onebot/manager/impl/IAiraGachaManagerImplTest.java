package moe.aira.onebot.manager.impl;

import moe.aira.entity.aira.AiraGachaInfo;
import moe.aira.onebot.entity.AiraGachaResultDto;
import moe.aira.onebot.manager.IAiraGachaManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IAiraGachaManagerImplTest {
    @Autowired
    IAiraGachaManager airaGachaManager;

    @Test
    void currentGacha() {
    }

    @Test
    void gachaInfo() {
    }

    @Test
    void gacha() {
        Set<Integer> integers = airaGachaManager.currentGachaList();
        System.out.println(integers);
        AiraGachaInfo airaGachaInfo = airaGachaManager.gachaInfo(376);
        System.out.println(airaGachaInfo);
        ArrayList<Map.Entry<String, Integer>> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            AiraGachaResultDto gacha = airaGachaManager.gacha(airaGachaInfo, 10);
            list.addAll(gacha.getCardIds());
        }

        list.stream().collect(Collectors.groupingBy((Function<Map.Entry<String, Integer>, Object>) Map.Entry::getKey, Collectors.counting())).forEach((k, v) -> System.out.println(k + ":" + v));

    }
}