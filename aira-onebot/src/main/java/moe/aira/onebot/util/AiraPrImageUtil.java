package moe.aira.onebot.util;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class AiraPrImageUtil {
    private static final Color LINE_COLOR = new Color(1, 14, 68);
    private static final Font FONT1 = new Font("Noto Sans SC Black", Font.PLAIN, 50);
    private static final Font FONT2 = new Font("Noto Sans SC Black", Font.PLAIN, 12);

    private AiraPrImageUtil() {
    }

    public static BufferedImage generatorImage(Map<Integer, Integer> data) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        List<Map.Entry<Integer, Integer>> collect = data.entrySet().stream().filter(entry -> entry.getValue() > 0).toList();
        int width = 700;
        int height = collect.size() * 70 + 60 + 70;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(177, 213, 243));
        g.fillRect(0, 0, width, height);

        g.setColor(Color.BLACK);
        int x = 30;
        int y = 100;
        g.setStroke(new BasicStroke(6));
        g.setColor(LINE_COLOR);
        g.setFont(FONT1);


        FontRenderContext fontRenderContext = g.getFontRenderContext();
        String title = "当前活动点数";
        g.setColor(new Color(11, 98, 171));
        Rectangle2D titleSharp = FONT1.getStringBounds(title, fontRenderContext);
        int x1 = (int) ((image.getWidth() - titleSharp.getWidth()) / 2);
        g.fillRect(x + 20, 12, 600, (int) (titleSharp.getHeight()));

        g.setColor(Color.WHITE);
        g.drawString(title, x1, 65);

        g.setColor(LINE_COLOR);
        for (Map.Entry<Integer, Integer> entry : collect) {
            //绘制框
            g.drawRect(x, y, 640, 70);
            g.drawRect(x, y, 200, 70);

            String formatRank = decimalFormat.format(entry.getKey());
            g.drawString(formatRank, (int) (215 - FONT1.getStringBounds(formatRank, fontRenderContext).getWidth()), y + 55);
            g.drawString(formatRank, (int) (215 - FONT1.getStringBounds(formatRank, fontRenderContext).getWidth()), y + 55);
            String formatPoint = decimalFormat.format(entry.getValue());
            g.drawString(formatPoint, (int) (655 - FONT1.getStringBounds(formatPoint, fontRenderContext).getWidth()), y + 55);

            y += 70;
        }
        String formatDate = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss").format(LocalDateTime.now());
        g.setColor(new Color(121, 20, 20));
        g.setFont(FONT2);
        g.drawString(formatDate, image.getWidth() - 100, image.getHeight() - 10);
        return image;
    }
}
