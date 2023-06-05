package moe.aira.entity.hekk;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.catalog.Catalog;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TitleResponse extends ServerResponse {

    private byte[] masterData;

    @Data
    public static class CatalogData {
        private Integer id;
        private String name;
        private String catalogVersion;
        private String clientVersion;
        private String description;
        private String updatedAt;
        private String beganAt;
        private String generatedAt;

    }

    private List<CatalogData> assetCatalogs;

    private CatalogData audioCatalog;

    @Override
    public String toString() {
        return "TitleResponse{" +
                "appStatusCode=" + appStatusCode +
                ", serverVersion=" + serverVersion +
                ", currentTime=" + currentTime +
                ", assetCatalogs=" + assetCatalogs +
                ", audioCatalog=" + audioCatalog +
                '}';
    }
}
