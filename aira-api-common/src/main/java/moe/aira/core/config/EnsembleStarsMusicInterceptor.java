package moe.aira.core.config;

import com.dtflys.forest.callback.RetryWhen;
import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestRequestBody;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.http.body.ByteArrayRequestBody;
import com.dtflys.forest.http.body.NameValueRequestBody;
import com.dtflys.forest.interceptor.Interceptor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import moe.aira.core.client.hekk.AccessTokenClient;
import moe.aira.core.client.hekk.BaseClient;
import moe.aira.core.util.CryptoUtils;
import moe.aira.entity.hekk.ServerResponse;
import moe.aira.enums.AppStatusCode;
import moe.aira.exception.AiraException;
import moe.aira.exception.EnsembleStarsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

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
            Object result = messagePackMapper.readValue(response.getByteArray(), request.getMethod().getReturnClass());
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
