package moe.aira.api.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StoryToImageUtil {


    public static BufferedImage parseStoryToImage(String story) throws IOException {
        int imageWidth = 800; // 图片宽度（固定）
        int fontSize = 28; // 字体大小
        int lineHeight = 45; // 行高
        int margin = 15; // 边距

        // 读取文本文件
        List<String> lines = readTextFile(story);
        // 计算文本高度
        int lineCount = lines.size();
        int textHeight = lineCount * lineHeight + 2 * margin;
        // 创建动态高度的画布
        BufferedImage image = new BufferedImage(imageWidth, textHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 设置背景颜色
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, imageWidth, textHeight);

        // 设置字体和颜色
        g2d.setColor(Color.BLACK);
//            悠哉字体 中等
        g2d.setFont(new Font("悠哉字体 中等", Font.PLAIN, fontSize));

        // 绘制文本
        int y = margin + 20;
        for (String line : lines) {
            g2d.drawString(line, margin, y);
            y += lineHeight;
        }

        // 释放资源
        g2d.dispose();

        // 保存图片

        return image;
    }


    private static List<String> readTextFile(String story) {
        List<String> strings = story.lines().collect(Collectors.toList());
        ArrayList<String> list = new ArrayList<>();
        for (String string : strings) {
            // 如果当前行为空，直接添加到列表
            if (string.isEmpty()) {
                list.add("");
                continue;
            }

            // 每 27 个字符换行一次
            int widthCount = 0;
            ArrayList<Character> characters = new ArrayList<>();
            for (char c : string.toCharArray()) {
                if (c < 0x127) {
                    widthCount += 1;
                } else {
                    widthCount += 2;
                }
                if (c == ' ' || c == '　') {
                    continue;
                }
                characters.add(c);
                if (widthCount >= 54) {
                    String collect = characters.stream().map(Object::toString).collect(Collectors.joining(""));
                    list.add(collect);
                    widthCount = 0;
                    characters.clear();
                }

            }
            if (widthCount > 0) {
                String collect = characters.stream().map(Object::toString).collect(Collectors.joining(""));
                list.add(collect);
                widthCount = 0;
                characters.clear();
            }

        }
        return list;
    }
}
