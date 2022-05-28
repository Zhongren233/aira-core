package moe.aira.core.config;

import moe.aira.core.util.CryptoUtils;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AsyncHttpClientConfig {
    @Autowired
    CryptoUtils cryptoUtils;

    @Bean
    public CloseableHttpAsyncClient httpClient() {

        CloseableHttpAsyncClient build = HttpAsyncClientBuilder.create().build();
        build.start();

        return build;
    }
}
