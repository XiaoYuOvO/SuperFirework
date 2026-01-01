package net.xiaoyu233.superfirework.util;

import com.google.common.base.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.xiaoyu233.superfirework.Superfirework;
import org.intellij.lang.annotations.MagicConstant;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Bitmap {
    private static final Map<String, boolean[][]> lettersMap = new HashMap<>();
    private static final Map<Identifier,BufferedImage> imgCache = new HashMap<>();
    private static final Map<ResizeConfig,BufferedImage> resizeCache = new HashMap<>();
    private static final Map<String,Font> fontCache = new HashMap<>();

    @Environment(EnvType.CLIENT)
    public static boolean[][] getStringPixels(String fontName, @MagicConstant(flags = {Font. PLAIN,Font. BOLD,Font. ITALIC})  int fontStyle, int fontSize, String s) {
        String fontKey = fontName + "_" + fontStyle + "_" + fontSize;
        String key = fontKey + "_" + s;
        Font font;
        if (lettersMap.containsKey(key))
            return lettersMap.get(key);
        if (fontCache.containsKey(fontKey)){
            font = (fontCache.get(fontKey));
        } else {
            if (Identifier.isPathValid(fontName.toLowerCase())) {
                font = MinecraftClient.getInstance()
                        .getResourceManager()
                        .getResource(Identifier.of(Superfirework.MOD_ID, "fonts/" + fontName.toLowerCase() + ".ttf"))
                        .map(resource -> {
                            try {
                                return getSelfDefinedFont(resource.getInputStream(), fontSize);
                            } catch (IOException | InvalidIdentifierException e) {
                                return null;
                            }
                        }).orElse(new Font(fontName, fontStyle, fontSize).deriveFont(Font.PLAIN, fontSize));
            }else font = new Font(fontName, fontStyle, fontSize).deriveFont(Font.PLAIN, fontSize);
            fontCache.put(fontKey, font);
        }
        Rectangle2D stringBounds = font.getStringBounds(s, new FontRenderContext(null, false, false));
        int strHeight = (int) stringBounds.getHeight();
        int strWidth = (int) stringBounds.getWidth();
        BufferedImage bi = new BufferedImage(strWidth, strHeight, BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.getGraphics();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
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

    public static void invalidateCaches(){
        fontCache.clear();
        resizeCache.clear();
        imgCache.clear();
    }

    public static BufferedImage loadImage(String name){
        Identifier imageLocation = Identifier.of(name);
        if (imgCache.containsKey(imageLocation)){
            return imgCache.get(imageLocation);
        }

        BufferedImage bufferedImage = MinecraftClient.getInstance()
                .getResourceManager()
                .getResource(imageLocation)
                .map(resource -> {
                    try {
                        return ImageIO.read(resource.getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .orElse(new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB));
        imgCache.put(imageLocation,bufferedImage);
        return bufferedImage;
    }

    public static BufferedImage zoomInImage(BufferedImage originalImage, int maxWidth, int maxHeight) {
        ResizeConfig resizeConfig = new ResizeConfig(originalImage, maxWidth, maxHeight);
        if (resizeCache.containsKey(resizeConfig)){
            return resizeCache.get(resizeConfig);
        }
        BufferedImage newImage = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage, 0, 0, maxWidth, maxHeight, null);
        g.dispose();
        resizeCache.put(resizeConfig, newImage);
        return newImage;
    }

    private record ResizeConfig(BufferedImage src, int maxWidth, int maxHeight) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ResizeConfig that = (ResizeConfig) o;
            return maxWidth == that.maxWidth && maxHeight == that.maxHeight && Objects.equal(src, that.src);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(src, maxWidth, maxHeight);
        }
    }

    private static Font getSelfDefinedFont(InputStream stream, float fontSize){
        Font font;
        try{
            font = Font.createFont(Font.TRUETYPE_FONT, stream);
            font = font.deriveFont(Font.PLAIN, fontSize);
        } catch (FontFormatException | IOException e){
            return null;
        }
        return font;
    }

}
