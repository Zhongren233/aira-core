package moe.aira.core.biz.impl;

import moe.aira.core.biz.IAiraEnsembleStarsMusicBiz;
import moe.aira.core.client.hekk.BaseClient;
import moe.aira.entity.api.FetchCatalogResponse;
import moe.aira.entity.hekk.TitleResponse;
import moe.aira.enums.AppStatusCode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component

public class IAiraEnsembleStarsMusicBizImpl implements IAiraEnsembleStarsMusicBiz {
    final
    BaseClient client;

    public IAiraEnsembleStarsMusicBizImpl(BaseClient client) {
        this.client = client;
    }

    @Override
    public FetchCatalogResponse fetchCatalogInfo() {
        FetchCatalogResponse fetchCatalogResponse = new FetchCatalogResponse();
        TitleResponse title = client.title();
        fetchCatalogResponse.setAppStatusCode(title.getAppStatusCode());
        if (title.getAppStatusCode() == AppStatusCode.OK) {
            List<TitleResponse.CatalogData> assetCatalogs = title.getAssetCatalogs();
            ArrayList<TitleResponse.CatalogData> list = new ArrayList<>(assetCatalogs);
            TitleResponse.CatalogData audioCatalog = title.getAudioCatalog();
            audioCatalog.setName("Audio");
            list.add(audioCatalog);
            fetchCatalogResponse.setCatalogDataList(list);
        }
        return fetchCatalogResponse;
    }
}
