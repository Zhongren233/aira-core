package moe.aira.task.config;

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
    private String appName;

    @Value("${xxl.job.executor.port}")
    private int port;

    @Value("${xxl.job.executor.ip}")
    private String ip;

    @Bean
    public XxlJobSpringExecutor xxlJobSpringExecutor() throws Exception {
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setAppname(appName);
        xxlJobSpringExecutor.setLogPath("/home/ubuntu/aira-task-log");
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        return xxlJobSpringExecutor;
    }

}

