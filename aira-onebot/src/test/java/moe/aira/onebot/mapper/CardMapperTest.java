package moe.aira.onebot.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

class CardMapperTest {

    @Test
    void name() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpClient httpClient = HttpClient.newHttpClient();
        FileWriter fileWriter = new FileWriter("./1.csv");
        int id = 3223;
        JsonNode node;
        do {
            String body = "{\"编号\":{$gt:" + id + ",$lte:" + (id + 100) + "}}";
            id += 100;
            body = URLEncoder.encode(body, StandardCharsets.UTF_8);
            String str = "https://ensemblestars.huijiwiki.com/api/rest_v1/namespace/data?filter=" + body;
            HttpResponse<InputStream> send = httpClient.send(HttpRequest.newBuilder(URI.create(str)).GET().build(), HttpResponse.BodyHandlers.ofInputStream());
            InputStream inputStream = send.body();
            node = objectMapper.readTree(inputStream);
            System.out.println(node);
            JsonNode nodeTT = node.get("_embedded");
            for (JsonNode jsonNode : nodeTT) {
                StringJoiner stringJoiner = new StringJoiner(",");
                stringJoiner.add(jsonNode.get("编号").asText());
                stringJoiner.add(jsonNode.get("稀有度").asText());
                stringJoiner.add(jsonNode.get("卡名").get("日").asText());
                JsonNode node1 = jsonNode.get("卡名").get("中");
                stringJoiner.add(node1 == null ? "未知翻译" : node1.asText());
                stringJoiner.add(jsonNode.get("偶像名").asText());
                stringJoiner.add(jsonNode.get("颜色").asText());
                stringJoiner.add(jsonNode.get("特化").asText());
                JsonNode node2 = jsonNode.get("SPP");
                if (node2 == null) {
                    stringJoiner.add("-1");
                } else {
                    stringJoiner.add(node2.get("歌曲id") == null ? "-1" : node2.get("歌曲id").asText());
                }
                fileWriter.write(stringJoiner.toString());
                fileWriter.write("\n");
            }

        } while (node.get("_returned").intValue() != 0);
        fileWriter.close();
    }
}