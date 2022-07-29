package moe.aira.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MessagePackMapperConfig {
    @Bean("messagePackMapper")
    @Primary
    public ObjectMapper messagePackMapper() {
        return new ObjectMapper(new MessagePackFactory());
    }


}
