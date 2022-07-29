package moe.aira.task;

import com.dtflys.forest.springboot.annotation.ForestScan;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@SpringBootApplication
@ComponentScan("moe.aira")
@MapperScan("moe.aira.core.dao")
@ForestScan("moe.aira.core.client")
@EnableCaching
public class AiraTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiraTaskApplication.class, args);
        ExecutorService executorService = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(false);
            return thread;
        });
        executorService.execute(() -> log.info("保活"));
    }

}
