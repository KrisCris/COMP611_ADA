package assignment_4.View.Components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class Display extends JScrollPane {
    private JTextField textField;

    public Display(){
        textField = new JTextField();
        add(textField);
        textField.setBackground(Color.gray);
        this.setBackground(Color.yellow);
        this.setForeground(Color.yellow);
        this.setViewportView(this.textField);
        this.textField.setBorder(null);
        this.setBorder(null);
        this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.getHorizontalScrollBar().setForeground(Color.gray);
        this.getHorizontalScrollBar().setBackground(Color.BLACK);
        this.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.BLACK;
            }
        });
    }
}
