package assignment_4.View.Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;


public class SketchpadPanel extends JPanel implements MouseMotionListener, MouseListener {
    public final int HEIGHT = 180;
    public final int WIDTH = 180;

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

    public SketchpadPanel() {
        this.initComponent();
        this.initData();
        this.bindListener();
    }

    private void initComponent() {
        this.setSize(HEIGHT, WIDTH);
        this.setBackground(Color.BLACK);
    }

    private void initData() {
        this.lastX = 0;
        this.lastY = 0;
        this.drawnLines = new LinkedList<>();
    }

    private void bindListener(){
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
    }

    /**
     * To draw pen strokes drew by user when being called.
     * When being repaint()ed.
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
        g.setStroke(new BasicStroke(10));
        for (int i = 0; i < drawnLines.size(); i++) {
            g.drawLine(
                    drawnLines.get(i)[0],
                    drawnLines.get(i)[1],
                    drawnLines.get(i)[2],
                    drawnLines.get(i)[3]
            );
        }

        g.dispose();
    }

    /**
     * Set new line to be rendered.
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
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {
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
