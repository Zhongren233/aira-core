package moe.aira.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class MessagePackMapperConfig {
    @Bean("messagePackMapper")
    public ObjectMapper messagePackMapper() {
        return new ObjectMapper(new MessagePackFactory());
    }

}
