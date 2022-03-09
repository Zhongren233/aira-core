package moe.aira.core.client.es;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MyPageClientTest {
    @Autowired
    MyPageClient myPageClient;

    @Test
    void myPage() {
        System.out.println(myPageClient.myPage());
    }
}