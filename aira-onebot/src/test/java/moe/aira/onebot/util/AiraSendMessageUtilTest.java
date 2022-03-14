package moe.aira.onebot.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;

class AiraSendMessageUtilTest {

    @Test
    void parseStringToBase64Image() throws IOException {
        AiraSendMessageUtil.parseStringToBase64Image("""
                昵称:这里理论要塞下十个字
                活动点数:10,480,129,480
                活动点数排名:150,358
                歌曲点数:180,129,480
                歌曲点数排名:115,002
                """);
    }
}