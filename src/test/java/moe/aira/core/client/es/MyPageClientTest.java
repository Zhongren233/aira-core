package moe.aira.core.client.es;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MyPageClientTest {
    @Autowired
    MyPageClient myPageClient;

    @Test
    void myPage() {
        System.out.println(myPageClient.myPage());
    }
}