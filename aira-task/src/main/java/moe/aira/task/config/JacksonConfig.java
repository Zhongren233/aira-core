package moe.aira.task.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean("jacksonObjectMapper")
    public ObjectMapper jacksonObjectMapper() {
        return new ObjectMapper();
    }



}
