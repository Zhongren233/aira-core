package moe.aira.core.biz.impl;

import moe.aira.core.biz.IAiraUserBiz;
import moe.aira.entity.aira.AiraEventRanking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IAiraEventRankingBizImplTest {
    @Autowired
    IAiraUserBiz eventRankingBiz;

    @Test
    void fetchAiraEventRanking() {
        AiraEventRanking airaEventRanking = eventRankingBiz.fetchAiraEventRanking(70000196);
        System.out.println(airaEventRanking);
    }
}