package moe.aira;

import com.dtflys.forest.springboot.annotation.ForestScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableCaching
@ForestScan(basePackages = "moe.aira.core.client")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan("moe.aira.*")
public class AiraApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiraApplication.class, args);
    }

}
