package assignment_4.View.Components;

import assignment_4.Model.Constants;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;

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

//class HighlightCaret extends DefaultCaret {
//
//    private static final Highlighter.HighlightPainter unfocusedPainter =
//            new DefaultHighlighter.DefaultHighlightPainter(new Color(230, 230, 210));
//    private static final long serialVersionUID = 1L;
//    private boolean isFocused;
//
//    @Override
//    protected Highlighter.HighlightPainter getSelectionPainter() {
//        return isFocused ? super.getSelectionPainter() : unfocusedPainter;
//    }
//
//    @Override
//    public void setSelectionVisible(boolean hasFocus) {
//        if (hasFocus != isFocused) {
//            isFocused = hasFocus;
//            super.setSelectionVisible(false);
//            super.setSelectionVisible(true);
//        }
//    }
//}