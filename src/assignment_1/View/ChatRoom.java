package assignment_1.View;

import assignment_1.Controller.ClientController;
import assignment_1.Model.Client;
import assignment_1.Model.Message;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import static javax.swing.ScrollPaneConstants.*;
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
    private JLabel onlineNum;

    private LinkedList<JButton> clients;
    private LinkedList<JButton> highlightedClients;
    public ChatRoom() {
        this.highlightedClients = new LinkedList<>();
        this.onlineNum = new JLabel("Clients: 0");
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
        this.receiver.setFont(new Font(Font.SANS_SERIF,Font.BOLD,18));
        this.sendBtn = new JButton("SEND");
        this.toolbar = new JPanel();
        this.setBackground(new Color(248, 248, 249));
        this.leftPanel.setBackground(new Color(248, 248, 249));
        this.rightPanel.setBackground(new Color(248, 248, 249));
        this.userList.setBackground(new Color(248, 248, 249));
        this.chatContents.setBackground(new Color(248, 248, 249));
        this.toolbar.setBackground(new Color(248, 248, 249));
        this.user.setFont(new Font(Font.SANS_SERIF,Font.ITALIC,15));
        this.user.setForeground(new Color(43, 132, 228));
        this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.setLayout(new GridBagLayout());
        this.add(this.leftPanel, new GBC(0, 0, 1, 1).setFill(GBC.BOTH).setWeight(0, 100).setIpad(130, 0));
        this.add(this.rightPanel, new GBC(1, 0, 1, 1).setFill(GBC.BOTH).setWeight(100, 100).setIpad(720, 0));
        this.leftPanel.setLayout(new GridBagLayout());
        this.leftPanel.add(this.user, new GBC(0, 0, 1, 1).setFill(GBC.BOTH).setWeight(100, 0).setIpad(0, 25));
        this.leftPanel.add(this.userScrollPane, new GBC(0, 1, 1, 1).setFill(GBC.BOTH).setWeight(100, 100).setIpad(0, 675));
        this.rightPanel.setLayout(new GridBagLayout());
        this.rightPanel.add(this.receiver, new GBC(0, 0, 2, 1).setFill(GBC.BOTH).setWeight(100, 0).setIpad(0, 25));
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

        this.receiver.setEnabled(false);
        this.sendBtn.setEnabled(false);
        this.chatInput.setEnabled(false);

        this.chatContents.setLayout(new GridBagLayout());
        this.chatContents.setBorder(new EmptyBorder(10, 10, 10, 10));

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

    public LinkedList<JButton> getClients() {
        return clients;
    }

    public void setClients(LinkedList<JButton> clients) {
        this.clients = clients;
    }

    public LinkedList<JButton> getHighlightedClients() {
        return highlightedClients;
    }

    public void setHighlightedClients(LinkedList<JButton> highlightedClients) {
        this.highlightedClients = highlightedClients;
    }

    public void setOnlineNumber(int num,int mode){
        this.onlineNum.setText("Client: "+num);
        this.onlineNum.setOpaque(true);
        this.onlineNum.setBorder(new EmptyBorder(0,25,0,15));
        this.switchModeColor(mode);
        GBC gbc = new GBC(0, 0, 1, 1).setWeight(100, 0).setFill(GBC.HORIZONTAL).setIpad(0, 25);
        this.userList.add(this.onlineNum,gbc);
    }
    public boolean getChatState(){
        return this.receiver.isEnabled();
    }
    public void alertOffline(){
        this.receiver.setText("[OFFLINE]  "+this.receiver.getText());
        this.disableChat();
    }
    public void enableChat(){
        this.chatInput.setEnabled(true);
        this.sendBtn.setEnabled(true);
        this.receiver.setEnabled(true);
    }
    public void disableChat(){
        this.chatInput.setEnabled(false);
        this.sendBtn.setEnabled(false);
        this.receiver.setEnabled(false);
    }
    public void setCurrentChatting(String target){
        this.receiver.setText(target);
    }
    public String getCurrentChatting(){
        return this.receiver.getText();
    }

    public void addChatContent(String content,GBC gbc){
        JLabel msg = new JLabel(content);
        msg.setBorder(new EmptyBorder(5,5,5,5));
        msg.setOpaque(true);
        if(gbc.gridx==1){
            msg.setBackground(new Color(92, 174, 255));
            msg.setForeground(new Color(248, 248, 249));
        } else {
            msg.setBackground(new Color(220,222,226));
        }
        this.chatContents.add(msg,gbc);
    }
    public void clearChatContents(){
        this.chatContents.removeAll();
        this.chatContents.repaint();
    }
    public void refreshChatContents(int index){
        this.dummyChatFiller(index);
        this.chatContents.revalidate();
        SwingUtilities.invokeLater(() -> {
            JScrollBar bar = chatScrollPane.getVerticalScrollBar();
            bar.setValue(bar.getMaximum());
        });
    }

    public void dummyChatFiller(int index){
        JLabel left = new JLabel("                                               ");
        JLabel right = new JLabel("                                               ");
        GBC gbcL = new GBC(0, index, 1, 1).setWeight(0, 1).setFill(GBC.HORIZONTAL);
        GBC gbcR = new GBC(1, index, 1, 1).setWeight(0, 1).setFill(GBC.HORIZONTAL);
        this.chatContents.add(left,gbcL);
        this.chatContents.add(right,gbcR);
    }
    public void highLight(String sender){
        for(JButton jb : this.clients){
            if(jb.getText().equals(sender)){
                if(!this.highlightedClients.contains(jb)){
                    this.highlightedClients.add(jb);
                    jb.setForeground(new Color(25, 190, 107));
                    jb.setFont(new Font(Font.SANS_SERIF,Font.BOLD,17));
                }
            }
        }
    }
    public void switchModeColor(int mode){
        System.out.println("MODE "+mode);
        if(mode == 1){
            this.onlineNum.setBackground(new Color(45, 182, 245));
            this.onlineNum.setForeground(Color.BLACK);
        } else if(mode==2){
            this.onlineNum.setBackground(new Color(23, 35, 61));
            this.onlineNum.setForeground(new Color(248, 248, 249));
        }
    }
    public void deHighLight(String target){
        for(JButton jb:this.highlightedClients){
            if(jb.getText().equals(target)){
                this.highlightedClients.remove(jb);
                jb.setForeground(Color.BLACK);
                jb.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,14));
                break;
            }
        }
    }
    public void refreshUserList(){
        this.userList.revalidate();
    }
    public void clearUserList(){
        this.userList.removeAll();
        this.clients = new LinkedList<>();
        this.userList.repaint();
    }
    public void addUser(String username,GBC gbc){
        JButton user = new JButton(username);
        user.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,14));
        this.userList.add(user,gbc);
        this.clients.add(user);
        /*
         * By adding actionListener directly to userBtn,
         * saves efforts finding which kind of btn it is.
         */
        user.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton userBtn = (JButton)e.getSource();
                userBtn.setEnabled(false);
                ChatRoom.this.controller.processSwitchChatTargetEvent(userBtn.getText());
            }
        });
    }

    public void rebuildUserList(GBC gbc){
        this.userList.add(new JLabel(""),gbc);
        this.refreshUserList();
    }

    public void init(String username){
        //TODO there can be a value to control the online status, like online,busy,etc.
        this.user.setText(username);
        this.user.setMargin(new Insets(0,0,0,0));
    }

    public void registerObserver(ClientController controller){
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == this.sendBtn && !this.chatInput.getText().equals("")){
            String target = this.getCurrentChatting();
            String content = this.chatInput.getText();
            this.chatInput.setText("");
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

}
