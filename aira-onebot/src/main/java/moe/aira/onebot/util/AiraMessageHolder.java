package moe.aira.onebot.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Component
public class AiraMessageHolder {
    final
    StringRedisTemplate stringRedisTemplate;

    public AiraMessageHolder(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @NotNull
    private String buildCacheKey(String key) {
        Long qqNumber = AiraContext.currentUser().getQqNumber();
        key = "Holder:" + key + ":" + qqNumber;
        return key;
    }

    public void put(String key, String value) {
        key = buildCacheKey(key);
        stringRedisTemplate.opsForList().leftPush(key, value);
        stringRedisTemplate.expire(key, Duration.ofMinutes(5));
    }

    public List<String> get(String key) {
        List<String> range = stringRedisTemplate.opsForList().range(buildCacheKey(key), 0, -1);
        clear(key);
        return range;
    }


    public Integer size(String key) {
        Long size = stringRedisTemplate.opsForList().size(buildCacheKey(key));
        Objects.requireNonNull(size);
        return size.intValue();
    }

    public void clear(String key) {
        key = buildCacheKey(key);
        stringRedisTemplate.delete(key);
    }

}
