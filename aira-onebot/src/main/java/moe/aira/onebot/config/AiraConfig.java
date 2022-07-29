package moe.aira.onebot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "aira")
public class AiraConfig {

    private static String ASSETS_PATH;
    private String assetsPath;
    @Value("${aira.service-url}")
    private String serviceUrl;

    public static String getAssetsPath() {
        return ASSETS_PATH != null ? ASSETS_PATH : "C:/Users/sc/Documents/Tencent Files/732713726/FileRecv/assets";
    }

    public void setAssetsPath(String assetsPath) {
        ASSETS_PATH = assetsPath;
        this.assetsPath = assetsPath;
    }
}
