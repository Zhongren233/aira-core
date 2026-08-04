package moe.aira.controller;

import lombok.SneakyThrows;
import moe.aira.entity.aira.AiraEventPointDto;
import moe.aira.entity.aira.AiraEventScoreDto;
import moe.aira.entity.api.ApiResult;
import moe.aira.enums.EventRank;
import moe.aira.onebot.client.AiraEventClient;
import moe.aira.onebot.manager.IEventConfigManager;
import moe.aira.onebot.task.EventReportTask;
import moe.aira.onebot.util.AiraRankingImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/image")
public class ImageServiceController {
    final
    AiraEventClient airaEventClient;

    public ImageServiceController(AiraEventClient airaEventClient, EventReportTask eventReportTask, IEventConfigManager eventConfigManager) {
        this.airaEventClient = airaEventClient;
        this.eventReportTask = eventReportTask;
        this.eventConfigManager = eventConfigManager;
    }

    @SneakyThrows
    @GetMapping(value = "/pointRanking", produces = MediaType.IMAGE_PNG_VALUE)
    public void pr(HttpServletResponse response) {
        Integer[] ranks = Arrays.stream(EventRank.values()).map(EventRank::getRank).toList().toArray(new Integer[0]);
        ApiResult<List<AiraEventPointDto>> listApiResult = airaEventClient.fetchCurrentRankPoint(ranks);
        if (listApiResult == null || listApiResult.getCode() != 0) {
            return;
        }
        List<AiraEventPointDto> data = listApiResult.getData();
        BufferedImage image = AiraRankingImageUtil.generatorPointImage(data);
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(image, "png", outputStream);
        outputStream.flush();
        outputStream.close();

    }

    @SneakyThrows
    @GetMapping(value = "/scoreRanking", produces = MediaType.IMAGE_PNG_VALUE)
    public void sr(HttpServletResponse response) {
        Integer[] ranks = Arrays.stream(EventRank.values()).map(EventRank::getRank).toList().toArray(new Integer[0]);
        ApiResult<List<AiraEventScoreDto>> listApiResult = airaEventClient.fetchCurrentRankScore(ranks);
        if (listApiResult == null || listApiResult.getCode() != 0) {
            return;
        }
        List<AiraEventScoreDto> data = listApiResult.getData();
        BufferedImage image = AiraRankingImageUtil.generatorScoreImage(data);
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(image, "png", outputStream);
        outputStream.flush();
        outputStream.close();
    }

    final
    EventReportTask eventReportTask;
    final
    IEventConfigManager eventConfigManager;


    @SneakyThrows
    @GetMapping(value = "/event", produces = MediaType.IMAGE_PNG_VALUE)
    public void event(HttpServletResponse response) {
        BufferedImage image = eventReportTask.getBufferedImage(eventConfigManager.fetchEventConfig());
        if (image == null) {
            return;
        }
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(image, "png", outputStream);
        outputStream.flush();
        outputStream.close();
    }

    @SneakyThrows
    @GetMapping(value = "/me", produces = MediaType.IMAGE_PNG_VALUE)
    public void me(String userId,HttpServletResponse response) {


        BufferedImage image = eventReportTask.getBufferedImage(eventConfigManager.fetchEventConfig());
        if (image == null) {
            return;
        }
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(image, "png", outputStream);
        outputStream.flush();
        outputStream.close();
    }
}
