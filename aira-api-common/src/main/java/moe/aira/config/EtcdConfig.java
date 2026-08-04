package moe.aira.config;

import io.etcd.jetcd.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EtcdConfig {
    @Value("${aira.etcd.endpoint}")
    private String etcdEndpoint;

    @Bean
    public Client etcdClient() {
        return Client.builder()
                .endpoints(etcdEndpoint) // 可通过配置文件替换
                .build();
    }
}
