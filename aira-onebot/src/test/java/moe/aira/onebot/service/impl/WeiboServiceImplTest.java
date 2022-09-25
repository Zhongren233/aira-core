package moe.aira.onebot.service.impl;

import moe.aira.onebot.service.WeiboService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WeiboServiceImplTest {
    @Autowired
    WeiboService weiboService;

    @Test
    void sendWeibo() throws IOException, InterruptedException {

        weiboService.sendWeibo("test");
    }
}