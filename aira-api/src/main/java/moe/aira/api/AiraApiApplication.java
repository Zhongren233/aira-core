package moe.aira.api;

import com.dtflys.forest.springboot.annotation.ForestScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("moe.aira")
@MapperScan("moe.aira.core.dao")
@ForestScan("moe.aira.core.client")
@EnableCaching
public class AiraApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiraApiApplication.class, args);
    }

}
