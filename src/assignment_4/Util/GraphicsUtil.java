package assignment_4.Util;

import assignment_4.Model.Outline;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This class implements the Singleton pattern.
 */
public class GraphicsUtil {
    private static GraphicsUtil graphicUtil;

    private GraphicsUtil() {
    }

    public static GraphicsUtil getInstance() {
        if (graphicUtil == null)
            graphicUtil = new GraphicsUtil();
        return graphicUtil;
    }


    public double[] panelToGrayMatrix(JPanel panel) {
        int panelW = panel.getWidth();
        int panelH = panel.getHeight();

        double[] grayMatrix = new double[panelH * panelW];
        BufferedImage image = new BufferedImage(panelW, panelH, BufferedImage.TYPE_BYTE_GRAY);

        /**
         * print images on the panel to BufferedImage image.
         */
        Graphics2D graphics2D = image.createGraphics();
        panel.print(graphics2D);
        graphics2D.dispose();

        grayMatrix = (double[]) image.getRaster().getDataElements(0, 0, panelW, panelH, grayMatrix);

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

    public Outline getOutline(double[] binaryMatrix) {
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
        int minX = Integer.MIN_VALUE;
        int minY = Integer.MIN_VALUE;
        int maxX = Integer.MAX_VALUE;
        int maxY = Integer.MAX_VALUE;

        /**
         * Iterate all pixels, to find the border of the figure.
         */
        for (int i = 0; i < binaryMatrix.length; i++) {
            x = i % edgeLen;
            y = i / edgeLen;
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

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
