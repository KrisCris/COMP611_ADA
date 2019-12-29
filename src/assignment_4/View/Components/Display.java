package assignment_4.View.Components;

import assignment_4.Model.Constants;

import javax.swing.*;
import java.awt.*;

public class Display extends JScrollPane {

    private JTextField textField;

    public Display() {
        this.initComponents();
        this.initLayout();
        this.initStyle();

    }

    private void initComponents() {
        this.textField = new JTextField();
        this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
    }

    private void initLayout() {
        this.add(textField);
        this.setViewportView(textField);


    }


    private void initStyle() {
        this.setBorder(null);
        this.textField.setBackground(Constants.DK0_COLOR);
        this.textField.setForeground(Constants.FORE_COLOR);
        this.textField.setCaretColor(Constants.FORE_COLOR);
        this.textField.setBorder(null);

    }

    public JTextField getTextField() {
        return textField;
    }
}
