package moe.aira.onebot.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import moe.aira.onebot.config.AiraConfig;
import moe.aira.onebot.entity.AiraGachaResultDto;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

@Slf4j
@UtilityClass
public class AiraGachaImageUtil {
    private final static String assetPath;

    private final BufferedImage rainbowBackground;
    private final BufferedImage goldenBackground;
    private final BufferedImage background;

    static {
        assetPath = AiraConfig.getAssetsPath();
        BufferedImage temp = null;
        try {
            temp = ImageIO.read(Path.of(AiraConfig.TEMPLATE_PATH, "gacha/gacha-rainbow.png").toFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
        rainbowBackground = temp;

        temp = null;
        try {
            temp = ImageIO.read(Path.of(AiraConfig.TEMPLATE_PATH, "gacha/gacha-golden.png").toFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
        goldenBackground = temp;

        temp = null;
        try {
            temp = ImageIO.read(Path.of(AiraConfig.TEMPLATE_PATH, "gacha/gacha.png").toFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
        background = temp;
    }

    public static BufferedImage generateImage(AiraGachaResultDto data) {
        log.info("Generating gacha image");
        long l = System.currentTimeMillis();
        BufferedImage backGround = switch (data.getType()) {
            case GOLDEN -> goldenBackground;
            case RAINBOW -> rainbowBackground;
            default -> background;
        };
        BufferedImage image = new BufferedImage(backGround.getWidth(), backGround.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.drawImage(backGround, 0, 0, null);
        int x = 90;
        int y = 110;
        for (Map.Entry<String, Integer> entry : data.getCardIds()) {
            Integer cardId = entry.getValue();
            Image cardImage = readCardImage(cardId);
            if (cardImage != null) {
                graphics.drawImage(cardImage, x, y, null);
            }
            x += 210;
            if (x > 930) {
                x = 90;
                y = 345;
            }
        }
        log.info("Generating gacha image done, time: {}ms", System.currentTimeMillis() - l);
        return image;
    }

    private static Image readCardImage(Integer cardId) {
        File file = new File(assetPath + "/card/rectangle2/card_rectangle2_" + cardId + "_normal.png");
        if (file.isFile()) {
            try {
                BufferedImage read = ImageIO.read(file);
                if (read.getWidth() == 180) {
                    return read;
                } else {
                    return read.getScaledInstance(180, 225, Image.SCALE_SMOOTH);
                }
            } catch (IOException e) {
                log.error("Failed to read card image", e);
                return null;
            }
        } else {
            log.warn("Card image not found: {}", cardId);
            return null;
        }
    }
}


