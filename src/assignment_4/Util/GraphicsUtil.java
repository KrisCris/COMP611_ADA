package assignment_4.Util;

import assignment_4.Model.Outline;
import assignment_4.View.Components.SketchpadPanel;

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
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = (rgb & 0xff);
                double gray = Double.valueOf(r * 299 + g * 587 + b * 114 + 500) / 255000.0;

                grayMatrix[y * width + x] = gray;
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
            y = i / edgeLen;
            x = i % edgeLen;
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

        int figureLen = Math.max(maxX - minX, maxY - minY);

        System.out.println(minX +"\t" + minY +"\t"+figureLen);
        return new Outline(minX, minY, figureLen);

    }

    /**
     * This method create a subImage of the panel image that only contains the figure.
     *
     * @param image   a image from the panel
     * @param outline the outline of figure
     * @return
     */
    public BufferedImage getFigureImage(BufferedImage image, Outline outline) {
        int x = outline.getX();
        int y = outline.getY();
        int lenX = outline.getLength();
        int lenY = outline.getLength();

        /**
         * To avoid the outline exceeds the img size;
         */
        if (x + lenX > image.getWidth()) {
            lenX = image.getWidth() - x;
        }
        if (y + lenY > image.getHeight()) {
            lenY = image.getHeight() - y;
        }

        BufferedImage figureImage =
                image.getSubimage(
                        x,
                        y,
                        lenX,
                        lenY
                );
        int len = Math.max(lenX,lenY);
        BufferedImage newImg = new BufferedImage(len,len,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = newImg.createGraphics();
        g2d.setBackground(Color.black);
        g2d.drawImage(figureImage,0,0,null);
        g2d.dispose();

        return newImg;
    }

    public BufferedImage compressImage(BufferedImage image, int h, int w) {
        Image img = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        BufferedImage scaledImage =
                new BufferedImage(
                        img.getWidth(null),
                        img.getHeight(null),
                        BufferedImage.TYPE_INT_RGB
                );
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return scaledImage;
    }

    /**
     * An all in one method.
     * Supported by methods above.
     * @param panel
     * @return
     */
    public int[] panelToBinaryFigureMatrix(SketchpadPanel panel) {
        BufferedImage panelImage = this.panelToImage(panel);

        double[] grayM = this.imageToGrayMatrix(panelImage);
        int[] binaryM = this.garyToBinaryMatrix(grayM);

        Outline outline = this.getOutline(binaryM);
        if (outline.getX() == 2147483647 || outline.getY() == 	2147483647){
            return null;
        }
        panel.setOutLine(outline);

        BufferedImage figureImage = this.compressImage(
                this.getFigureImage(
                        panelImage,
                        outline
                ),
                20,
                20
        );

        int[] binaryFigure =
                this.garyToBinaryMatrix(
                        this.imageToGrayMatrix(figureImage)
                );

        return binaryFigure;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
