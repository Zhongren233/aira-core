package moe.aira.core;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import moe.aira.core.biz.IAiraUserBiz;
import moe.aira.core.dao.CardMapper;
import moe.aira.core.entity.aira.Card;
import moe.aira.entity.aira.AiraEventRanking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AiraCoreApplicationTests {
    @Autowired
    CardMapper cardMapper;
    @Autowired
    IAiraUserBiz biz;

    @Test
    void contextLoads() throws IOException {
        File file = new File("D:\\hide\\aira-core\\.idea\\httpRequests\\2.json");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(file);
        List<JsonNode> values = jsonNode.get("rows").findValues("data");

        for (JsonNode value : values) {
            String cardName = value.get("1").get("value").textValue().substring(1).split("］")[0];
            String sppName = value.get("31").get("value").textValue();
            Card card = new Card();
            card.setSppName(sppName);
            QueryWrapper<Card> updateWrapper = new QueryWrapper<>();
            updateWrapper.eq("card_name_jp", cardName);
            int update = cardMapper.update(card, updateWrapper);
            if (update == 0) {
                System.out.println(cardName + ":" + sppName);
            }
        }
    }

    @Test
    void testHash() {
        AiraEventRanking airaEventRanking = biz.fetchAiraEventRanking(null);
    }

}
