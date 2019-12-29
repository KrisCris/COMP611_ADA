package assignment_4.View.Components;

import assignment_4.Model.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class OperatorLabel extends JLabel implements MouseListener, MouseMotionListener {
    private Color defaultColor;
    private Color enteredColor;
    private Color pressedColor;

    public OperatorLabel(String op) {
        super(op);
        this.setForeground(Constants.FORE_COLOR);
        this.setOpaque(true);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    public void setColor(Color defaultColor, Color enteredColor, Color pressedColor) {
        this.defaultColor = defaultColor;
        this.enteredColor = enteredColor;
        this.pressedColor = pressedColor;

        this.setBackground(defaultColor);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.setBackground(this.pressedColor);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.setBackground(this.enteredColor);
        int x = e.getX();
        int y = e.getY();

        if (x < 0 || x > this.getWidth()) {
            this.mouseExited(e);
        }
        if (y < 0 || y > this.getHeight()) {
            this.mouseExited(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.setBackground(this.enteredColor);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.setBackground(this.defaultColor);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
