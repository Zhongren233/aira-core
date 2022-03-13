package moe.aira.onebot.plugin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BindPluginTest {
    @Autowired
    BindPlugin bindPlugin;

    @Test
    void createRandomString() {
        for (int i = 0; i < 100; i++) {
            String randomString = bindPlugin.createRandomString();
            System.out.println(randomString);
        }
    }
}