package moe.aira.core.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XXLJobConfig {

    @Value("${xxl.job.admin.addresses}")
    private String adminAddresses;

    @Value("${xxl.job.accessToken}")
    private String accessToken;

    @Value("${xxl.job.executor.appname}")
    private String appname;

    @Value("${xxl.job.executor.port}")
    private int port;

    @Value("${xxl.job.executor.ip}")
    private String ip;

    @Bean
    public XxlJobSpringExecutor xxlJobSpringExecutor() {
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setAppname(appname);
        xxlJobSpringExecutor.setLogPath("/home/ubuntu/aira-task-log");
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        return xxlJobSpringExecutor;
    }

}

