package moe.aira.onebot.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageUtil {
    private ImageUtil() {

    }

    public static String bufferImageToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(image, "png", output);
        return "base64://" + Base64.getEncoder().encodeToString(output.toByteArray());
    }

    public static String bufferImageToBase64(BufferedImage image, String formatName) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(image, formatName, output);
        return "base64://" + Base64.getEncoder().encodeToString(output.toByteArray());
    }

    public static BufferedImage bufferedImageToJpg(BufferedImage image) throws IOException {
        return bufferedImageToJpg(image, 1);
    }

    public static BufferedImage bufferedImageToJpg(BufferedImage image, double scale) throws IOException {
        int width = (int) (image.getWidth() * scale);
        int height = (int) (image.getHeight() * scale);
        BufferedImage image1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image1.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.drawImage(image, 0, 0, width, height, null);
        return image1;
    }

}
