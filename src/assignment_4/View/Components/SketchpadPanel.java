package assignment_4.View.Components;

import assignment_4.Model.FigureModel;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class SketchpadPanel extends JPanel implements Observer {
    public final int HEIGHT = 180;
    public final int WIDTH = 180;

    private Graphics2D graphics2D;

    public SketchpadPanel() {
        this.initComponent();
        this.setVisible(true);
        this.graphics2D = (Graphics2D) this.getGraphics();
    }

    private void initComponent() {
        this.setSize(HEIGHT, WIDTH);
        this.setBackground(Color.BLACK);
    }

    public void paint(int x, int y) {
        this.graphics2D.setColor(Color.white);
        this.graphics2D.fillRect(x, y, 10, 10);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof FigureModel) {
            FigureModel fm = (FigureModel) arg;
            this.paint(fm.getCurrentPointX(), fm.getCurrentPointY());
        }
    }

}
