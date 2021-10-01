package net.xiaoyu233.superfirework.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.xiaoyu233.superfirework.SuperFirework;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Bitmap {
    private static final Map<String, boolean[][]> lettersMap = new HashMap<>();
    private static final Map<ResourceLocation,BufferedImage> imgCache = new HashMap<>();
    private static final Map<ResizeConfig,BufferedImage> resizeCache = new HashMap<>();
    private static final Map<String,Font> fontCache = new HashMap<>();
    public static boolean[][] getStringPixels(String fontName, int fontStyle, int fontSize, String s) {
        String key = fontName + "_" + fontStyle + "_" + fontSize + "_" + s;
        Font font;
        if (lettersMap.containsKey(key))
            return lettersMap.get(key);
        if (fontCache.containsKey(fontName)){
            font = fontCache.get(fontName);
        }else {
            try {
                InputStream inputStream = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(SuperFirework.MODID,"fonts/" + fontName +".ttf")).getInputStream();
                font = getSelfDefinedFont(inputStream);
            } catch (IOException e) {
                font = new Font(fontName, fontStyle, fontSize);
            }
            fontCache.put(fontName, font);
        }
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

    public static BufferedImage loadImage(String name){
        ResourceLocation imageLocation = new ResourceLocation(SuperFirework.MODID,"images/" + name + ".png");
        if (imgCache.containsKey(imageLocation)){
            return imgCache.get(imageLocation);
        }
        try {
            InputStream inputStream = Minecraft.getInstance().getResourceManager().getResource(imageLocation).getInputStream();
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        imgCache.put(imageLocation,image);
        return image;
    }

    public static BufferedImage zoomInImage(BufferedImage originalImage, int maxWidth, int maxHeight) {
        ResizeConfig resizeConfig = new ResizeConfig(originalImage, maxWidth, maxHeight);
        if (resizeCache.containsKey(resizeConfig)){
            return resizeCache.get(resizeConfig);
        }
        BufferedImage newImage = new BufferedImage(maxWidth, maxHeight, originalImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage, 0, 0, maxWidth, maxHeight, null);
        g.dispose();
        resizeCache.put(resizeConfig, newImage);
        return newImage;
    }

    private static class ResizeConfig{
        private BufferedImage src;
        private int maxWidth,maxHeight;

        private ResizeConfig(BufferedImage src, int maxWidth, int maxHeight) {
            this.src = src;
            this.maxWidth = maxWidth;
            this.maxHeight = maxHeight;
        }
    }

    private static Font getSelfDefinedFont(InputStream stream){
        Font font;
        try{
            font = Font.createFont(Font.TRUETYPE_FONT, stream);
            font = font.deriveFont(Font.PLAIN, 40);
        } catch (FontFormatException | IOException e){
            return null;
        }
        return font;
    }

}
