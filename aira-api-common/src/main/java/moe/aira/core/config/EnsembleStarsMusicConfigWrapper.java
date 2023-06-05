package moe.aira.core.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EnsembleStarsMusicConfigWrapper {
    @Data
    public static class EnsembleStarsMusicConfig {
        private String trackingId;
        private String idfa;
        private String idfv;
        private String adId;
        private String userAgent;
        private String deviceModel;
        private String format;
        private String version;
        private String serverVersion;
        private CatalogInfo catalogInfo = new CatalogInfo();

        private String osType;

        private String firebaseAppInstanceId;
        private String accessToken;
        private String token;

        @Data
        public static class CatalogInfo {
            private String spineProduction;
            private String resourcesProduction;
            private String liveProduction;
        }

        public Map<String, String> convertToQueryMap() {
            HashMap<String, String> map = new HashMap<>();
            map.put("tracking_id", trackingId);
            map.put("idfa", idfa);
            map.put("idfv", idfv);
            map.put("ad_id", adId);
            map.put("user_agent", userAgent);
            map.put("device_model", deviceModel);
            map.put("format", format);
            map.put("version", version);
            map.put("server_version", serverVersion);
            map.put("spine_production", catalogInfo.spineProduction);
            map.put("resources_production", catalogInfo.resourcesProduction);
            map.put("3dlive_production", catalogInfo.liveProduction);
            map.put("os_type", osType);
            map.put("firebase_app_instance_id", firebaseAppInstanceId);
            return map;
        }
    }

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper jacksonObjectMapper;
    private EnsembleStarsMusicConfig ensembleStarsMusicConfig;


    public EnsembleStarsMusicConfigWrapper(StringRedisTemplate stringRedisTemplate, @Qualifier("jacksonObjectMapper") ObjectMapper jacksonObjectMapper) throws JsonProcessingException {
        this.stringRedisTemplate = stringRedisTemplate;
        this.jacksonObjectMapper = jacksonObjectMapper;
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        String s = operations.get("EnsembleStarsMusicConfig");
        if (s == null) {
            throw new RuntimeException("ESMConfig为空");
        }
        this.ensembleStarsMusicConfig = jacksonObjectMapper.readValue(s, EnsembleStarsMusicConfig.class);
    }

    public EnsembleStarsMusicConfig getEnsembleStarsMusicConfig() {
        return ensembleStarsMusicConfig;
    }

    @SneakyThrows
    public void setEnsembleStarsMusicConfig(EnsembleStarsMusicConfig ensembleStarsMusicConfig) {
        this.ensembleStarsMusicConfig = ensembleStarsMusicConfig;
        String s = jacksonObjectMapper.writeValueAsString(ensembleStarsMusicConfig);
        stringRedisTemplate.opsForValue().set("EnsembleStarsMusicConfig", s);
    }
}
