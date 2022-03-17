package moe.aira.core.config;

import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestRequestBody;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.http.body.ByteArrayRequestBody;
import com.dtflys.forest.http.body.NameValueRequestBody;
import com.dtflys.forest.interceptor.Interceptor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import moe.aira.core.util.CryptoUtils;
import moe.aira.exception.AiraException;
import moe.aira.exception.EnsembleStarsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

@Component
@Slf4j
public class EnsembleStarsInterceptor implements Interceptor<String> {
    @Value("${es.game.token}")
    private String token;
    @Value("${es.game.session}")
    private String session;

    @Value("${es.game.resMd5}")
    private String resMd5;
    @Value("${es.game.major}")
    private String major;
    @Autowired
    ObjectMapper messagePackMapper;

    @Autowired
    CryptoUtils cryptoUtils;


    @Override
    public boolean beforeExecute(ForestRequest request) {
        setUpHeader(request);
        addParameters(request);
        try {
            encrypt(request);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new AiraException(e);
        }
        return true;
    }

    @Override
    public void onSuccess(String data, ForestRequest request, ForestResponse response) {
        try {
            decrypt(response);
        } catch (Exception e) {
            throw new AiraException(e);
        }
        if (response.getStatusCode() != 200) {
            log.info("状态不等于200:{}", response.getResult());
            throw new EnsembleStarsException("状态码不等于200");
        }
    }

    @Override
    public void onError(ForestRuntimeException ex, ForestRequest request, ForestResponse response) {
        log.error("exception", ex);
    }

    private void decrypt(ForestResponse response) throws Exception {
        byte[] byteArray = response.getByteArray();
        byte[] decrypt = cryptoUtils.decrypt(byteArray);
        JsonNode jsonNode = messagePackMapper.readTree(decrypt);
        response.setResult(jsonNode);
    }

    private void encrypt(ForestRequest request) throws IllegalBlockSizeException, BadPaddingException {
        List<ForestRequestBody> body = request.getBody();
        StringJoiner stringJoiner = new StringJoiner("&");
        body.forEach(forestRequestBody -> {
            if (forestRequestBody instanceof NameValueRequestBody
            ) {
                NameValueRequestBody nameValueRequestBody = (NameValueRequestBody) forestRequestBody;
                Object value = nameValueRequestBody.getValue();
                if (value instanceof String)
                    nameValueRequestBody.setValue(
                            URLEncoder.encode((String) value,
                                    StandardCharsets.UTF_8));

            }
            stringJoiner.add(forestRequestBody.toString());
        });
        String parameters = stringJoiner.toString();
        log.debug("未加密前参数:{}", parameters);
        byte[] encrypt = cryptoUtils.encrypt(parameters);
        ByteArrayRequestBody byteArrayRequestBody = new ByteArrayRequestBody(encrypt);
        request.replaceBody(byteArrayRequestBody);
    }

    private void addParameters(ForestRequest request) {
        List<ForestRequestBody> body = request.getBody();
        body.add(0, new NameValueRequestBody("login_type", "mobile"));
        body.add(0, new NameValueRequestBody("hei_token", token));
        body.add(0, new NameValueRequestBody("session", session));
        body.add(0, new NameValueRequestBody("channel_uid", "522e3495d82423b3675b035c9a06c69c"));
        body.add(0, new NameValueRequestBody("platform", "iOS"));
        body.add(0, new NameValueRequestBody("packageName", "apple"));
        body.add(0, new NameValueRequestBody("resMd5", resMd5));
        body.add(0, new NameValueRequestBody("major", major));
        body.add(0, new NameValueRequestBody("maintainceCnfVer", "31"));
        body.add(0, new NameValueRequestBody("msg_id", UUID.randomUUID().toString()));
    }

    private void setUpHeader(ForestRequest request) {
        request.addHeader("Authorization", "Token " + token);
        request.addHeader("Content-Type", "application/octet-stream");
        request.addHeader("X-Game-Version", major);//？这啥啊

//        request.addHeader("X-Protocol-Compress", true);//？这啥啊

    }
}
