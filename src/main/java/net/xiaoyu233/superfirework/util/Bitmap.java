package net.xiaoyu233.superfirework.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Bitmap {
    private static final Map<String, boolean[][]> lettersMap = new HashMap<>();
    public static boolean[][] getStringPixels(String fontName, int fontStyle, int fontSize, String s) {
        String key = fontName + "_" + fontStyle + "_" + fontSize + "_" + s;
        if (lettersMap.containsKey(key))
            return lettersMap.get(key);
        Font font = new Font(fontName, fontStyle, fontSize);

        BufferedImage bi = new BufferedImage(fontSize * 8, fontSize * 50, BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.getGraphics();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int strHeight = fm.getAscent() + fm.getDescent() - 3;
        int strWidth = fm.stringWidth(s);
        g2d.drawString(s, 0, fm.getAscent() - fm.getLeading() - 1);
        boolean[][] b = new boolean[strHeight][strWidth];
        for (int y = 0; y < strHeight; y++) {
            for (int x = 0; x < strWidth; x++) {
                b[y][x] = bi.getRGB(x, y) == -1;
            }
        }
        lettersMap.put(key, b);
        return b;
    }

}
