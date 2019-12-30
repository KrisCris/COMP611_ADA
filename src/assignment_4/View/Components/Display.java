package assignment_4.View.Components;

import assignment_4.Model.Constants;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * The display of calculator.
 */
public class Display extends JScrollPane {

    private JTextField textField;

    public Display() {
        this.initComponents();
        this.initLayout();
        this.initStyle();

    }

    private void initComponents() {
        this.textField = new JTextField("0");
        this.textField.setEnabled(true);
        this.textField.setFocusable(false);
        this.textField.setHorizontalAlignment(SwingConstants.RIGHT);

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
        this.textField.setFont(Constants.DISPLAY_DEFAULT_FONT);
        Border empty = new EmptyBorder(0, 0, 0, 6);
        this.textField.setBorder(empty);

    }

    public JTextField getTextField() {
        return textField;
    }
}