package moe.aira.core.service.impl;

import moe.aira.core.service.IGachaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.SplittableRandom;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class IGachaServiceImplTest {
    @Autowired
    IGachaService gachaService;

    @Test
    void fetchCurrentGacha() {
        System.out.println("gachaService.fetchCurrentGacha() = " + gachaService.fetchCurrentGacha());
    }

    @Test
    void fetchGachaPool() {
        System.out.println("gachaService.fetchGachaPool(\"5000014\") = " + gachaService.fetchGachaPool("5000014"));
    }

    @Test
    void fetchGachaProbability() {
        System.out.println(gachaService.fetchGachaProbability("5000014"));
    }

    @Test
    void gacha() {

    }

    @Test
    void test() {
        SplittableRandom splittableRandom = new SplittableRandom();
        HashMap<Integer, Integer> integerIntegerHashMap = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            int x = splittableRandom.nextInt(1, 101);
            Integer integer = integerIntegerHashMap.get(x);
            if (integer != null) {
                integer++;
                integerIntegerHashMap.put(x, integer);
            } else {
                integerIntegerHashMap.put(x, 1);
            }
        }
        System.out.println(integerIntegerHashMap);
    }

}