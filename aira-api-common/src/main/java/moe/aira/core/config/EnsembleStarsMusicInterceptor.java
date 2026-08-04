package moe.aira.core.config;

import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.Interceptor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@SuppressWarnings("unchecked")
@Component
@Slf4j
public class EnsembleStarsMusicInterceptor implements Interceptor<String> {
    final
    EnsembleStarsMusicConfigWrapper ensembleStarsMusicConfigWrapper;
    private final ObjectMapper messagePackMapper;

    public EnsembleStarsMusicInterceptor(EnsembleStarsMusicConfigWrapper ensembleStarsMusicConfigWrapper, @Qualifier("messagePackMapper") ObjectMapper messagePackMapper) {
        this.ensembleStarsMusicConfigWrapper = ensembleStarsMusicConfigWrapper;
        this.messagePackMapper = messagePackMapper;
    }

    @Override
    public boolean beforeExecute(ForestRequest request) {
        request.addQuery(ensembleStarsMusicConfigWrapper.getEnsembleStarsMusicConfig().convertToQueryMap());

        {
            request.addHeader("Authorization", ensembleStarsMusicConfigWrapper.getEnsembleStarsMusicConfig().getAccessToken());
        }
        return true;
    }

    @Override
    public void onSuccess(String data, ForestRequest request, ForestResponse response) {
        try {
            JsonNode node = messagePackMapper.readTree(response.getByteArray());
            log.debug("日服返回信息:{}", node);
            Object result = messagePackMapper.treeToValue(node, request.getMethod().getReturnClass());
            response.setResult(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onRetry(ForestRequest request, ForestResponse response) {
        Object result;
        try {
            result = messagePackMapper.readValue(response.getByteArray(), request.getMethod().getReturnClass());
            response.setResult(result);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
