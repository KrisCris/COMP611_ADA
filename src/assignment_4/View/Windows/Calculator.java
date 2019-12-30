package assignment_4.View.Windows;

import assignment_4.Controller.CalculatorController;
import assignment_4.Model.Calculation;
import assignment_4.Model.Constants;
import assignment_4.View.Components.Display;
import assignment_4.View.Components.OperatorLabel;
import assignment_4.View.Components.SketchpadPanel;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

/**
 * View of calculator.
 */
public class Calculator extends JFrame implements Observer {

    private CalculatorController cc;

    private JPanel rightOpPanel;
    private JPanel topOpPanel;
    private JPanel bottomOpPanel;
    private SketchpadPanel handwritingPanel;
    private Display displayPanel;

    private OperatorLabel plus;
    private OperatorLabel minus;
    private OperatorLabel multi;
    private OperatorLabel div;

    private OperatorLabel leftBracket;
    private OperatorLabel rightBracket;
    private OperatorLabel mod;
    private OperatorLabel dot;
    private OperatorLabel equal;

    private OperatorLabel ac;

    private OperatorLabel recognize;
    private OperatorLabel clear;


    public Calculator() {
        initComponents();
        initLayout();
        initStyle();
    }

    private void initComponents() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.rightOpPanel = new JPanel();
        this.topOpPanel = new JPanel();
        this.bottomOpPanel = new JPanel();
        this.handwritingPanel = new SketchpadPanel();
        this.displayPanel = new Display();

        this.plus = new OperatorLabel("+");
        this.minus = new OperatorLabel("-");
        this.multi = new OperatorLabel("×");
        this.div = new OperatorLabel("÷");
        this.leftBracket = new OperatorLabel("(");
        this.rightBracket = new OperatorLabel(")");
        this.mod = new OperatorLabel("Mod");
        this.dot = new OperatorLabel(".");
        this.equal = new OperatorLabel("=");
        this.ac = new OperatorLabel("AC");
        this.clear = new OperatorLabel("Clear");
        this.recognize = new OperatorLabel("Recog");
    }

    private void initLayout() {

        this.setSize(252, 394);
        this.setResizable(false);

        /**
         * Button (Label)s
         */
        this.mod.setBounds(0, 0, 60, 60);
        this.leftBracket.setBounds(60, 0, 60, 60);
        this.rightBracket.setBounds(120, 0, 60, 60);
        this.ac.setBounds(180, 0, 72, 60);

        this.topOpPanel.setLayout(null);
        this.topOpPanel.add(mod);
        this.topOpPanel.add(leftBracket);
        this.topOpPanel.add(rightBracket);
        this.topOpPanel.add(ac);


        this.clear.setBounds(0, 0, 60, 60);
        this.recognize.setBounds(60, 0, 60, 60);
        this.dot.setBounds(120, 0, 60, 60);

        this.bottomOpPanel.setLayout(null);
        this.bottomOpPanel.add(clear);
        this.bottomOpPanel.add(recognize);
        this.bottomOpPanel.add(dot);


        this.div.setBounds(0, 0, 72, 48);
        this.multi.setBounds(0, 48, 72, 48);
        this.minus.setBounds(0, 96, 72, 48);
        this.plus.setBounds(0, 144, 72, 48);
        this.equal.setBounds(0, 192, 72, 48);

        this.rightOpPanel.setLayout(null);
        this.rightOpPanel.add(div);
        this.rightOpPanel.add(multi);
        this.rightOpPanel.add(minus);
        this.rightOpPanel.add(plus);
        this.rightOpPanel.add(equal);


        /**
         * Major panels
         */
        this.setLayout(null);
        this.displayPanel.setBounds(0, 0, 252, 72);
        this.topOpPanel.setBounds(0, 72, 252, 60);
        this.handwritingPanel.setBounds(0, 132, 180, 180);
        this.bottomOpPanel.setBounds(0, 312, 180, 60);
        this.rightOpPanel.setBounds(180, 132, 72, 240);
        this.add(rightOpPanel);
        this.add(topOpPanel);
        this.add(bottomOpPanel);
        this.add(handwritingPanel);
        this.add(displayPanel);

    }

    private void initStyle() {
        this.topOpPanel.setBackground(Constants.DK2_COLOR);
        this.bottomOpPanel.setBackground(Constants.DK3_COLOR);
        this.rightOpPanel.setBackground(Constants.ORANGE_L_COLOR);

        this.mod.setColor(Constants.DK2_COLOR, Constants.DK3_COLOR, Constants.DK1_COLOR);
        this.leftBracket.setColor(Constants.DK2_COLOR, Constants.DK3_COLOR, Constants.DK1_COLOR);
        this.rightBracket.setColor(Constants.DK2_COLOR, Constants.DK3_COLOR, Constants.DK1_COLOR);
        this.ac.setColor(Constants.DK2_COLOR, Constants.DK3_COLOR, Constants.DK1_COLOR);

        this.clear.setColor(Constants.DK3_COLOR, Constants.DK4_COLOR, Constants.DK2_COLOR);
        this.recognize.setColor(Constants.DK3_COLOR, Constants.DK4_COLOR, Constants.DK2_COLOR);
        this.dot.setColor(Constants.DK3_COLOR, Constants.DK4_COLOR, Constants.DK2_COLOR);

        this.div.setColor(Constants.ORANGE_H_COLOR, Constants.ORANGE_L_COLOR, Constants.ORANGE_M_COLOR);
        this.multi.setColor(Constants.ORANGE_H_COLOR, Constants.ORANGE_L_COLOR, Constants.ORANGE_M_COLOR);
        this.plus.setColor(Constants.ORANGE_H_COLOR, Constants.ORANGE_L_COLOR, Constants.ORANGE_M_COLOR);
        this.minus.setColor(Constants.ORANGE_H_COLOR, Constants.ORANGE_L_COLOR, Constants.ORANGE_M_COLOR);
        this.equal.setColor(Constants.ORANGE_H_COLOR, Constants.ORANGE_L_COLOR, Constants.ORANGE_M_COLOR);

        this.div.setFont(Constants.DISPLAY_SM3_FONT);
        this.multi.setFont(Constants.DISPLAY_SM3_FONT);
        this.plus.setFont(Constants.DISPLAY_SM3_FONT);
        this.minus.setFont(Constants.DISPLAY_SM3_FONT);
        this.equal.setFont(Constants.DISPLAY_SM3_FONT);

        this.leftBracket.setFont(Constants.DISPLAY_SM4_FONT);
        this.rightBracket.setFont(Constants.DISPLAY_SM4_FONT);
        this.dot.setFont(Constants.DISPLAY_SM4_FONT);


    }

    protected void bindListener() {
        this.addKeyListener(this.cc);
        /**
         * Bind buttons with mouse event listeners.
         */
        this.plus.addMouseListener(this.cc);
        this.minus.addMouseListener(this.cc);
        this.multi.addMouseListener(this.cc);
        this.div.addMouseListener(this.cc);
        this.leftBracket.addMouseListener(this.cc);
        this.rightBracket.addMouseListener(this.cc);
        this.mod.addMouseListener(this.cc);
        this.dot.addMouseListener(this.cc);
        this.equal.addMouseListener(this.cc);
        this.ac.addMouseListener(this.cc);
        this.recognize.addMouseListener(this.cc);
        this.clear.addMouseListener(this.cc);

        this.displayPanel.getTextField().addKeyListener(cc);
    }

    /**
     * Bind the controller as well as listeners to target components.
     *
     * @param controller
     */
    public void registerController(CalculatorController controller) {
        this.cc = controller;
        this.bindListener();
    }


    public void clearSketchPad() {
        this.handwritingPanel.clear();
    }

    public SketchpadPanel getHandwritingPanel() {
        return handwritingPanel;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Calculation) {
            Calculation c = (Calculation) o;
            JTextField txt = this.displayPanel.getTextField();

            if (c.getErrorIndex() < 0) {
                txt.setForeground(Constants.FORE_COLOR);
                String str = c.getFormula();
                txt.setText(str);

            } else {
                txt.setForeground(Constants.ERROR_COLOR);
            }

            int len = c.getFormula().length();
            fitDisplaySize(txt, len);

        }
    }

    /**
     * To adjust the font size so that the display will be able to fit all the characters.
     * @param txt
     * @param len
     */
    public void fitDisplaySize(JTextField txt, int len) {
        if (len > 11 && len < 13) {
            txt.setFont(Constants.DISPLAY_SM1_FONT);
        } else if (len >= 13 && len < 16) {
            txt.setFont(Constants.DISPLAY_SM2_FONT);
        } else if (len >= 16 && len < 19) {
            txt.setFont(Constants.DISPLAY_SM3_FONT);
        } else if (len >= 19 && len < 22) {
            txt.setFont(Constants.DISPLAY_SM4_FONT);
        } else if (len >= 22) {
            SwingUtilities.invokeLater(() -> {
                JScrollBar bar = displayPanel.getHorizontalScrollBar();
                bar.setValue(bar.getMaximum());
            });
        } else {
            txt.setFont(Constants.DISPLAY_DEFAULT_FONT);
        }
    }

}
