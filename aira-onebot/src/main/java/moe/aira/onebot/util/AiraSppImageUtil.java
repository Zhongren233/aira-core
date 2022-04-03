package moe.aira.onebot.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import moe.aira.enums.ColorType;
import moe.aira.onebot.entity.AiraCardSppDto;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@UtilityClass
@Slf4j
public class AiraSppImageUtil {

    private final static BufferedImage content;
    private final static BufferedImage footer;

    private final static String assetPath;
    private static final Color FONT_COLOR = new Color(1, 14, 68);
    private static final Font songJpFont = new Font("Noto Sans SC", Font.BOLD, 36);
    private static final Font songCnFont = new Font("Noto Sans SC", Font.BOLD, 28);

    static {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            assetPath = "C:/Users/sc/Documents/Tencent Files/732713726/FileRecv/assets";
        } else {
            assetPath = "/var/www/assets";
        }
        BufferedImage temp = null;
        try {
            temp = ImageIO.read(new ClassPathResource("/image/template/spp-content.png").getInputStream());
        } catch (IOException e) {
            log.error("Failed to load spp-content.png", e);
        }
        content = temp;
        try {
            temp = ImageIO.read(new ClassPathResource("/image/template/spp-footer.png").getInputStream());
        } catch (IOException e) {
            log.error("Failed to load spp-footer.png", e);
        }
        footer = temp;
    }

    public BufferedImage generateImage(List<AiraCardSppDto> data) throws IOException {
        log.info("Generating spp image...");
        long l = System.currentTimeMillis();
        int size = data.size();
        int height = content.getHeight();
        int width = content.getWidth();
        int totalHeight = (height * size) + footer.getHeight();
        BufferedImage image = new BufferedImage(width, totalHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(new Color(204, 224, 235));
        graphics.fillRect(0, 0, width, totalHeight);
        int currentY = 0;
        for (AiraCardSppDto datum : data) {
            BufferedImage content = generateCard(datum);
            graphics.drawImage(content, 0, currentY, null);
            currentY += content.getHeight();
        }
        graphics.drawImage(footer, 0, currentY, null);
        graphics.dispose();
        log.info("Spp image generated. cost {}ms", System.currentTimeMillis() - l);
        return image;
    }

    private static BufferedImage generateCard(AiraCardSppDto datum) throws IOException {
        BufferedImage image = new BufferedImage(content.getWidth(), content.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setComposite(AlphaComposite.Src);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.drawImage(content, 0, 0, null);
        // 读取卡面资源
        File cardPath = new File(assetPath + "/Card");
        File[] files = cardPath.listFiles(pathname -> {
            String cardId = datum.getCardId();
            if (cardId == null) {
                return false;
            }
            return pathname.getName().contains(cardId);
        });
        assert files != null;
        for (File asset : files) {
            if (asset.getName().contains("normal")) {
                graphics.drawImage(ImageIO.read(asset), 70, 100, 192, 192, null);
            } else if (asset.getName().contains("evolution")) {
                graphics.drawImage(ImageIO.read(asset), 285, 100, 192, 192, null);
            }
        }

        // 读取歌曲资源
        File songPath = new File(assetPath + "/Song");
        File[] songs = songPath.listFiles(pathname -> {
            String songId = datum.getSongId();
            if (songId == null) {
                return false;
            }
            return pathname.getName().endsWith('_' + songId + ".png");
        });
        assert songs != null;
        if (songs.length != 0) {
            graphics.drawImage(ImageIO.read(songs[0]), 515, 151, 150, 150, null);
        }
        if (datum.getSongColorType() != null && datum.getSongColorType() != ColorType.ALL) {
            graphics.setColor(new Color(datum.getSongColorType().rgb));
            Polygon p = new Polygon();
            int baseX = 1030;
            int baseY = 196;
            p.addPoint(baseX + 120, baseY);
            p.addPoint(baseX, baseY + 120);
            p.addPoint(baseX + 120, baseY + 120);
            graphics.fillPolygon(p);
        }
        graphics.setColor(Color.WHITE);
        // 卡片名
        graphics.setFont(new Font("Noto Sans SC", Font.BOLD, 22));
        String str = "[" + datum.getCardName() + "] " + datum.getIdolName();
        Rectangle2D stringBounds2 = graphics.getFont().getStringBounds(str, graphics.getFontRenderContext());
        graphics.drawString(str, (int) -(stringBounds2.getWidth() / 2) + 270, 62);
        graphics.setFont(songJpFont);
        graphics.setColor(FONT_COLOR);
        Rectangle2D stringBounds = graphics.getFont().getStringBounds(datum.getSongNameJp(), graphics.getFontRenderContext());
        double width = stringBounds.getWidth();
        graphics.drawString(datum.getSongNameJp(), (int) (820 - width / 2), 120);

        graphics.setFont(songCnFont);
        Rectangle2D stringBounds1 = graphics.getFont().getStringBounds(datum.getSongNameCn(), graphics.getFontRenderContext());
        double width1 = stringBounds1.getWidth();
        graphics.drawString(datum.getSongNameCn(), (int) (1140 - width1), 172);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (datum.getJpInstallTime() != null) {
            graphics.drawString("日服实装时间: " + simpleDateFormat.format(datum.getJpInstallTime()), 680, 255);
        }
        if (datum.getCnInstallTime() != null) {
            graphics.drawString("国服实装时间: " + simpleDateFormat.format(datum.getCnInstallTime()), 680, 295);
        }
        return image;
    }


}
