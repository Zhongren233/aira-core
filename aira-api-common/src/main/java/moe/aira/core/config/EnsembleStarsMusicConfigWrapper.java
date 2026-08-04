package moe.aira.core.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KeyValue;
import io.etcd.jetcd.Watch;
import io.etcd.jetcd.watch.WatchEvent;
import io.etcd.jetcd.watch.WatchResponse;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import moe.aira.resp.hekk.TitleResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class EnsembleStarsMusicConfigWrapper {

    private static final String CONFIG_KEY = "/config/aira/ensemble-stars/music/config.json";
    private static final ByteSequence KEY_SEQ = ByteSequence.from(CONFIG_KEY, StandardCharsets.UTF_8);

    private final ObjectMapper jacksonObjectMapper;
    private final Client etcdClient;

    @Getter
    private EnsembleStarsMusicConfig ensembleStarsMusicConfig;

    private Watch.Watcher watcher;

    public EnsembleStarsMusicConfigWrapper(
            @Qualifier("jacksonObjectMapper") ObjectMapper objectMapper,
            Client etcdClient
    ) {
        this.jacksonObjectMapper = objectMapper;
        this.etcdClient = etcdClient;
    }

    @PostConstruct
    public void init() {
        // 初始加载配置
        reloadFromEtcd().thenAccept(unused -> {
            log.info("已加载 etcd 配置");
        }).exceptionally(failure -> {
            log.error("初始化加载配置失败", failure);
            return null;
        });

        // 设置监听
        this.watcher = etcdClient.getWatchClient().watch(KEY_SEQ, new Watch.Listener() {
            @Override
            public void onNext(WatchResponse response) {
                for (WatchEvent event : response.getEvents()) {
                    switch (event.getEventType()) {
                        case PUT -> {
                            String json = event.getKeyValue().getValue().toString(StandardCharsets.UTF_8);
                            try {
                                ensembleStarsMusicConfig = jacksonObjectMapper.readValue(json, EnsembleStarsMusicConfig.class);
                                log.info("etcd 配置变更已自动加载");
                            } catch (JsonProcessingException e) {
                                log.error("etcd 配置解析失败", e);
                            }
                        }
                        case DELETE -> log.warn("etcd 配置被删除: " + CONFIG_KEY);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("监听 etcd 配置失败", throwable);
            }

            @Override
            public void onCompleted() {
                log.info("etcd 配置监听完成");
            }
        });
    }

    @PreDestroy
    public void shutdown() {
        if (watcher != null) {
            try {
                watcher.close();
                log.info("etcd watcher 已关闭");
            } catch (Exception e) {
                log.warn("关闭 etcd watcher 失败", e);
            }
        }

        if (etcdClient != null) {
            try {
                etcdClient.close();
                log.info("etcd 客户端已关闭");
            } catch (Exception e) {
                log.warn("关闭 etcd 客户端失败", e);
            }
        }
    }

    public CompletableFuture<Void> reloadFromEtcd() {
        return etcdClient.getKVClient().get(KEY_SEQ)
                .thenApply(response -> {
                    List<KeyValue> kvs = response.getKvs();
                    if (kvs.isEmpty()) {
                        throw new RuntimeException("etcd 中未找到配置: " + CONFIG_KEY);
                    }
                    KeyValue first = kvs.get(0);
                    String json = first.getValue().toString(StandardCharsets.UTF_8);
                    try {
                        this.ensembleStarsMusicConfig = jacksonObjectMapper.readValue(json, EnsembleStarsMusicConfig.class);
                        return null;
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("解析 etcd 配置失败", e);
                    }
                });
    }

    @SneakyThrows
    public void setEnsembleStarsMusicConfig(EnsembleStarsMusicConfig config) {
        this.ensembleStarsMusicConfig = config;
        String jsonConfig = jacksonObjectMapper.writeValueAsString(config);
        etcdClient.getKVClient().put(KEY_SEQ, ByteSequence.from(jsonConfig, StandardCharsets.UTF_8))
                .thenAccept(r -> log.info("配置已写入 etcd"))
                .exceptionally(t -> {
                    log.error("写入 etcd 失败", t);
                    return null;
                });
    }

    @SneakyThrows
    public void updateCatalog(List<TitleResponse.CatalogData> list, Long serverVersion) {
        EnsembleStarsMusicConfig.CatalogInfo catalog = this.ensembleStarsMusicConfig.getCatalogInfo();
        list.forEach(item -> {
            switch (item.getName()) {
                case "spine_production" -> catalog.setSpineProduction(item.getCatalogVersion());
                case "resources_production" -> catalog.setResourcesProduction(item.getCatalogVersion());
                case "3dlive_production" -> catalog.setLiveProduction(item.getCatalogVersion());
            }
        });
        this.ensembleStarsMusicConfig.setServerVersion(serverVersion);
        setEnsembleStarsMusicConfig(this.ensembleStarsMusicConfig);
    }

    // ========================= 内部配置结构体 =========================
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
        private Long serverVersion;
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
            Map<String, String> map = new HashMap<>();
            map.put("tracking_id", trackingId);
            map.put("idfa", idfa);
            map.put("idfv", idfv);
            map.put("ad_id", adId);
            map.put("user_agent", userAgent);
            map.put("device_model", deviceModel);
            map.put("format", format);
            map.put("version", version);
            map.put("server_version", serverVersion != null ? serverVersion.toString() : "");
            if (catalogInfo != null) {
                map.put("spine_production", catalogInfo.getSpineProduction());
                map.put("resources_production", catalogInfo.getResourcesProduction());
                map.put("3dlive_production", catalogInfo.getLiveProduction());
            }
            map.put("os_type", osType);
            map.put("firebase_app_instance_id", firebaseAppInstanceId);
            return map;
        }

        public String convertToFormUrlEncoded() {
            StringJoiner joiner = new StringJoiner("&");

            if (trackingId != null) joiner.add("tracking_id=" + encode(trackingId));
            if (idfa != null) joiner.add("idfa=" + encode(idfa));
            if (idfv != null) joiner.add("idfv=" + encode(idfv));
            if (adId != null) joiner.add("ad_id=" + encode(adId));
            if (userAgent != null) joiner.add("user_agent=" + encode(userAgent));
            if (deviceModel != null) joiner.add("device_model=" + encode(deviceModel));
            if (format != null) joiner.add("format=" + encode(format));
            if (version != null) joiner.add("version=" + encode(version));
            if (serverVersion != null) joiner.add("server_version=" + encode(serverVersion.toString()));

            if (catalogInfo != null) {
                if (catalogInfo.getSpineProduction() != null)
                    joiner.add("spine_production=" + encode(catalogInfo.getSpineProduction()));
                if (catalogInfo.getResourcesProduction() != null)
                    joiner.add("resources_production=" + encode(catalogInfo.getResourcesProduction()));
                if (catalogInfo.getLiveProduction() != null)
                    joiner.add("3dlive_production=" + encode(catalogInfo.getLiveProduction()));
            }

            if (osType != null) joiner.add("os_type=" + encode(osType));
            if (firebaseAppInstanceId != null)
                joiner.add("firebase_app_instance_id=" + encode(firebaseAppInstanceId));

            return joiner.toString();
        }

        private String encode(String value) {
            return URLEncoder.encode(value, StandardCharsets.UTF_8);
        }
    }
}
