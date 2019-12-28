package assignment_4.View.Windows;

import assignment_4.View.Components.Display;
import assignment_4.View.Components.OperatorLabel;
import assignment_4.View.Components.SketchpadPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class Calculator extends JFrame implements ActionListener, Observer {

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

    public Calculator() {
        initComponents();
        initLayout();
        setVisible(true);
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
        this.multi = new OperatorLabel("x");
        this.div = new OperatorLabel("/");
        this.leftBracket = new OperatorLabel("(");
        this.rightBracket = new OperatorLabel(")");
        this.mod = new OperatorLabel("mod");
        this.dot = new OperatorLabel(".");
        this.equal = new OperatorLabel("=");
    }

    private void initLayout() {

        this.setSize(252, 394);
       // this.setResizable(false);

        this.setLayout(null);
        this.displayPanel.setBounds(
                0,
                0,
                252,
                72
        );
        this.topOpPanel.setBounds(
                0,
                72,
                252,
                60
        );
        this.handwritingPanel.setBounds(
                0,
                132,
                180,
                180);
        this.bottomOpPanel.setBounds(
                0,
                312,
                180,
                60);
        this.rightOpPanel.setBounds(
                180,
                132,
                72,
                240
        );
        this.add(rightOpPanel);
        this.add(topOpPanel);
        this.add(bottomOpPanel);
        this.add(handwritingPanel);
        this.add(displayPanel);

        this.topOpPanel.setBackground(Color.pink);
        this.bottomOpPanel.setBackground(Color.pink);
        this.rightOpPanel.setBackground(Color.ORANGE);

    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public static void main(String[] args) {
        Calculator c = new Calculator();

    }
}
