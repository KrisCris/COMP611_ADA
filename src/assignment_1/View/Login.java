package assignment_1.View;

import assignment_1.Controller.ClientController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;

public class Login extends JFrame implements ActionListener,CommonFunc{
    private static final int DEFAULT_WIDTH = 250;
    private static final int DEFAULT_HEIGHT = 350;
    private JTextField nameInput;
    private JButton loginBtn;

    private ClientController controller;

    public Login(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
        this.nameInput = new JTextField();
        this.loginBtn = new JButton("Login");
        this.setTitle("OFFLINE");
        this.setLayout(null);
        this.nameInput.setBounds(50,50,150,30);
        this.loginBtn.setBounds(50,200,150,50);
        /*
         * Auto show and remove tips on the username input bar.
         */
        this.nameInput.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if((nameInput.getText().equals("Enter Username")
                        && nameInput.getForeground()==Color.gray)
                        || nameInput.getForeground()==Color.RED){
                    nameInput.setText("");
                    nameInput.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(nameInput.getText().equals("")){
                    nameInput.setForeground(Color.gray);
                    nameInput.setText("Enter Username");
                }
            }
        });
        this.add(this.nameInput);
        this.add(this.loginBtn);

        this.loginBtn.addActionListener(this);
    }

    public JTextField getNameInput() {
        return nameInput;
    }

    public void setNameInput(JTextField nameInput) {
        this.nameInput = nameInput;
    }

    public void registerObserver(ClientController controller){
        this.controller = controller;
    }

    public void reset(){
        this.setTitle("OFFLINE");
        this.nameInput.setText("");
    }

    @Override
    public void alert(String alertMsg){
        this.nameInput.setText(alertMsg);
        this.nameInput.setForeground(Color.RED);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /*
         * Set cursor to indicate computation on-going; this matters only if
         * processing the event might take a noticeable amount of time as seen
         * by the user.
         */
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        Object source = e.getSource();
        if(source == this.loginBtn){
            try {
                this.controller.processLoginEvent();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else{ ; }
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
}
