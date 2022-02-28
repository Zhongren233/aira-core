package moe.aira.core.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class EnsembleStarsConfig {
    @Value("${es.crypto.key}")
    private String cryptoKey;
}
