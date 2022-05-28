package moe.aira.onebot.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import moe.aira.config.EventConfig;
import moe.aira.entity.aira.AiraEventPointDto;
import moe.aira.entity.aira.AiraEventScoreDto;
import moe.aira.enums.EventType;
import moe.aira.onebot.entity.AiraTourAwardDto;
import moe.aira.onebot.entity.AiraUnitAwardDto;
import moe.aira.onebot.entity.EventReportDto;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

@UtilityClass
@Slf4j
public class EventReportTaskImageUtil {
    private static final Color PR_FONT_COLOR = new Color(7, 82, 145);
    private static final Color SR_FONT_COLOR = new Color(184, 126, 0);
    private static final Color AWARD_COLOR = new Color(184, 91, 0);
    private static final DecimalFormat decimalFormat = new DecimalFormat("#,###");

    public BufferedImage generateImage(EventReportDto eventReportDto) throws IOException {
        Integer eventId = eventReportDto.getEventId();
        BufferedImage read = ImageIO.read(new ClassPathResource("image/template/report/" + eventId + ".png").getInputStream());
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

        return image;

    }

    private static void drawCount(Map<Integer, Integer> countMap, EventConfig eventConfig, Graphics2D graphics) {
        graphics.setColor(AWARD_COLOR);
        if (eventConfig.getEventType() == EventType.TOUR) {
            drawTourAwardImage(new AiraTourAwardDto(eventConfig, countMap), graphics);
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
}
