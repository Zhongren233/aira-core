package moe.aira.core.biz.impl;

import moe.aira.core.biz.IAiraEventBiz;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IAiraEventBizImplTest {
    @Autowired
    IAiraEventBiz eventBiz;

    @Test
    void countEventPointBatch() {
    }

    @Test
    void testCountEventPointBatch() {
    }

    @Test
    void fetchCurrentRankPoint() {
    }

    @Test
    void testFetchCurrentRankPoint() {
        System.out.println(eventBiz.fetchCurrentRankPoint());
    }
}