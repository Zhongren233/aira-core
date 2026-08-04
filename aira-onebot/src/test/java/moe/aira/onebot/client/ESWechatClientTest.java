package moe.aira.onebot.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ESWechatClientTest {
    @Autowired
    ESWechatClient esWechatClient;
    @Test
    void getMonthCardInfo() {
        System.out.println("esWechatClient.getMonthCardInfo(\"tttt\") = " + esWechatClient.getMonthCardInfo("tttt"));

    }


    @Test
    void getMonthCardReward() {
        System.out.println("esWechatClient.getMonthCardReward(\"tttt\") = " + esWechatClient.getMonthCardReward("tttt","1"));

    }
}