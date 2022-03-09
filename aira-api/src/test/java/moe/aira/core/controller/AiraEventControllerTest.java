package moe.aira.core.controller;

import moe.aira.entity.api.ApiResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class AiraEventControllerTest {
    @Autowired
    AiraEventController eventController;

    @Test
    void countEventPoint() {
        System.out.println(eventController.countEventPoint(new Integer[]{200000000}));
    }

    @Test
    void countEventPointBatch() {
        ApiResult<Map<Integer, Integer>> mapApiResult = eventController.countEventPoint();
        System.out.println(mapApiResult);
    }
}