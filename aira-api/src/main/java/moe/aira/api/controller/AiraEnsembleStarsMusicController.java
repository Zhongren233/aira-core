package moe.aira.api.controller;

import moe.aira.core.biz.IAiraEnsembleStarsMusicBiz;
import moe.aira.entity.api.ApiResult;
import moe.aira.entity.api.FetchCatalogResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiraEnsembleStarsMusicController {
    final
    IAiraEnsembleStarsMusicBiz airaEnsembleStarsMusicBiz;

    public AiraEnsembleStarsMusicController(IAiraEnsembleStarsMusicBiz airaEnsembleStarsMusicBiz) {
        this.airaEnsembleStarsMusicBiz = airaEnsembleStarsMusicBiz;
    }

    @GetMapping("/ansan/fetchCatalog")
    public ApiResult<FetchCatalogResponse> fetchCatalog() {
        return ApiResult.success(airaEnsembleStarsMusicBiz.fetchCatalogInfo());

    }


}
