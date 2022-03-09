package moe.aira.core.service.impl;

import moe.aira.core.service.IFriendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class IFriendServiceImplTest {
    @Autowired
    IFriendService friendService;

    @Test
    void fetchFriendSearchList() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.execute(() -> System.out.println(friendService.fetchFriendSearchList("mika")));
        executorService.execute(() -> System.out.println(friendService.fetchFriendSearchList("宗")));
        executorService.execute(() -> System.out.println(friendService.fetchFriendSearchList("小杏")));
        executorService.execute(() -> System.out.println(friendService.fetchFriendSearchList("泉")));
        executorService.execute(() -> System.out.println(friendService.fetchFriendSearchList("雪")));
        Thread.sleep(50000);

    }
}