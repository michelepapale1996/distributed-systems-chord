package Graphic;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MergeMultipleImages {
    public static BufferedImage joinBufferedImage(BufferedImage img1, BufferedImage img2) {
        int offset = 2;
        int width = img1.getWidth();
        int height = Math.max(img1.getHeight(), img2.getHeight()) + offset + img2.getWidth();
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();
        Color oldColor = g2.getColor();
        g2.setPaint(Color.BLACK);
        g2.fillRect(0, 0, width, height);
        g2.setColor(oldColor);
        g2.drawImage(img1, null, 0, 0);
        g2.drawImage(img2, null, 0, img1.getHeight() + offset);
        g2.dispose();
        return newImage;
    }
}
