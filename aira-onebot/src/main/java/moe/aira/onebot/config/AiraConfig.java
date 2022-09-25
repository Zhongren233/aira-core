package moe.aira.onebot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "aira")
public class AiraConfig {

    public static String REPORT_PATH;
    public static String ASSETS_PATH;
    public static String TEMPLATE_PATH;
    private String assetsPath;
    private String templatePath;

    @Value("${aira.service-url}")
    private String serviceUrl;

    public static String getAssetsPath() {
        return ASSETS_PATH != null ? ASSETS_PATH : "C:/Users/sc/Documents/Tencent Files/732713726/FileRecv/assets";
    }

    public void setAssetsPath(String assetsPath) {
        ASSETS_PATH = assetsPath;
        this.assetsPath = assetsPath;
    }


    public void setTemplatePath(String templatePath) {
        TEMPLATE_PATH = templatePath;
        this.templatePath = templatePath;
    }

    public void setReportPath(String reportPath) {
        REPORT_PATH = reportPath;
    }

}
