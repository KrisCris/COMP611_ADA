package assignment_1.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 200;
    private JTextField nameInput;
    private JButton loginBtn;
    private JLabel info;

    public Login(){
        this.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
        this.nameInput = new JTextField();
        this.loginBtn = new JButton("Login");
        this.info = new JLabel("OFFLINE");
        this.setLayout(new BorderLayout());
        this.add(this.info,BorderLayout.NORTH);
        this.add(this.nameInput, BorderLayout.CENTER);
        this.add(this.loginBtn,BorderLayout.SOUTH);
    }

    public void setBtnListener(ActionListener actionListener){
        this.loginBtn.addActionListener(actionListener);
    }
    public String getUsername(){
        return this.nameInput.getText();
    }
    public void clearTextField(){
        this.nameInput.setText("");
    }
    public void setLabel(String s){
        this.info.setText(s);
    }

}
