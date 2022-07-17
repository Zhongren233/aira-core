package moe.aira.onebot.util;

import lombok.extern.slf4j.Slf4j;
import moe.aira.entity.aira.AiraEventRanking;
import moe.aira.entity.aira.AiraSSFEventRanking;
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
import java.text.SimpleDateFormat;

@Slf4j
public class AiraMeImageUtil {
    private static final BufferedImage IMAGE_TEMPLATE;
    private static final BufferedImage SSF_IMAGE_TEMPLATE;
    private static final Font FONT1 = new Font("Noto Sans SC Black", Font.PLAIN, 120);
    private static final Font FONT2 = new Font("Noto Sans SC Black", Font.PLAIN, 80);
    private static final Font FONT3 = new Font("Noto Sans SC Black", Font.PLAIN, 36);

    private static final Font FONT4 = new Font("Noto Sans SC Black", Font.PLAIN, 72);
    private static final Color FONT_COLOR = new Color(1, 14, 68);
    private static final Color BLUE = new Color(0, 183, 238);
    private static final Color YELLOW = new Color(255, 241, 0);

    static {
        BufferedImage IMAGE_TEMPLATE1;
        BufferedImage IMAGE_TEMPLATE2;
        try {
            IMAGE_TEMPLATE1 = ImageIO.read(new ClassPathResource("image/template/Aira-Me-2.png").getInputStream());
        } catch (IOException e) {
            log.error("读取模板错误", e);
            IMAGE_TEMPLATE1 = null;
        }
        try {
            IMAGE_TEMPLATE2 = ImageIO.read(new ClassPathResource("image/template/Aira-Me-SSF.png").getInputStream());
        } catch (IOException e) {
            log.error("读取模板错误", e);
            IMAGE_TEMPLATE2 = null;
        }
        SSF_IMAGE_TEMPLATE = IMAGE_TEMPLATE2;
        IMAGE_TEMPLATE = IMAGE_TEMPLATE1;
    }


    private AiraMeImageUtil() {

    }

    public static BufferedImage generatorImage(AiraEventRanking eventRanking) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String userName = eventRanking.getUserProfile().getUserName();
        PointRanking pointRanking = eventRanking.getPointRanking();
        if (pointRanking == null) {
            pointRanking = new PointRanking();
            pointRanking.setEventPoint(0);
            pointRanking.setEventRank(-1);
        }
        ScoreRanking scoreRanking = eventRanking.getScoreRanking();
        if (scoreRanking == null) {
            scoreRanking = new ScoreRanking();
            scoreRanking.setEventPoint(0);
            scoreRanking.setEventRank(-1);
        }
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

        //footer
        String format = new SimpleDateFormat("MM-dd HH:mm").format(eventRanking.getPointUpdateTime());
        graphics.setColor(Color.WHITE);
        graphics.setFont(FONT3);
        String timeString = "查询时间:" + format;
        Rectangle2D stringBounds = FONT3.getStringBounds(timeString, graphics.getFontRenderContext());
        graphics.drawString(timeString, (float) (image.getWidth() - stringBounds.getWidth() - 10), image.getHeight() - 10);
        graphics.dispose();
        return image;
    }

    public static BufferedImage generatorImage(AiraSSFEventRanking eventRanking) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String userName = eventRanking.getUserProfile().getUserName();
        PointRanking pointRanking = eventRanking.getPointRanking();
        if (pointRanking == null) {
            pointRanking = new PointRanking();
            pointRanking.setEventPoint(0);
            pointRanking.setEventRank(-1);
        }
        ScoreRanking redScoreRanking = eventRanking.getRedScoreRanking();
        if (redScoreRanking == null) {
            redScoreRanking = new ScoreRanking();
            redScoreRanking.setEventPoint(0);
            redScoreRanking.setEventRank(-1);
        }
        ScoreRanking whiteScoreRanking = eventRanking.getWhiteScoreRanking();
        if (whiteScoreRanking == null) {
            whiteScoreRanking = new ScoreRanking();
            whiteScoreRanking.setEventPoint(0);
            whiteScoreRanking.setEventRank(-1);
        }
        BufferedImage image = new BufferedImage(SSF_IMAGE_TEMPLATE.getWidth(), SSF_IMAGE_TEMPLATE.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        FontRenderContext fontRenderContext = graphics.getFontRenderContext();
        double x;

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(FONT_COLOR);
        graphics.setFont(FONT2);
        graphics.drawImage(SSF_IMAGE_TEMPLATE, 0, 0, null);
        graphics.drawString(userName, 300, 145);

        // 绘制point
        graphics.setFont(FONT1);
        String point = decimalFormat.format(pointRanking.getEventPoint());
        Rectangle2D pointBound = FONT1.getStringBounds(point, fontRenderContext);
        x = (image.getWidth() - pointBound.getWidth()) / 2;

        graphics.setColor(YELLOW);
        graphics.fillRect((int) x - 30, 640 - 165 - 30, (int) pointBound.getWidth() + 60, 60);
        graphics.setColor(FONT_COLOR);
        graphics.drawString(point, (float) x, 640 - 165);


        //POINT RANKING
        graphics.setFont(FONT2);
        String pointRank = decimalFormat.format(pointRanking.getEventRank()) + "位";
        Rectangle2D pointRankBound = FONT2.getStringBounds(pointRank, fontRenderContext);
        x = (image.getWidth() - pointRankBound.getWidth()) / 2;
        graphics.setColor(BLUE);
        graphics.fillRect((int) x - 30, 640 - 30, (int) pointRankBound.getWidth() + 60, 60);
        graphics.setColor(FONT_COLOR);
        graphics.drawString(pointRank, (float) x, 640);


        // 绘制red Score
        graphics.setFont(FONT2);
        String redScore = decimalFormat.format(redScoreRanking.getEventPoint());
        Rectangle2D redScoreBound = FONT2.getStringBounds(redScore, fontRenderContext);
        x = ((image.getWidth() - redScoreBound.getWidth()) / 2) - 285;

        graphics.setColor(new Color(226, 80, 80));
        graphics.fillRect((int) x - 15, 1176 - 165 - 15, (int) redScoreBound.getWidth() + 30, 45);
        graphics.setColor(FONT_COLOR);
        graphics.drawString(redScore, (float) x, 1176 - 165);

        // 绘制white Score
        graphics.setFont(FONT2);
        String whiteScore = decimalFormat.format(whiteScoreRanking.getEventPoint());
        Rectangle2D whiteScoreBound = FONT2.getStringBounds(whiteScore, fontRenderContext);
        x = ((image.getWidth() - whiteScoreBound.getWidth()) / 2) + 285;

        graphics.setColor(Color.WHITE);
        graphics.fillRect((int) x - 15, 1176 - 165 - 15, (int) whiteScoreBound.getWidth() + 30, 45);
        graphics.setColor(FONT_COLOR);
        graphics.drawString(whiteScore, (float) x, 1176 - 165);
        // 绘制red rank
        graphics.setFont(FONT4);
        String redRank = decimalFormat.format(redScoreRanking.getEventRank()) + "位";
        Rectangle2D redRankBound = FONT4.getStringBounds(redRank, fontRenderContext);
        x = ((image.getWidth() - redRankBound.getWidth()) / 2) - 285;

        graphics.setColor(BLUE);
        graphics.fillRect((int) x - 15, 1176 - 15, (int) redRankBound.getWidth() + 30, 45);
        graphics.setColor(FONT_COLOR);
        graphics.drawString(redRank, (float) x, 1176);

        // 绘制red rank
        graphics.setFont(FONT4);
        String whiteRank = decimalFormat.format(whiteScoreRanking.getEventRank()) + "位";
        Rectangle2D whiteRankBound = FONT4.getStringBounds(whiteRank, fontRenderContext);
        x = ((image.getWidth() - whiteRankBound.getWidth()) / 2) + 285;

        graphics.setColor(BLUE);
        graphics.fillRect((int) x - 15, 1176 - 15, (int) whiteRankBound.getWidth() + 30, 45);
        graphics.setColor(FONT_COLOR);
        graphics.drawString(whiteRank, (float) x, 1176);

        //footer
        String format = new SimpleDateFormat("MM-dd HH:mm").format(eventRanking.getPointUpdateTime());
        graphics.setColor(Color.WHITE);
        graphics.setFont(FONT3);
        String timeString = "查询时间:" + format;
        Rectangle2D stringBounds = FONT3.getStringBounds(timeString, graphics.getFontRenderContext());
        graphics.drawString(timeString, (float) (image.getWidth() - stringBounds.getWidth() - 10), image.getHeight() - 10);
        graphics.dispose();
        return image;
    }
}
