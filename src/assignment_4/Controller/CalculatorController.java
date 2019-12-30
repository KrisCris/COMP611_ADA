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

/**
 * Controller of calculator.
 */
public class CalculatorController implements MouseListener, KeyListener {
    private Calculator view;
    private Calculation model;

    private KNN knnCore;
    private GraphicsUtil GU;

    private Object pressed;

    public CalculatorController(Calculator view, Calculation model) {
        this.view = view;
        this.model = model;

        initEngine();
    }

    /**
     * Get those singleton instances before hand.
     */
    private void initEngine() {
        knnCore = KNN.getKnnCore();
        GU = GraphicsUtil.getInstance();
    }

    public void start() {
        this.view.setVisible(true);
    }

    /**
     * Use mousePressed + mouseReleased instead of mouseClicked,
     * which makes the click event much easier to trigger.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        pressed = e.getSource();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        /**
         * If the event source is the same as the one being pressed, it means a operator label is clicked.
         */
        if (e.getSource().equals(this.pressed)) {
            if (e.getSource() instanceof OperatorLabel) {

                /**
                 * If the cursor already moved outside the target,
                 * it will do nothing.
                 */
                if (x < 0 || x > ((OperatorLabel) e.getSource()).getWidth()) {
                    return;
                }
                if (y < 0 || y > ((OperatorLabel) e.getSource()).getHeight()) {
                    return;
                }

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
                        str.equals("Mod")) {
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
                    int[] target = GU.panelToBinaryFigureMatrix(view.getHandwritingPanel());
                    /**
                     * Do nothing if the sketchpad is blank.
                     */
                    if (target == null) return;

                    int result = knnCore.getResult(target, 4);
                    this.model.addDigit(result + "");
                }

            }
        }
    }

    /**
     * Enable user to type in operators and digits.
     * Also delete is available, simply click backspace or del.
     */
    @Override
    public void keyTyped(KeyEvent e) {
        String chars = "1234567890";
        String ops = "+/*-%";
        String brackets = "()";
        String input = e.getKeyChar() + "";
        if (chars.contains(input)) {
            model.addDigit(input);
        } else if (ops.contains(input)) {
            if (input.equals("*")) {
                model.addOperators("×");
            } else if (input.equals("/")) {
                model.addOperators("÷");
            } else {
                model.addOperators(input);
            }
        } else if (brackets.contains(input)) {
            model.addBracket(input);
        } else if (input.equals("=") || input.equals("\n")) {
            model.getResult();
        } else if (input.equals(".")) {
            model.addDot();
        } else {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_BACK_SPACE ||
                    key == KeyEvent.VK_DELETE ||
                    input.equals("\b")||
                    e.getKeyChar() == 127) {
                model.del();
            }
        }

    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
