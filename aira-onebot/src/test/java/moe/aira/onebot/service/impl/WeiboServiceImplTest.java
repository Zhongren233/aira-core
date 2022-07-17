package moe.aira.onebot.service.impl;

import moe.aira.onebot.service.WeiboService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WeiboServiceImplTest {
    @Autowired
    WeiboService weiboService;

    @Test
    void sendWeibo() throws IOException, InterruptedException {
        File[] files = new File[2];
        files[0] = new File("D:\\hide\\aira-core\\aira-onebot\\src\\main\\resources\\image\\template\\report\\243_RED.png");
        files[1] = new File("D:\\hide\\aira-core\\aira-onebot\\src\\main\\resources\\image\\template\\report\\243_WIHTE.png");
        weiboService.sendWeibo("測試一下", files);
    }
}