package moe.aira.core;

import com.dtflys.forest.springboot.annotation.ForestScan;
import moe.aira.core.entity.es.PointRanking;
import moe.aira.core.mapper.UserProfileMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@SpringBootApplication
@EnableCaching
@ForestScan(basePackages = "moe.aira.core.client")
public class AiraCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiraCoreApplication.class, args);
    }

}
