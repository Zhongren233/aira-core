package moe.aira;

import com.dtflys.forest.springboot.annotation.ForestScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@ForestScan(basePackages = "moe.aira.core.client")
public class AiraApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiraApplication.class, args);
    }

}
