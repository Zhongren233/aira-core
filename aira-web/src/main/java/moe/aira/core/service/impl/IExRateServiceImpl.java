package moe.aira.core.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import moe.aira.core.service.IExRateService;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Component
public class IExRateServiceImpl implements IExRateService {
    final
    StringRedisTemplate stringRedisTemplate;

    public IExRateServiceImpl(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public JsonNode fetchCNYRate() throws IOException {
        String cnyRate = stringRedisTemplate.opsForValue().get("cnyRate");
        ObjectMapper objectMapper = new ObjectMapper();
        if (cnyRate != null) {
            return objectMapper.readTree(cnyRate);
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        Request build = new Request.Builder().url("https://open.er-api.com/v6/latest/CNY").get().build();
        Call call = okHttpClient.newCall(build);
        Response execute = call.execute();
        assert execute.body() != null;
        JsonNode jsonNode = objectMapper.readTree(execute.body().byteStream());
        stringRedisTemplate.opsForValue().set("cnyRate", jsonNode.toString(), Duration.ofHours(12));
        return jsonNode;
    }
}
