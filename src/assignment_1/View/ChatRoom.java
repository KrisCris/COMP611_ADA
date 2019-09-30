package assignment_1.View;

import assignment_1.Controller.ClientController;
import assignment_1.Model.Client;
import assignment_1.Model.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.LinkedList;

public class ChatRoom extends JFrame implements ActionListener,CommonFunc {
    private ClientController controller;

    private static final int DEFAULT_WIDTH = 950;
    private static final int DEFAULT_HEIGHT = 700;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel userList;
    private JPanel chatContents;
    private JScrollPane userScrollPane;
    private JScrollPane chatScrollPane;
    private JScrollPane inputScrollPane;
    private JTextArea chatInput;
    private JButton user;
    private JButton receiver;
    private JButton sendBtn;
    private JPanel toolbar;
    private Object[] statusSelection;

    public ChatRoom() {
        this.statusSelection = new Object[]{"Online", "Stealth", "Offline"};
        this.leftPanel = new JPanel();
        this.rightPanel = new JPanel();
        this.userList = new JPanel();
        this.chatContents = new JPanel();
        this.userScrollPane = new JScrollPane();
        this.chatScrollPane = new JScrollPane();
        this.inputScrollPane = new JScrollPane();
        this.chatInput = new JTextArea();
        this.user = new JButton("");
        this.receiver = new JButton("Click names to start Chats");
//        this.you = new JButton("");
        this.sendBtn = new JButton("SEND");
        this.toolbar = new JPanel();

        this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.setLayout(new GridBagLayout());
        this.add(this.leftPanel, new GBC(0, 0, 1, 1).setFill(GBC.BOTH).setWeight(0, 100).setIpad(130, 0));
        this.add(this.rightPanel, new GBC(1, 0, 1, 1).setFill(GBC.BOTH).setWeight(100, 100).setIpad(720, 0));
        this.leftPanel.setLayout(new GridBagLayout());
        this.leftPanel.add(this.user, new GBC(0, 0, 1, 1).setFill(GBC.BOTH).setWeight(100, 0).setIpad(0, 25));
        this.leftPanel.add(this.userScrollPane, new GBC(0, 1, 1, 1).setFill(GBC.BOTH).setWeight(100, 100).setIpad(0, 675));
        this.rightPanel.setLayout(new GridBagLayout());
        this.rightPanel.add(this.receiver, new GBC(0, 0, 2, 1).setFill(GBC.BOTH).setWeight(100, 0).setIpad(0, 25));
//        this.rightPanel.add(this.you, new GBC(1, 0, 1, 1).setFill(GBC.BOTH).setWeight(100, 0).setIpad(0, 25));
        this.rightPanel.add(this.chatScrollPane, new GBC(0, 1, 2, 1).setFill(GBC.BOTH).setWeight(100, 100).setIpad(0, 325));
        this.rightPanel.add(this.inputScrollPane, new GBC(0, 2, 2, 1).setFill(GBC.BOTH).setWeight(100, 5).setIpad(0, 0));
        this.rightPanel.add(this.sendBtn, new GBC(2, 2, 1, 1).setFill(GBC.BOTH).setWeight(0, 100).setIpad(0, 0));
        this.rightPanel.add(this.toolbar, new GBC(2, 0, 1, 2).setFill(GBC.BOTH).setWeight(0, 100));
        this.userList.setLayout(new GridBagLayout());
        this.inputScrollPane.setViewportView(this.chatInput);
        this.userScrollPane.setViewportView(this.userList);
        this.chatScrollPane.setViewportView(this.chatContents);

        this.chatInput.setLineWrap(true);
        this.chatInput.setWrapStyleWord(true);

//        this.you.setEnabled(false);
        this.receiver.setEnabled(false);
        this.sendBtn.setEnabled(false);
        this.chatInput.setEnabled(false);

        this.chatContents.setLayout(new GridBagLayout());

        this.sendBtn.addActionListener(this);
        this.user.addActionListener(this);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    ChatRoom.this.controller.processWindowCloseEvent();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void enableChat(){
        this.chatInput.setEnabled(true);
        this.sendBtn.setEnabled(true);
        this.receiver.setEnabled(true);
    }
    public void setCurrentChatting(String target){
        this.receiver.setText(target);
    }
    public String getCurrentChatting(){
        return this.receiver.getText();
    }

    public void addChatContent(String content,GBC gbc){
        JButton msg = new JButton(content);
//        msg.setEnabled(false);
        this.chatContents.add(msg,gbc);
        JScrollBar scrollBar = chatScrollPane.getVerticalScrollBar();
        scrollBar.setValue((scrollBar.getMaximum()));
    }
    public void clearChatContents(){
        this.chatContents.removeAll();
        this.chatContents.repaint();
    }
    public void refreshChatContents(){
        this.chatContents.revalidate();
    }

    public void refreshUserList(){
        this.userList.revalidate();
    }
    public void clearUserList(){
        this.userList.removeAll();
        this.userList.repaint();
    }
    public void addUser(String username,GBC gbc){
        JButton user = new JButton(username);
        this.userList.add(user,gbc);
        /*
         * By adding actionListener directly to userBtn,
         * saves efforts finding which kind of btn it is.
         */
        user.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton userBtn = (JButton)e.getSource();
                ChatRoom.this.controller.processSwitchChatTargetEvent(userBtn.getText());
            }
        });
    }
    public void userListFiller(GBC gbc){
        this.userList.add(new JLabel(""),gbc);
    }


    public void init(String username){
        //TODO there can be a value to control the online status, like online,busy,etc.
        this.user.setText(username);
    }

    public void registerObserver(ClientController controller){
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == this.sendBtn){
            String target = this.getCurrentChatting();
            String content = this.chatInput.getText();
            try {
                this.controller.processSendChatMsgEvent(target,content);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if(source == this.user){
            try {
                this.controller.processSwitchStatusEvent(this.statusSelection);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    @Override
    public void alert(String alertMsg) {

    }

//    public static void main(String[] args) {
//        EventQueue.invokeLater(()->{
//            ChatRoom chatRoom = new ChatRoom();
//            chatRoom.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            chatRoom.setVisible(true);
//        });
//    }
}
