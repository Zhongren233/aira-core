package moe.aira.onebot.util;

import lombok.extern.slf4j.Slf4j;
import moe.aira.entity.aira.AiraEventPointDto;
import moe.aira.entity.aira.AiraEventScoreDto;
import moe.aira.onebot.config.AiraConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
public class AiraRankingImageUtil {
    private static final BufferedImage POINT_IMAGE_TEMPLATE;
    private static final BufferedImage SCORE_IMAGE_TEMPLATE;
    private static final Color PR_FONT_COLOR = new Color(1, 14, 68);
    private static final Color SR_FONT_COLOR = new Color(184, 126, 0);
    private static final Font FONT = new Font("Noto Sans SC Black", Font.PLAIN, 70);
    private static final Font FOOTER_FONT = new Font("Noto Sans SC Black", Font.PLAIN, 36);
    static final DecimalFormat decimalFormat = new DecimalFormat("#,###");

    static {
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(Path.of(AiraConfig.TEMPLATE_PATH, "Pt-Ranking.png").toFile());

        } catch (IOException e) {
            log.error("", e);
        }
        POINT_IMAGE_TEMPLATE = bufferedImage;

        try {
            bufferedImage = ImageIO.read(Path.of(AiraConfig.TEMPLATE_PATH, "Sr-Ranking.png").toFile());

        } catch (IOException e) {
            log.error("", e);
        }
        SCORE_IMAGE_TEMPLATE = bufferedImage;
    }

    private AiraRankingImageUtil() {
    }

    public static BufferedImage generatorPointImage(List<AiraEventPointDto> data) {
        Integer[] integers = data.stream().map(AiraEventPointDto::getPoint).toArray(Integer[]::new);
        return getBufferedImage(integers, POINT_IMAGE_TEMPLATE, PR_FONT_COLOR);
    }

    public static BufferedImage generatorScoreImage(List<AiraEventScoreDto> data) {
        Integer[] integers = data.stream().map(AiraEventScoreDto::getScore).toArray(Integer[]::new);
        return getBufferedImage(integers, SCORE_IMAGE_TEMPLATE, SR_FONT_COLOR);
    }

    public static BufferedImage generatorScoreImage(List<AiraEventScoreDto> data, Color color, String imagePath) throws IOException {
        Integer[] integers = data.stream().map(AiraEventScoreDto::getScore).toArray(Integer[]::new);
        return getBufferedImage(integers, ImageIO.read(new ClassPathResource(imagePath).getInputStream()), color);
    }

    @NotNull
    private static BufferedImage getBufferedImage(Integer[] integers, BufferedImage template, Color fontColor) {
        BufferedImage image = new BufferedImage(template.getWidth(), template.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.drawImage(template, 0, 0, null);
        graphics.setColor(fontColor);
        graphics.setFont(FONT);
        int y = 325;
        FontRenderContext fontRenderContext = graphics.getFontRenderContext();
        for (Integer integer : integers) {
            String format = decimalFormat.format(integer);
            Rectangle2D stringBounds = FONT.getStringBounds(format, fontRenderContext);
            graphics.drawString(format, (int) (image.getWidth() - 80 - stringBounds.getWidth()), y);
            y += 100;
        }
        graphics.setFont(FOOTER_FONT);
        String format = "查询时间:" + new SimpleDateFormat("MM-dd HH:mm").format(System.currentTimeMillis());
        graphics.setColor(Color.WHITE);
        Rectangle2D stringBounds = FOOTER_FONT.getStringBounds(format, graphics.getFontRenderContext());
        graphics.drawString(format, (int) (image.getWidth() - stringBounds.getWidth() - 20),
                image.getHeight() - 12);
        return image;
    }
}