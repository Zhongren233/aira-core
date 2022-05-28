package moe.aira.onebot.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mizosoft.methanol.MultipartBodyPublisher;
import lombok.extern.slf4j.Slf4j;
import moe.aira.onebot.mapper.AiraConfigMapper;
import moe.aira.onebot.service.WeiboService;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.Objects;

@Component
@Slf4j
public class WeiboServiceImpl implements WeiboService {
    final
    ObjectMapper objectMapper;
    final
    AiraConfigMapper airaConfigMapper;

    public WeiboServiceImpl(AiraConfigMapper airaConfigMapper, ObjectMapper objectMapper) {
        this.airaConfigMapper = airaConfigMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendWeibo(String status, File bufferedImage) throws IOException, InterruptedException, NoSuchAlgorithmException, KeyManagementException {
        String accessToken = airaConfigMapper.selectConfigValueByConfigKey("WEIBO_ACCESS_TOKEN");
        String domain = airaConfigMapper.selectConfigValueByConfigKey("WEIBO_DOMAIN");
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        MultipartBodyPublisher.Builder publisherBuilder = MultipartBodyPublisher.newBuilder()
                .textPart("access_token", accessToken).textPart("status", status + "\n" + domain);
        publisherBuilder.filePart("pic", Objects.requireNonNullElseGet(bufferedImage, () -> new File("C:\\Users\\sc\\Pictures\\3F147074FC0630617E8C22F7065EAD22.png")).toPath());
        publisherBuilder.textPart("rip", "8.8.4.4");
        publisherBuilder.boundary("boundary");
        builder.header("Content-Type", "multipart/form-data; boundary=boundary");
        HttpResponse<String> send = httpClient.send(builder.uri(URI.create("https://api.weibo.com/2/statuses/share.json")).POST(publisherBuilder.build()).build(), HttpResponse.BodyHandlers.ofString());

        System.out.println(send.body());
    }


    @Override
    public void updateToken(String code) throws IOException, InterruptedException {
        String client_id = airaConfigMapper.selectConfigValueByConfigKey("CLIENT_ID");
        String client_secret = airaConfigMapper.selectConfigValueByConfigKey("CLIENT_SECRET");
        String redirect_uri = airaConfigMapper.selectConfigValueByConfigKey("REDIRECT_URI");
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        String format = MessageFormat.format(
                "https://api.weibo.com/oauth2/access_token?client_id={0}&client_secret={1}&grant_type=authorization_code&redirect_uri={2}&code={3}",
                client_id, client_secret, redirect_uri, code);
        builder.uri(URI.create(format));
        builder.POST(HttpRequest.BodyPublishers.noBody());
        HttpResponse<String> send = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        log.info(send.body());
        String accessToken = objectMapper.readTree(send.body()).get("access_token").asText();
        airaConfigMapper.updateConfigValueByConfigKey("WEIBO_ACCESS_TOKEN", accessToken);
    }

    @Override
    public JsonNode getTokenInfo() {
        String accessToken = airaConfigMapper.selectConfigValueByConfigKey("WEIBO_ACCESS_TOKEN");
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        builder.uri(URI.create("https://api.weibo.com/oauth2/get_token_info"));
        builder.POST(HttpRequest.BodyPublishers.ofString(MessageFormat.format("access_token={0}", accessToken)));
        try {
            HttpResponse<String> send = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            String body = send.body();
            return objectMapper.readTree(body);
        } catch (IOException | InterruptedException e) {
            log.error("微博读取accessToken错误", e);
            return null;
        }
    }
}
