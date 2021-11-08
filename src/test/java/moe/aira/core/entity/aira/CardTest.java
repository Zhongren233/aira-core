package moe.aira.core.entity.aira;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import moe.aira.core.mapper.CardMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
@SpringBootTest
class CardTest {
    @Autowired
    CardMapper cardMapper;
    @Test
    void test() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("C:\\Users\\sc\\Desktop\\card");
        for (File listFile : Objects.requireNonNull(file.listFiles())) {
            Card card = new Card();
            JsonNode jsonNode = objectMapper.readTree(listFile);
            int cardId = jsonNode.get("编号").intValue();
            card.setCardId(cardId);

            byte rarity = (byte) jsonNode.get("稀有度").intValue();
            card.setRarity(rarity);

            String idolName = jsonNode.get("偶像名").textValue();
            card.setIdolName(idolName);

            String cardNameJp = jsonNode.get("卡名").get("日").textValue();
            String cardNameCn = jsonNode.get("卡名").get("中").textValue();
            card.setCardNameJp(cardNameJp);
            card.setCardNameCn(cardNameCn);

            JsonNode 卡池 = jsonNode.get("卡池");
            if (卡池 != null) {
                String poolName = 卡池.get("名称").get("日").textValue();
                JsonNode node = 卡池.get("名称").get("中");
                if (node != null) {
                    poolName = node.textValue();
                }
                card.setCardPoolName(poolName);
            }

            JsonNode 颜色 = jsonNode.get("颜色");
            if (颜色 != null) {
                String color = 颜色.textValue();
                card.setCardColor(color);

            }

            JsonNode 特化 = jsonNode.get("特化");
            if (特化 != null) {
                String attr = 特化.textValue();
                card.setCardAttr(attr);
            }
            if (card.getIdolName().length()>5) {
                System.out.println(card.getIdolName());
            }
            cardMapper.insert(card);


        }
    }
}