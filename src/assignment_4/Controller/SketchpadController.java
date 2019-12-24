package assignment_4.Controller;

import assignment_4.Model.FigureModel;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Observable;
import java.util.Observer;

public class SketchpadController implements MouseMotionListener {
    Observable model;
    Observer view;

    public void bind(Observable observable, Observer observer) {
        this.model = observable;
        this.view = view;

        ((JPanel)this.view).addMouseMotionListener(this);
        this.model.addObserver(view);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        System.out.println("x: " + x + " y: " + y);
        ((FigureModel) model).updateMatrix(x, y);

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
