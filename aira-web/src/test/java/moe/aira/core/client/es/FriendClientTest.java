package moe.aira.core.client.es;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FriendClientTest {
    @Autowired
    FriendClient friendClient;
    @Test
    void friendList() {
        System.out.println("friendClient = " + friendClient);
        System.out.println(friendClient.friendList("雪泉"));
    }
}