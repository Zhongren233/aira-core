package moe.aira.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import moe.aira.core.biz.IAiraEnsembleStarsMusicBiz;
import moe.aira.entity.api.ApiResult;
import moe.aira.entity.api.FetchCatalogResponse;
import moe.aira.entity.hekk.Chapter;
import moe.aira.entity.hekk.Story;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AiraEnsembleStarsMusicController {
    final
    IAiraEnsembleStarsMusicBiz airaEnsembleStarsMusicBiz;

    public AiraEnsembleStarsMusicController(IAiraEnsembleStarsMusicBiz airaEnsembleStarsMusicBiz) {
        this.airaEnsembleStarsMusicBiz = airaEnsembleStarsMusicBiz;
    }

    @GetMapping("/ansan/fetchCatalog")
    public ApiResult<FetchCatalogResponse> fetchCatalog() {
        FetchCatalogResponse data = airaEnsembleStarsMusicBiz.fetchCatalogInfo();
        return ApiResult.success(data);
    }


    @GetMapping("/ansan/cards")
    public ApiResult<JsonNode> cards() {
        JsonNode data = airaEnsembleStarsMusicBiz.cards();
        return ApiResult.success(data);
    }

    @GetMapping("/ansan/story/readStories")
    public ApiResult<?> readStories(@RequestParam Long chapterId, Boolean asFile, HttpServletResponse servletResponse) throws IOException {
        List<Story> stories = airaEnsembleStarsMusicBiz.campaignChapterStories(chapterId);
        String title = stories.get(0).getTitle();
        StringBuilder stringBuilder = new StringBuilder();
        for (Story story : stories) {
            Story read = airaEnsembleStarsMusicBiz.readStory(story.getId());
            stringBuilder.append("【").append(story.getTitle()).append(" ").append(story.getSubTitle()).append("】\n");
            for (Story.Paragraph paragraph : read.getParagraphs()) {
                if (paragraph.getSpeaker() == null) {
                    stringBuilder.append(paragraph.getMessage()).append("\n");
                } else {
                    stringBuilder.append(paragraph.getSpeaker()).append("：").append(paragraph.getMessage()).append("\n");
                }
            }
            stringBuilder.append("\n");
        }
        String s = stringBuilder.toString();
        if (asFile == null || asFile) {
            ServletOutputStream outputStream = servletResponse.getOutputStream();
            servletResponse.setContentType("text/plain;charset=utf-8");
            servletResponse.setHeader(HttpHeaders.CACHE_CONTROL,"public, max-age=0, s-maxage=3600");
            servletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition
                    .attachment()            // 附件形式
                    .filename(title + ".txt", StandardCharsets.UTF_8)  // 文件名称 & 编码
                    .build()
                    .toString());
            outputStream.write(s.getBytes(StandardCharsets.UTF_8));
            return null;
        } else {
            return ApiResult.success(s);

        }
    }




    @GetMapping("/ansan/story/campaignChapters")
    public ApiResult<List<Chapter>> campaignChapters(@RequestParam(value = "limitedRelease", required = false) Boolean limitedRelease) {
        List<Chapter> chapters = airaEnsembleStarsMusicBiz.campaignChapters();
        if (limitedRelease == null || limitedRelease) {
            return ApiResult.success(chapters.stream().filter(a -> a.storyLimitedRelease != null).collect(Collectors.toList()));
        }

        return ApiResult.success(chapters);
    }




    @GetMapping("/ansan/story/readFeatureStories")
    public ApiResult<?> readFeatureStories(@RequestParam Long gachaId, Boolean asFile, HttpServletResponse servletResponse) throws IOException {
        List<Story> stories = airaEnsembleStarsMusicBiz.featureGachaStories(gachaId).getStories();
        String title = stories.get(0).getTitle();
        StringBuilder stringBuilder = new StringBuilder();
        for (Story story : stories) {
            Story read = airaEnsembleStarsMusicBiz.readStory(story.getId());
            stringBuilder.append("【").append(story.getTitle()).append(" ").append(story.getSubTitle()).append("】\n");
            for (Story.Paragraph paragraph : read.getParagraphs()) {
                if (paragraph.getSpeaker() == null) {
                    stringBuilder.append(paragraph.getMessage()).append("\n");
                } else {
                    stringBuilder.append(paragraph.getSpeaker()).append("：").append(paragraph.getMessage()).append("\n");
                }
            }
            stringBuilder.append("\n");
        }
        String s = stringBuilder.toString();
        if (asFile == null || asFile) {
            ServletOutputStream outputStream = servletResponse.getOutputStream();
            servletResponse.setContentType("text/plain;charset=utf-8");
            servletResponse.setHeader(HttpHeaders.CACHE_CONTROL,"public, max-age=0, s-maxage=3600");
            servletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition
                    .attachment()            // 附件形式
                    .filename(title + ".txt", StandardCharsets.UTF_8)  // 文件名称 & 编码
                    .build()
                    .toString());
            outputStream.write(s.getBytes(StandardCharsets.UTF_8));
            return null;
        } else {
            return ApiResult.success(s);

        }
    }


}
