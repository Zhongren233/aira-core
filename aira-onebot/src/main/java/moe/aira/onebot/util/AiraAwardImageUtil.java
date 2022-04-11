package moe.aira.onebot.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import moe.aira.config.EventConfig;
import moe.aira.enums.EventType;
import moe.aira.onebot.entity.AiraTourAwardDto;
import moe.aira.onebot.entity.AiraUnitAwardDto;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
@UtilityClass
public class AiraAwardImageUtil {
    private static final Font FONT = new Font("Noto Sans SC Black", Font.PLAIN, 65);
    private static final Font FOOTER_FONT = new Font("Noto Sans SC Black", Font.PLAIN, 36);
    private static final BufferedImage NORMAL_AWARD_IMAGE;
    private static final BufferedImage TOUR_AWARD_IMAGE;
    private static final Color COLOR = new Color(0xB85B00);

    static {
        BufferedImage NORMAL_AWARD_IMAGE1 = null;
        try {
            NORMAL_AWARD_IMAGE1 = ImageIO.read(new ClassPathResource("/image/template/unit-award.png").getInputStream());
        } catch (IOException e) {
            log.error("", e);
        }
        NORMAL_AWARD_IMAGE = NORMAL_AWARD_IMAGE1;


        BufferedImage TOUR_AWARD_IMAGE1 = null;
        try {
            TOUR_AWARD_IMAGE1 = ImageIO.read(new ClassPathResource("/image/template/tour-award.png").getInputStream());
        } catch (IOException e) {
            log.error("", e);
        }
        TOUR_AWARD_IMAGE = TOUR_AWARD_IMAGE1;
    }

    public static BufferedImage generateAwardImage(EventConfig eventConfig, Map<Integer, Integer> data) {
        if (eventConfig.getEventType() == EventType.TOUR) {
            return generateTourAwardImage(new AiraTourAwardDto(eventConfig, data));
        } else {
            return generateNormalAwardImage(new AiraUnitAwardDto(eventConfig, data));
        }
    }


    public static BufferedImage generateNormalAwardImage(AiraUnitAwardDto airaUnitAwardDto) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        BufferedImage image = new BufferedImage(NORMAL_AWARD_IMAGE.getWidth(), NORMAL_AWARD_IMAGE.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.drawImage(NORMAL_AWARD_IMAGE, 0, 0, null);
        int x = 850;
        int y = 325;
        graphics.setColor(COLOR);
        graphics.setFont(FONT);
        for (Integer card : airaUnitAwardDto.getCards()) {
            String str = decimalFormat.format(card) + "人";
            Rectangle2D stringBounds = FONT.getStringBounds(str, graphics.getFontRenderContext());
            graphics.drawString(str, (int) (x - stringBounds.getWidth()), y);
            y += 100;
        }
        drawFooter(image, graphics);
        return image;
    }

    private static BufferedImage generateTourAwardImage(AiraTourAwardDto airaTourAwardDto) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        BufferedImage image = new BufferedImage(TOUR_AWARD_IMAGE.getWidth(), TOUR_AWARD_IMAGE.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.drawImage(TOUR_AWARD_IMAGE, 0, 0, null);
        int x = 920;
        int y = 325;
        graphics.setColor(COLOR);
        graphics.setFont(FONT);

        for (Integer card : airaTourAwardDto.getOneCards()) {
            String str = decimalFormat.format(card) + "人";
            Rectangle2D stringBounds = FONT.getStringBounds(str, graphics.getFontRenderContext());
            graphics.drawString(str, (int) (x - stringBounds.getWidth()), y);
            y += 100;
        }
        y = 975;
        for (Integer card : airaTourAwardDto.getTwoCards()) {
            String str = decimalFormat.format(card) + "人";
            Rectangle2D stringBounds = FONT.getStringBounds(str, graphics.getFontRenderContext());
            graphics.drawString(str, (int) (x - stringBounds.getWidth()), y);
            y += 100;
        }
        drawFooter(image, graphics);
        graphics.dispose();
        return image;
    }

    private static void drawFooter(BufferedImage image, Graphics2D graphics) {
        graphics.setFont(FOOTER_FONT);
        String footer = "查询时间: " + new SimpleDateFormat("MM-dd HH:mm").format(new Date());
        Rectangle2D stringBounds = FOOTER_FONT.getStringBounds(footer, graphics.getFontRenderContext());
        graphics.setColor(Color.WHITE);
        graphics.drawString(footer, (int) (image.getWidth() - stringBounds.getWidth() - 20),
                image.getHeight() - 12);
    }


}
