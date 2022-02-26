package moe.aira.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagePackMapperConfig {
    @Bean("messagePackMapper")
    public ObjectMapper messagePackMapper() {
        return new ObjectMapper(new MessagePackFactory());
    }

}
