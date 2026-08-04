package moe.aira.onebot.manager.impl;

import moe.aira.onebot.manager.IAiraObserverManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IAiraObserverManagerImplTest {

    @Autowired
    private IAiraObserverManager iAiraObserverManager;

    @Test
    void getAiraObserverInfo() {
    }

    @Test
    void regAiraObserver() {
    }

    @Test
    void getLastestAiraObserverDetail() {
        System.out.println(1);
        System.out.println(iAiraObserverManager.getLastestAiraObserverDetail(9001));
    }

    @Test
    void getAllAiraObserverDetails() {
    }
}