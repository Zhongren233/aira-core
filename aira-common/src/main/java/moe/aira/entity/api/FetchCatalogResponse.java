package moe.aira.entity.api;

import lombok.Data;
import moe.aira.resp.hekk.TitleResponse;
import moe.aira.enums.AppStatusCode;

import java.util.List;
@Data
public class FetchCatalogResponse {

   private AppStatusCode appStatusCode;
   private List<TitleResponse.CatalogData> catalogDataList;
}
