package assignment_4.Controller;

import assignment_4.Model.Calculation;
import assignment_4.Model.Constants;
import assignment_4.Util.GraphicsUtil;
import assignment_4.Util.KNN;
import assignment_4.View.Components.OperatorLabel;
import assignment_4.View.Windows.Calculator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;

public class CalculatorController implements ActionListener, MouseMotionListener, MouseListener,  KeyListener {
    private Calculator view;
    private Calculation model;

    private KNN knnCore;

    public CalculatorController(Calculator view, Calculation model) {
        this.view = view;
        this.model = model;

        initEngine();
    }

    private void initEngine(){
        knnCore = KNN.getKnnCore();
    }

    public void start() {
        this.view.setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("clicked");
        if (e.getSource() instanceof OperatorLabel) {
            OperatorLabel op = (OperatorLabel) e.getSource();
            String str = op.getText();

            /**
             * Normal operators
             */
            if (str.equals("=")) {
                this.model.getResult();
            }

            if (str.equals("AC")) {
                this.model.clear();
                this.view.getHandwritingPanel().clear();
            }
            if (str.equals("+") ||
                    str.equals("-") ||
                    str.equals("×") ||
                    str.equals("÷") ||
                    str.equals("Mod")
            ) {
                if (str.equals("Mod")) {
                    this.model.addOperators("%");
                } else {
                    this.model.addOperators(str);
                }
            }
            if (op.getText().equals("(") || op.getText().equals(")")) {
                this.model.addBracket(op.getText());
            }
            if (op.getText().equals(".")) {
                this.model.addDot();
            }

            /**
             * Digit recognition
             */
            if (op.getText().equals("Clear")) {
                this.view.clearSketchPad();
            }
            if (op.getText().equals("Recog")) {
                GraphicsUtil GU = GraphicsUtil.getInstance();
                int[] target = GU.panelToBinaryFigureMatrix(view.getHandwritingPanel());
                if (target == null) return;

                int result = knnCore.getResult(target, 6);

                //TODO add digit into textField.
                this.model.addDigit(result + "");
            }


        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        String chars = "1234567890";
        String ops = "+/*-";
        String brackets = "()";
        if (chars.contains(e.getKeyChar() + "")) {
            model.addDigit(e.getKeyChar() + "");
        } else if (ops.contains(e.getKeyChar() + "")) {
            if (e.getKeyChar() == '*') {
                model.addOperators("×");
            } else if (e.getKeyChar() == '/') {
                model.addOperators("÷");
            } else {
                model.addOperators(e.getKeyChar() + "");
            }
        } else if (brackets.contains(e.getKeyChar() + "")) {
            model.addBracket(e.getKeyChar() + "");
        } else if (e.getKeyChar() == '=' || e.getKeyChar() == '\n') {
            model.getResult();
        } else if (e.getKeyChar() == '.') {
            model.addDot();
        } else {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_BACK_SPACE || key == KeyEvent.VK_DELETE || key == 0) {
                model.del();
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }


    @Override
    public void mousePressed(MouseEvent e) {

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

    @Override
    public void mouseDragged(MouseEvent e) {
        this.mouseClicked(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
