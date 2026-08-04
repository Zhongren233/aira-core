package moe.aira.onebot.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import moe.aira.config.EventConfig;
import moe.aira.entity.aira.AiraEventPointDto;
import moe.aira.entity.aira.AiraEventScoreDto;
import moe.aira.enums.EventRank;
import moe.aira.enums.EventType;
import moe.aira.onebot.config.AiraConfig;
import moe.aira.onebot.entity.AiraTourAwardDto;
import moe.aira.onebot.entity.AiraUnitAwardDto;
import moe.aira.onebot.entity.EventReportDto;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
@Slf4j
public class EventReportTaskImageUtil {
    private static final Color PR_FONT_COLOR = new Color(7, 82, 145);
    private static final Color SR_FONT_COLOR = new Color(184, 126, 0);
    private static final Color AWARD_COLOR = new Color(184, 91, 0);
    private static final DecimalFormat decimalFormat = new DecimalFormat("#,###");

    public BufferedImage generateImage(EventReportDto eventReportDto) throws IOException {
        Integer eventId = eventReportDto.getEventId();
        return drawSignalImage(eventReportDto, eventId.toString());

    }

    @NotNull
    private static BufferedImage drawSignalImage(EventReportDto eventReportDto, String imageName) throws IOException {
        BufferedImage read = ImageIO.read(Path.of(AiraConfig.TEMPLATE_PATH, "report/" + imageName + ".png").toFile());
        BufferedImage image = new BufferedImage(read.getWidth(), read.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.drawImage(read, 0, 0, null);
        graphics.setFont(new Font("Noto Sans SC Black", Font.PLAIN, 40));
        graphics.setColor(Color.WHITE);
        graphics.drawString("记录时间:" + eventReportDto.getFormatDate(), 22, 100);
        Font font = new Font("Noto Sans SC Black", Font.PLAIN, 28);
        graphics.setFont(font);
        List<AiraEventPointDto> eventPoint = eventReportDto.getEventPoint();
        if (eventPoint != null) {
            drawPoint(eventPoint, graphics);
        }
        List<AiraEventScoreDto> eventScore = eventReportDto.getEventScore();
        if (eventScore != null) {
            drawScore(eventScore, graphics);
        }
        Map<Integer, Integer> countMap = eventReportDto.getCountMap();
        if (countMap != null) {
            drawCount(countMap, eventReportDto.getEventConfig(), graphics);
        }
        graphics.dispose();
        return image;
    }

    private static void drawCount(Map<Integer, Integer> countMap, EventConfig eventConfig, Graphics2D graphics) {
        graphics.setColor(AWARD_COLOR);
        if (eventConfig.getEventType() == EventType.TOUR || eventConfig.getEventType() == EventType.SS_FINAL) {
            drawTourAwardImage(new AiraTourAwardDto(eventConfig, countMap), graphics);
        } else if (eventConfig.getEventId() == 318) {
            drawNormalAwardImage(AiraUnitAwardDto.colabo2025(eventConfig, countMap), graphics);
        } else {
            drawNormalAwardImage(new AiraUnitAwardDto(eventConfig, countMap), graphics);
        }
    }

    private static void drawNormalAwardImage(AiraUnitAwardDto airaUnitAwardDto, Graphics2D graphics) {
        int x = 878;
        int y = 780 + 50;
        for (Integer oneCard : airaUnitAwardDto.getCards()) {
            String format = decimalFormat.format(oneCard) + "人";

            graphics.drawString(format, (int) (x - graphics.getFont().getStringBounds(format, graphics.getFontRenderContext()).getWidth()), y);
            y += 40;
        }
    }

    private static void drawTourAwardImage(AiraTourAwardDto airaTourAwardDto, Graphics2D graphics) {
        int x = 878;
        int y = 780 + 50;
        for (Integer oneCard : airaTourAwardDto.getOneCards()) {
            String format = decimalFormat.format(oneCard) + "人";

            graphics.drawString(format, (int) (x - graphics.getFont().getStringBounds(format, graphics.getFontRenderContext()).getWidth()), y);
            y += 40;
        }

        y = 1100 + 50;
        for (Integer twoCard : airaTourAwardDto.getTwoCards()) {
            String format = decimalFormat.format(twoCard) + "人";
            graphics.drawString(format, (int) (x - graphics.getFont().getStringBounds(format, graphics.getFontRenderContext()).getWidth()), y);
            y += 40;
        }
    }

    private static void drawTwoUnitAwardImage(AiraTourAwardDto airaTourAwardDto, Graphics2D graphics) {
        int x = 878;
        int baseY = 1300 + 30;
        int y = baseY;
        graphics.setColor(AWARD_COLOR);
        for (Integer oneCard : airaTourAwardDto.getOneCards()) {
            String format = decimalFormat.format(oneCard) + "人";

            graphics.drawString(format, (int) (x - graphics.getFont().getStringBounds(format, graphics.getFontRenderContext()).getWidth()), y);
            y += 40;
        }

        y = baseY + 320;
        for (Integer twoCard : airaTourAwardDto.getTwoCards()) {
            String format = decimalFormat.format(twoCard) + "人";
            graphics.drawString(format, (int) (x - graphics.getFont().getStringBounds(format, graphics.getFontRenderContext()).getWidth()), y);
            y += 40;
        }
    }

    private void drawScore(List<AiraEventScoreDto> eventScoreDtoList, Graphics2D graphics) {

        graphics.setColor(SR_FONT_COLOR);
        int x = 878;
        int y = 331;
        for (AiraEventScoreDto airaEventPointDto : eventScoreDtoList) {
            String format = decimalFormat.format(airaEventPointDto.getScore());
            Rectangle2D bounds = graphics.getFont().getStringBounds(format, graphics.getFontRenderContext());
            graphics.drawString(format, (int) (x - bounds.getWidth()), y);
            y += 40;
        }
    }

    private void drawPoint(List<AiraEventPointDto> eventPointDtoList, Graphics2D graphics) {
        int x = 428;
        int y = 331;
        graphics.setColor(PR_FONT_COLOR);
        for (AiraEventPointDto airaEventPointDto : eventPointDtoList) {
            String format = decimalFormat.format(airaEventPointDto.getPoint());
            Rectangle2D bounds = graphics.getFont().getStringBounds(format, graphics.getFontRenderContext());
            graphics.drawString(format, (int) (x - bounds.getWidth()), y);
            y += 40;
        }
    }

    //test ok
    public static BufferedImage generateSSFinalImage(EventReportDto eventReportDto,
                                                     List<AiraEventPointDto> pointDtos,
                                                     Map<Integer, Integer> redCount,
                                                     Map<Integer, Integer> whiteCount,
                                                     List<AiraEventScoreDto> redScore,
                                                     List<AiraEventScoreDto> whiteScore) throws IOException {
        eventReportDto.setEventPoint(pointDtos);
        eventReportDto.setEventScore(redScore);
        eventReportDto.setCountMap(redCount);
        BufferedImage redImage = drawSignalImage(eventReportDto, "243_RED");
        eventReportDto.setEventScore(whiteScore);
        eventReportDto.setCountMap(whiteCount);
        BufferedImage whiteImage = drawSignalImage(eventReportDto, "243_WHITE");
        BufferedImage image = new BufferedImage(redImage.getWidth() * 2, redImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.drawImage(redImage, 0, 0, null);
        graphics.drawImage(whiteImage, redImage.getWidth(), 0, null);
        graphics.dispose();
        return image;
    }

    public static BufferedImage generateTwoUnitImage(EventReportDto eventReportDto,
                                                     List<AiraEventScoreDto> blackScore,
                                                     List<AiraEventScoreDto> whiteScore) throws IOException {
        BufferedImage read = ImageIO.read(Path.of(AiraConfig.TEMPLATE_PATH, "report/" + eventReportDto.getEventId() + ".png").toFile());
//        BufferedImage read = ImageIO.read(Path.of("E:\\新建文件夹 (8)", "274.png").toFile());
        BufferedImage image = new BufferedImage(read.getWidth(), read.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.drawImage(read, 0, 0, null);
        graphics.setFont(new Font("Noto Sans SC Black", Font.PLAIN, 40));
        graphics.setColor(Color.WHITE);
        graphics.drawString("记录时间:" + eventReportDto.getFormatDate(), 22, 100);
        Font font = new Font("Noto Sans SC Black", Font.PLAIN, 28);
        graphics.setFont(font);
        drawPoint(eventReportDto.getEventPoint(), graphics);

        drawTwoUnitScore(blackScore, 428, new Color(100, 54, 143), graphics);
        drawTwoUnitScore(whiteScore, 428 + 450, new Color(216, 145, 48), graphics);
        AiraTourAwardDto airaTourAwardDto = new AiraTourAwardDto(eventReportDto.getEventConfig(), eventReportDto.getCountMap());
        drawTwoUnitAwardImage(airaTourAwardDto, graphics);
        return image;
    }

    private static void drawTwoUnitScore(List<AiraEventScoreDto> blackScore, int x, Color color, Graphics2D graphics) {
        graphics.setColor(color);
        int y = 830;
        for (AiraEventScoreDto airaEventPointDto : blackScore) {
            String format = decimalFormat.format(airaEventPointDto.getScore());
            Rectangle2D bounds = graphics.getFont().getStringBounds(format, graphics.getFontRenderContext());
            graphics.drawString(format, (int) (x - bounds.getWidth()), y);
            y += 40;
        }
    }

//    public static void main(String[] args) throws Exception {
//        EventReportDto eventReportDto = new EventReportDto();
//        eventReportDto.setFormatDate("2023-09-23 00:00");
//        ArrayList<AiraEventPointDto> pointDtos = new ArrayList<>();
//        ArrayList<AiraEventScoreDto> blackScore = new ArrayList<>();
//        ArrayList<AiraEventScoreDto> whiteScore = new ArrayList<>();
//
//        for (EventRank value : EventRank.values()) {
//            AiraEventPointDto e = new AiraEventPointDto();
//            e.setRank(value.getRank());
//            e.setPoint(ThreadLocalRandom.current().nextInt(100000000));
//            pointDtos.add(e);
//
//            AiraEventScoreDto airaEventScoreDto = new AiraEventScoreDto();
//            airaEventScoreDto.setScore(ThreadLocalRandom.current().nextInt(10000000));
//            airaEventScoreDto.setRank(value.getRank());
//            blackScore.add(airaEventScoreDto);
//            whiteScore.add(airaEventScoreDto);
//        }
//        HashMap<Integer, Integer> count = new HashMap<>();
//        count.put(350 * 10000, ThreadLocalRandom.current().nextInt(10000));
//        count.put(800 * 10000, ThreadLocalRandom.current().nextInt(10000));
//        count.put(1250 * 10000, ThreadLocalRandom.current().nextInt(10000));
//        count.put(2100 * 10000, ThreadLocalRandom.current().nextInt(10000));
//        count.put(2700 * 10000, ThreadLocalRandom.current().nextInt(10000));
//        count.put(420 * 10000, ThreadLocalRandom.current().nextInt(10000));
//        count.put(700 * 10000, ThreadLocalRandom.current().nextInt(10000));
//        count.put(1400 * 10000, ThreadLocalRandom.current().nextInt(10000));
//        count.put(1900 * 10000, ThreadLocalRandom.current().nextInt(10000));
//        count.put(2900 * 10000, ThreadLocalRandom.current().nextInt(10000));
//        BufferedImage bufferedImage = generateTwoUnitImage(eventReportDto, pointDtos,
//                count, blackScore, whiteScore);
//        ImageIO.write(bufferedImage, "PNG", new File("./test.png"));
//    }


}
