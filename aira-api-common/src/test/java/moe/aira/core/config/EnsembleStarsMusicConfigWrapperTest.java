package moe.aira.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnsembleStarsMusicConfigWrapperTest {
    @Test
    void test() throws  Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(new EnsembleStarsMusicConfigWrapper.EnsembleStarsMusicConfig());
        System.out.println(s);
    }
}