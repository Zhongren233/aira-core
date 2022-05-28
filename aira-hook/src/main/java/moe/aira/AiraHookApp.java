package moe.aira;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication(scanBasePackages = {"moe.aira"})
public class AiraHookApp {
    public static void main(String[] args) {
        SpringApplication.run(AiraHookApp.class, args);
        System.out.println("AiraHookApp is running!");
    }
}
