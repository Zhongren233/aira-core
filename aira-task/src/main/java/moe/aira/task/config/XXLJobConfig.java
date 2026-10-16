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

    // xxl-job 任务日志目录：原先硬编码为 /home/ubuntu/aira-task-log（依赖旧机用户名），改为可配置
    @Value("${xxl.job.executor.logpath:/aira/log/aira-task}")
    private String logPath;

    @Bean
    public XxlJobSpringExecutor xxlJobSpringExecutor() throws Exception {
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setAppname(appName);
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        return xxlJobSpringExecutor;
    }

}

