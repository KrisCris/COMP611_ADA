package assignment_4.View.Components;

import assignment_4.Model.Constants;
import assignment_4.Model.Outline;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

/**
 * The handwriting digit input panel.
 */
public class SketchpadPanel extends JPanel implements MouseMotionListener, MouseListener {

    /**
     * By paint the pen stroke line by line,
     * the discontinuity caused by apple trackpad can be solved.
     * drawnLines contains lines user drew.
     * drawnLine[1], [2] is last point
     * drawnLine[3], [4] is current point
     */
    private int lastX;
    private int lastY;
    private LinkedList<int[]> drawnLines;

    private int outlineX;
    private int outlineY;
    private int outlineW;
    private int outlineH;
    private boolean drawOutline;

    public SketchpadPanel() {
        this.initComponent();
        this.initData();
        this.bindListener();
    }

    private void initComponent() {
        this.setBackground(Constants.SP_COLOR);
    }

    private void initData() {
        this.lastX = 0;
        this.lastY = 0;
        this.drawnLines = new LinkedList<>();

        this.outlineH = 0;
        this.outlineW = 0;
        this.outlineX = 0;
        this.outlineY = 0;
        this.drawOutline = false;
    }

    private void bindListener() {
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
    }

    /**
     * To set a outline with some fancy animations. LOL
     */
    public void setOutLine(Outline outline) {
        int x = outline.getX();
        int y = outline.getY();
        int len = outline.getLength();
        new Thread(new Runnable() {
            public void run() {
                for (int i = 50; i >= 0; i--) {
                    try {
                        outlineX = x - i;
                        outlineY = y - i;
                        outlineW = len + i * 2;
                        outlineH = len + i * 2;
                        drawOutline = true;
                        repaint();
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    /**
     * clear the panel
     */
    public void clear() {
        initData();
        repaint();
    }

    /**
     * To draw pen strokes drew by user when being called.
     * When being repaint()ed.
     *
     * @param graphics
     */
    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        Graphics2D g = (Graphics2D) graphics;

        g.setBackground(Color.WHITE);

        /**
         * Draw what user draws.
         */
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(this.getWidth() / 28));
        for (int i = 0; i < drawnLines.size(); i++) {
            g.drawLine(
                    drawnLines.get(i)[0],
                    drawnLines.get(i)[1],
                    drawnLines.get(i)[2],
                    drawnLines.get(i)[3]
            );
        }

        /**
         * draw the fancy outline.
         */
        if (drawOutline == true) {
            g.setColor(Color.RED);
            g.setStroke(new BasicStroke(1));
            g.drawRect(outlineX, outlineY, outlineW, outlineH);
        }

        g.dispose();
    }

    /**
     * Set new line to be rendered.
     *
     * @param e
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        this.drawnLines.add(new int[]{
                this.lastX,
                this.lastY,
                e.getX(),
                e.getY(),
        });
        this.lastX = e.getX();
        this.lastY = e.getY();
        repaint();
    }

    /**
     * Update last click point when pressed,
     * to avoid starting a pen stroke from somewhere unexpected.
     *
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (drawOutline == true) {
            drawOutline = false;
            clear();
        }

        this.lastX = e.getX();
        this.lastY = e.getY();

    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
