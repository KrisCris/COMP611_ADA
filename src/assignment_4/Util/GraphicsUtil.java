package assignment_4.Util;

import assignment_4.Model.Outline;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This class implements the Singleton pattern.
 */
public class GraphicsUtil {
    private static GraphicsUtil GRAPHICS_UTIL;

    private GraphicsUtil() {
    }

    public static GraphicsUtil getInstance() {
        if (GRAPHICS_UTIL == null)
            GRAPHICS_UTIL = new GraphicsUtil();
        return GRAPHICS_UTIL;
    }

    public BufferedImage panelToImage(JPanel panel) {
        int panelW = panel.getWidth();
        int panelH = panel.getHeight();
        BufferedImage image = new BufferedImage(panelW, panelH, BufferedImage.TYPE_INT_RGB);

        /**
         * print images on the panel to BufferedImage.
         */
        Graphics2D graphics2D = image.createGraphics();
        panel.print(graphics2D);
        graphics2D.dispose();

        return image;
    }

    public double[] imageToGrayMatrix(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        /**
         * Bit operation that split r, g, b from RGB of each pixel,
         * and turn it into the grayScale.
         */
        double[] grayMatrix = new double[height * width];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = (rgb & 0xff);
                double gray = Double.valueOf(r * 299 + g * 587 + b * 114 + 500) / 255000.0;

                grayMatrix[i * width + j] = gray;
            }
        }

        return grayMatrix;
    }

    public int[] garyToBinaryMatrix(double[] grayMatrix) {
        int[] binaryMatrix = new int[grayMatrix.length];
        /**
         * Set pixels with gray_level > 50% to 1;
         */
        for (int i = 0; i < grayMatrix.length; i++) {
            if (grayMatrix[i] < 0.5) {
                binaryMatrix[i] = 0;
            } else {
                binaryMatrix[i] = 1;
            }
        }
        return binaryMatrix;
    }

    public Outline getOutline(int[] binaryMatrix) {
        int edgeLen = (int) Math.sqrt(binaryMatrix.length);
        /**
         * Current pos
         */
        int x = 0;
        int y = 0;

        /**
         * minXY = pos of top left corner.
         * maxXY = pos of bottom right corner.
         */
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        /**
         * Iterate all pixels, to find the border of the figure.
         */
        for (int i = 0; i < binaryMatrix.length; i++) {
            y = i % edgeLen;
            x = i / edgeLen;
            /**
             * Update border information.
             */
            if (binaryMatrix[i] != 0) {
                if (x < minX) {
                    minX = x;
                } else if (x > maxX) {
                    maxX = x;
                }
                if (y < minY) {
                    minY = y;
                } else if (y > maxY) {
                    maxY = y;
                }
            }
        }

        /**
         * To get the side length of the square outline.
         */
        int figureLen = Math.max(maxX - minX, maxY - minY);

        return new Outline(minX, minY, figureLen);

    }

    /**
     * This method create a subImage of the panel image that only contains the figure.
     * @param image a image from the panel
     * @param outline the outline of figure
     * @return
     */
    public BufferedImage getFigureImage(BufferedImage image, Outline outline) {

        BufferedImage figureImage =
                image.getSubimage(
                        outline.getX(),
                        outline.getY(),
                        outline.getLength(),
                        outline.getLength()
                );
        return figureImage;
    }

    public BufferedImage compressImage(BufferedImage image, int h, int w){
        Image img = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        BufferedImage scaledImage =
                new BufferedImage(
                        img.getWidth(null),
                        img.getHeight(null),
                        BufferedImage.TYPE_INT_RGB
                );
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.drawImage(img,0,0,null);
        g2d.dispose();

        return scaledImage;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
