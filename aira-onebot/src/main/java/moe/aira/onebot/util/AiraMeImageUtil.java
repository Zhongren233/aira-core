package moe.aira.onebot.util;

import lombok.extern.slf4j.Slf4j;
import moe.aira.entity.aira.AiraEventRanking;
import moe.aira.entity.es.PointRanking;
import moe.aira.entity.es.ScoreRanking;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;

@Slf4j
public class AiraMeImageUtil {
    private static final BufferedImage IMAGE_TEMPLATE;
    private static final Font FONT1 = new Font("Noto Sans SC Black", Font.PLAIN, 120);
    private static final Font FONT2 = new Font("Noto Sans SC Black", Font.PLAIN, 80);
    private static final Color FONT_COLOR = new Color(1, 14, 68);
    private static final Color BLUE = new Color(0, 183, 238);
    private static final Color YELLOW = new Color(255, 241, 0);

    static {
        BufferedImage IMAGE_TEMPLATE1;
        try {
            IMAGE_TEMPLATE1 = ImageIO.read(new ClassPathResource("image/template/Aira-Me-1.png").getInputStream());
        } catch (IOException e) {
            log.error("读取模板错误", e);
            IMAGE_TEMPLATE1 = null;
        }
        IMAGE_TEMPLATE = IMAGE_TEMPLATE1;
    }


    private AiraMeImageUtil() {

    }

    public static BufferedImage generatorImage(AiraEventRanking eventRanking) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String userName = eventRanking.getUserProfile().getUserName();
        PointRanking pointRanking = eventRanking.getPointRanking();
        ScoreRanking scoreRanking = eventRanking.getScoreRanking();

        BufferedImage image = new BufferedImage(IMAGE_TEMPLATE.getWidth(), IMAGE_TEMPLATE.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(FONT_COLOR);
        graphics.setFont(FONT2);
        graphics.drawImage(IMAGE_TEMPLATE, 0, 0, null);
        graphics.drawString(userName, 300, 145);

        graphics.setFont(FONT2);
        FontRenderContext fontRenderContext = graphics.getFontRenderContext();
        double x;
        // 绘制积分Ranking
        String pointRank = "Ranking:" + decimalFormat.format(pointRanking.getEventRank());
        Rectangle2D pointRankBound = FONT2.getStringBounds(pointRank, fontRenderContext);
        x = (image.getWidth() - pointRankBound.getWidth()) / 2;
        graphics.setColor(BLUE);
        graphics.fillRect((int) x - 30, 640 - 30, (int) pointRankBound.getWidth() + 60, 60);
        graphics.setColor(FONT_COLOR);
        graphics.drawString(pointRank, (float) x, 640);
        //绘制歌曲Ranking
        String scoreRank = "Ranking:" + decimalFormat.format(scoreRanking.getEventRank());
        Rectangle2D scoreRankBound = FONT2.getStringBounds(scoreRank, fontRenderContext);
        x = (image.getWidth() - scoreRankBound.getWidth()) / 2;
        graphics.setColor(BLUE);
        graphics.fillRect((int) x - 30, 1176 - 30, (int) scoreRankBound.getWidth() + 60, 60);
        graphics.setColor(FONT_COLOR);
        graphics.drawString(scoreRank, (float) x, 1176);
        graphics.setFont(FONT1);

        // 绘制积分
        String point = decimalFormat.format(pointRanking.getEventPoint());
        Rectangle2D pointBound = FONT1.getStringBounds(point, fontRenderContext);
        x = (image.getWidth() - pointBound.getWidth()) / 2;

        graphics.setColor(YELLOW);
        graphics.fillRect((int) x - 30, 640 - 165 - 30, (int) pointBound.getWidth() + 60, 60);
        graphics.setColor(FONT_COLOR);
        graphics.drawString(point, (float) x, 640 - 165);
        // 绘制歌曲
        String score = decimalFormat.format(scoreRanking.getEventPoint());
        Rectangle2D scoreBound = FONT1.getStringBounds(score, fontRenderContext);
        x = (image.getWidth() - scoreBound.getWidth()) / 2;

        graphics.setColor(YELLOW);
        graphics.fillRect((int) x - 30, 1176 - 165 - 30, (int) scoreBound.getWidth() + 60, 60);
        graphics.setColor(FONT_COLOR);
        graphics.drawString(score, (float) x, 1176 - 165);
        graphics.dispose();
        return image;
    }

}
