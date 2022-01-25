package moe.aira.core.biz.impl;

import moe.aira.core.biz.IAiraEventRankingBiz;
import moe.aira.core.entity.aira.AiraEventRanking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IAiraEventRankingBizImplTest {
    @Autowired
    IAiraEventRankingBiz eventRankingBiz;

    @Test
    void fetchAiraEventRanking() {
        AiraEventRanking airaEventRanking = eventRankingBiz.fetchAiraEventRanking(70000196);
        System.out.println(airaEventRanking);
    }
}