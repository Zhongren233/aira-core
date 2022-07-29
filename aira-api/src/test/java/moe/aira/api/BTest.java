package moe.aira.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import moe.aira.core.dao.AiraCardMapper;
import moe.aira.entity.aira.Card;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@SpringBootTest
public class BTest {
    @Autowired
    AiraCardMapper cardMapper;

    @Test
    void name() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("C:\\Users\\sc\\Documents\\Tencent Files\\938364861\\FileRecv\\wikitext");
        File[] s2 = file.listFiles(pathname -> {
            String s = pathname.getName().split("\\.")[0];
            String[] split = s.split("_");
            String s1 = split[split.length - 1];
            return Integer.parseInt(s1) > 2944;
        });
        assert s2 != null;
        ArrayList<Card> cards = new ArrayList<>();
        for (File file1 : s2) {
            Card card = new Card();
            JsonNode node = mapper.readTree(file1);
            JsonNode node1 = node.get("编号");
            if (node1 != null) {
                card.setCardId(node1.asInt());
            }
            JsonNode node2 = node.get("偶像名");
            if (node2 != null) {
                card.setIdolName(node2.asText());
            }
            JsonNode node3 = node.get("稀有度");
            if (node3 != null) {
                card.setRarity((byte) node3.asInt());
            }
            JsonNode node4 = node.get("卡名");
            if (node4 != null) {
                if (node4.get("日") != null) {
                    card.setCardNameJp(node4.get("日").asText());
                }
                if (node4.get("中") != null) {
                    card.setCardNameCn(node4.get("中").asText());
                }
            }
            JsonNode node5 = node.get("颜色");
            if (node5 != null) {
                card.setCardColor(node5.asText());
            }
            if (node.get("特化") != null) {
                card.setCardAttr(node.get("特化").asText());
            }
            JsonNode node6 = node.get("卡池");
            if (node6 != null) {
                if (node6.get("名称") != null) {
                    JsonNode node8 = node6.get("名称");
                    JsonNode node7 = node8.get("日");
                    if (node7 != null) {
                        card.setCardPoolName(node7.asText());
                    }
                }
            }
            if (node.get("SPP") != null) {
                JsonNode node7 = node.get("SPP").get("歌曲名");
                if (node7 != null) {
                    card.setSppName(node7.asText());
                }
                node7 = node.get("SPP").get("歌曲id");
                if (node7 != null) {
                    card.setSppSongId(node7.asInt());
                }
            }
            cards.add(card);
        }
        cards.forEach(cardMapper::insert);
    }
}
