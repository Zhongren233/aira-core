package moe.aira.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import moe.aira.core.util.CryptoUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileOutputStream;

@SpringBootTest
public class DTest {
    @Autowired
    CryptoUtils cryptoUtils;
    @Qualifier("messagePackMapper")
    @Autowired
    private ObjectMapper messagePackMapper;

    @Test
    void test() throws Exception {
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\sc\\Documents\\mv");
        byte[] bytes = fileInputStream.readAllBytes();
        byte[] decrypt = cryptoUtils.decrypt(bytes);
        JsonNode jsonNode = messagePackMapper.readTree(decrypt);
        ObjectNode node = (ObjectNode) jsonNode;
        ArrayNode liveCostumes = (ArrayNode) node.get("live_costumes");
        for (JsonNode liveCostume : liveCostumes) {
            ObjectNode costume = (ObjectNode) liveCostume;
            costume.remove("head_asset_address");
            costume.put("head_asset_address", "3dlive/livecharacters/head/aira/base");
        }
        node.set("live_costumes", liveCostumes);
        System.out.println(node);
        ObjectNode live_scene_information = (ObjectNode) node.get("live_scene_information");
        live_scene_information.remove("live_scene_asset_address");
        live_scene_information.put("live_scene_asset_address", "3dlive/livescenes/mv058_livingontheedge");
        node.set("live_scene_information", live_scene_information);
        byte[] encrypt = cryptoUtils.encrypt(messagePackMapper.writeValueAsBytes(node));
        FileOutputStream fileOutputStream = new FileOutputStream("./test");
        fileOutputStream.write(encrypt);
        fileOutputStream.close();
    }
}
