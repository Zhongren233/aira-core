package moe.aira.core.config;

import com.dtflys.forest.callback.RetryWhen;
import com.dtflys.forest.exceptions.ForestRuntimeException;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import moe.aira.core.client.hekk.AccessTokenClient;
import moe.aira.entity.hekk.ServerResponse;
import moe.aira.enums.AppStatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component

public class EnsembleStarsRetryWhen implements RetryWhen {

    @Qualifier("messagePackMapper")
    @Autowired
    private ObjectMapper messagePackMapper;
    @Autowired
    AccessTokenClient accessTokenClient;
    @Autowired
    EnsembleStarsMusicConfigWrapper ensembleStarsMusicConfigWrapper;

    @Override
    public boolean retryWhen(ForestRequest req, ForestResponse res) {
        try {
            ServerResponse serverResponse = messagePackMapper.readValue(res.getByteArray(), ServerResponse.class);

            boolean flag = !serverResponse.getAppStatusCode().equals(AppStatusCode.OK);
            if (flag) {
                byte[] bytes = accessTokenClient.accessToken(ensembleStarsMusicConfigWrapper.getEnsembleStarsMusicConfig().getToken(), ensembleStarsMusicConfigWrapper.getEnsembleStarsMusicConfig().convertToQueryMap());
                String s = messagePackMapper.readTree(bytes).get("access_token").textValue();
                EnsembleStarsMusicConfigWrapper.EnsembleStarsMusicConfig ensembleStarsMusicConfig = ensembleStarsMusicConfigWrapper.getEnsembleStarsMusicConfig();
                String accessToken = "Token " + s;
                ensembleStarsMusicConfig.setAccessToken(accessToken);
                ensembleStarsMusicConfigWrapper.setEnsembleStarsMusicConfig(ensembleStarsMusicConfig);
                req.addHeader("Authorization", accessToken);
            }
            return flag;
        } catch (Exception e) {
            throw new ForestRuntimeException(e);
        }
    }
}
