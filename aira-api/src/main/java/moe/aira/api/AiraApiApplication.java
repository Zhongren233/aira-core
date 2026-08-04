package moe.aira.api;

import com.dtflys.forest.springboot.annotation.ForestScan;
import moe.aira.core.client.hekk.BaseClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("moe.aira")
@MapperScan("moe.aira.core.dao")
@ForestScan(value = "moe.aira.core.client",basePackageClasses = BaseClient.class)
@EnableCaching
public class AiraApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiraApiApplication.class, args);
    }

}
