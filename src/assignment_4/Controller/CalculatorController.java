package assignment_4.Controller;

import assignment_4.Model.Calculation;
import assignment_4.Util.GraphicsUtil;
import assignment_4.Util.KNN;
import assignment_4.View.Components.OperatorLabel;
import assignment_4.View.Windows.Calculator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.event.*;

public class CalculatorController implements ActionListener, MouseMotionListener, MouseListener, DocumentListener {
    Calculator view;
    Calculation model;

    public CalculatorController(Calculator view, Calculation model) {
        this.view = view;
        this.model = model;
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
            int pos = this.view.getCursorPosition();
            System.out.println(pos);

            /**
             * Normal operators
             */
            if (str.equals("=")) {
                this.model.getResult();
            }

            if (str.equals("AC")) {
                this.model.clear();
            }
            if (str.equals("+") ||
                    str.equals("-") ||
                    str.equals("×") ||
                    str.equals("÷") ||
                    str.equals("Mod")
            ) {
                this.model.addOperators("str", pos);
            }
            if (op.getText().equals("(")) {

            }
            if (op.getText().equals(")")) {

            }
            if (op.getText().equals(".")) {
                this.model.addDot(pos);
            }

            /**
             * Digit recognition
             */
            if (op.getText().equals("Clear")) {
                this.view.clearSketchPad();
            }
            if (op.getText().equals("Recog")) {
                GraphicsUtil GU = GraphicsUtil.getInstance();
                KNN knnCore = KNN.getKnnCore();
                int[] target = GU.panelToBinaryFigureMatrix(view.getHandwritingPanel());
                if (target == null) return;

                int result = knnCore.getResult(target, 6);

                JOptionPane.showMessageDialog(null, "Estimated: " + result, "result", JOptionPane.INFORMATION_MESSAGE);
                //TODO add digit into textField.
                this.model.addDigit(result + "", pos);
            }


        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        try {
            this.model.validateChange(e.getDocument().getText(0, e.getDocument().getLength()));
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        try {
            this.model.validateChange(e.getDocument().getText(0, e.getDocument().getLength()));
        } catch (BadLocationException ex) {
            ex.printStackTrace();
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

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }


}
