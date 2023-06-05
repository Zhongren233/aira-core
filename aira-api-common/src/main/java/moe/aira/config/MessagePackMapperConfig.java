package moe.aira.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import moe.aira.enums.AppStatusCode;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.util.Arrays;

@Configuration
public class MessagePackMapperConfig {
    @Bean("messagePackMapper")
    @Primary
    public ObjectMapper messagePackMapper() {
        ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(AppStatusCode.class, new StdScalarDeserializer<>(AppStatusCode.class) {
            @Override
            public AppStatusCode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                int valueAsInt = p.getValueAsInt();
                return Arrays.stream(AppStatusCode.values()).filter(appStatusCode -> appStatusCode.getCode() == valueAsInt).findFirst().orElse(AppStatusCode.SerializationError);
            }
        });
        objectMapper.registerModule(module);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }


}
