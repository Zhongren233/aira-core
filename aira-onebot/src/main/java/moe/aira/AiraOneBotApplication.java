package moe.aira;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableCaching
@SpringBootApplication
@EnableFeignClients
public class AiraOneBotApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AiraOneBotApplication.class);
        app.setMainApplicationClass(AiraOneBotApplication.class);
        app.run(args);
    }
}
