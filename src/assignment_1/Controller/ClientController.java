package assignment_1.Controller;

import assignment_1.Model.Client;
import assignment_1.Model.Message;
import assignment_1.View.ChatRoom;
import assignment_1.View.CommonFunc;
import assignment_1.View.GBC;
import assignment_1.View.Login;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientController {
    //model
    private Client client;
    //view
    private ChatRoom chatRoom;
    private Login login;
    //
    private MessageHandler msgHandler;

    public ClientController(Client client,Login login,ChatRoom chatroom){
        this.client = client;
        this.login = login;
        this.chatRoom = chatroom;

        this.login.setVisible(true);
    }

    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
    //Event listeners
    public void processWindowCloseEvent() throws IOException {
        this.switchState("Offline");
    }

    public void processLoginEvent() throws IOException {
        JTextField nameField = this.login.getNameInput();
        String username = nameField.getText();
        if(nameField.getForeground()==Color.RED || nameField.getForeground()==Color.gray){
            nameField.setForeground(Color.RED);
        } else if(username.contains(" ")) {
            nameField.setText("Blank is invalid!");
            nameField.setForeground(Color.red);
        } else {
            this.login.setTitle("Logging, wait patiently");
            this.login(username);
        }
    }

    public void processSwitchChatTargetEvent(String target) {
        this.chatRoom.setCurrentChatting(target);
        this.updateChatMessage(target);
        this.chatRoom.enableChat();
    }

    public void processSendChatMsgEvent(String target,String content) throws IOException {
        this.sendChatMsg(target,content);
        this.updateChatMessage(target);
    }
    
    public void processSwitchStatusEvent(Object[] selection) throws IOException {
        String status;
        switch (this.client.getStatus()){
            case 0: status = "Offline";break;
            case 1: status = "Online";break;
            case 2: status = "Stealth";break;
            default:
                status = "error";
        }
        String tips = "Change your status to: ";
        String title = "Current status: "+status;
        String op = (String)JOptionPane.showInputDialog(null,tips,title,JOptionPane.PLAIN_MESSAGE,null,selection,selection[2]);
        System.out.println("selection:  "+op);
        if (op != null){
            this.switchState(op);
        }

    }

//    //control views
    public void showError(CommonFunc view,String alertMsg){
        view.alert(alertMsg);
    }

    public void initChatRoom(){
        String username = this.client.getUsername();
        this.chatRoom.init(username);
        this.chatRoom.setVisible(true);
    }

    public void updateChatMessage(String target){
        this.chatRoom.clearChatContents();
        Map<GBC,String> messageList = this.processMessage(this.getMessage(target));
        int index = 0;
        for(Map.Entry<GBC,String> each : messageList.entrySet()){
            this.chatRoom.addChatContent(each.getValue(),each.getKey());
            index = each.getKey().gridy;
        }
        index++;

        this.chatRoom.refreshChatContents(index);
    }

    public void alertNewMsg(Message msg){
        if(this.chatRoom.getCurrentChatting().equals(msg.getSender())){
            this.updateChatMessage(msg.getSender());
        } else {
            //TODO Do some alert
        }
    }

    public void updateViewClientList(){
        LinkedList<String> clientList= this.client.getClientList();
        int index = 0;
        this.chatRoom.clearUserList();
        this.chatRoom.setOnlineNumber(clientList.size()+1);
        for(String username : clientList){
            index = clientList.indexOf(username)+1;
            GBC gbc = new  GBC(0, index, 1, 1).setWeight(100, 0).setFill(GBC.HORIZONTAL).setIpad(0, 25);
            this.chatRoom.addUser(username,gbc);
        }
        GBC dummy = new GBC(0, index+1, 1, 1).setWeight(0, 1);
        this.chatRoom.rebuildUserList(dummy);
    }

    public void alertTargetOffline(){
        if((!this.client.getClientList().contains(this.chatRoom.getCurrentChatting()))
                && this.chatRoom.getChatState()){
            this.chatRoom.alertOffline();
        }
    }

    //
    public ArrayList<Message> getMessage(String username){
        Map<String,ArrayList<Message>> messageHistory = this.client.getMessageHistory();
        if(messageHistory.containsKey(username)){
            return messageHistory.get(username);
        } else {
            ArrayList<Message> newChat = new ArrayList<>();
            messageHistory.put(username,newChat);
            return newChat;
        }
    }

    //TODO some modifications are required to fix bugs with html
    public Map<GBC,String> processMessage(ArrayList<Message> rawMsgList){
        Map<GBC,String> messageList = new LinkedHashMap<>();
        String username = this.client.getUsername();
        for(Message m: rawMsgList) {
            int index = rawMsgList.indexOf(m);
            int side = 0;
            if (m.getSender().equals(username)) {
                side = 1;
            }
            String content = m.getContent();
            StringBuilder reBuildStr = new StringBuilder();
            reBuildStr.append("<html>");
            int len;
            if(this.isContainChinese(content)){
                len = 20;
            } else {
                len = 47;
            }
            for (String str : content.split("\n")) {
                int _len = len;
                while (str.length() > len) {
                    _len = len;
                    if(len==47){
                        while(str.charAt(_len)!=' '){
                            if(_len<1){
                                _len = len;
                                break;
                            }
                            _len--;
                        }
                    }
                    reBuildStr.append(str.substring(0, _len));
                    reBuildStr.append("<br>");
                    str = str.substring(_len);
                }
                reBuildStr.append(str);
                reBuildStr.append("<br>");
            }
            reBuildStr.append("</html>");
            content = reBuildStr.toString();
            GBC gbc = new GBC(side, index, 1, 1).setWeight(100, 0).setFill(GBC.HORIZONTAL) .setInsets(3);
            messageList.put(gbc,content);
        }
        return messageList;
    }
    //control models
    public void login(String username) throws IOException {
        this.client.setUsername(username);
        this.conn();
        this.initIOStream();
        this.initMessageHandlerThread();
        Message initMsg = new Message("LOGIN",username,"","",this.client.getStealthCode());
        this.sendMsg(initMsg);
    }

    private void conn() throws IOException {
        this.client.setAddr(InetAddress.getByName(Client.DOMAIN));
        this.client.setSocket(new Socket(this.client.getAddr(),Client.PORT));
    }

    private void initIOStream() throws IOException {
        Socket socket = this.client.getSocket();
        this.client.setObjIn(new ObjectInputStream(socket.getInputStream()));
        this.client.setObjOut(new ObjectOutputStream(socket.getOutputStream()));
    }

    private void initMessageHandlerThread(){
        this.msgHandler = new MessageHandler(this.client.getSocket());
        Thread thread = new Thread(msgHandler);
        thread.start();
    }

    public void setClientList(String clientList){
        Map<String,ArrayList<Message>> messageHistory = this.client.getMessageHistory();
        LinkedList<String> clients = new LinkedList<>(Arrays.asList(clientList.split("&")));
        clients.remove(this.client.getUsername());
        for(String username : messageHistory.keySet()){
            if(clients.contains(username) && !messageHistory.get(username).isEmpty()){
                clients.remove(username);
                clients.addFirst(username);
            }
        }
        this.client.setClientList(clients);
    }

    public void refreshMessageHistory(){
        Map<String,ArrayList<Message>> messageHistory = this.client.getMessageHistory();
        for(String username : messageHistory.keySet()){
            if(this.client.getClientList().contains(username) && !messageHistory.get(username).isEmpty()){
                ;
            } else {
                messageHistory.remove(username);
            }
        }
    }

    private void addToMsgHistory(Message msg){
        String sender = msg.getSender();
        Map<String,ArrayList<Message>> messageHistory = this.client.getMessageHistory();
        ArrayList<Message> msgList;
        //to decide whether it is a message to be sent or received
        if(sender.equals(this.client.getUsername())){
            //sender is of the perspective of the one you are chatting with
            sender = msg.getReceiver();
        }
        //get or create chat history of a specific user
        if(messageHistory.containsKey(sender)){
            msgList = messageHistory.get(sender);
        } else {
            msgList = new ArrayList<>();
            this.client.getMessageHistory().put(sender,msgList);
        }
        //add new message to it
        msgList.add(msg);
    }

    private void sendMsg(Message msg) throws IOException{
        this.client.getObjOut().writeObject(msg);
    }

    private void sendChatMsg(String username,String content) throws IOException {
        Message chat = new Message("CHAT",this.client.getUsername(),username,content,this.client.getStealthCode());
        this.addToMsgHistory(chat);
        this.sendMsg(chat);
    }

    private void sendRequestMsg(String type,String content) throws IOException {
        Message request = new Message(type,this.client.getUsername(),"",content,this.client.getStealthCode());
        this.sendMsg(request);
    }

    public void switchState(String type) throws IOException {
        if(type.equals("Offline")){
            this.sendRequestMsg("OFFLINE","");
        } else if (type.equals("Stealth")){
            //TODO switch to stealth mode, may change the ui.
            String tips = "People sharing the same code can see each other";
            String title = "Enter your stealth code";
            if(this.client.getStatus()==2){
                tips = "Your current code is "+this.client.getStealthCode();
                title = "Enter a new code to switch.";
            }
            String code = (String)JOptionPane.showInputDialog(null,tips,title,JOptionPane.PLAIN_MESSAGE,null,null,"");
            if(code != null){
                if(code.equals("") || code.contains(" ")){
                    JOptionPane.showMessageDialog(null,"Code shouldn't contain blanks or be empty.","Invalid stealth code!",JOptionPane.ERROR_MESSAGE);
                } else if(code.equals(this.client.getStealthCode()) ) {
                    JOptionPane.showMessageDialog(null,"You are already using this code.","Invalid stealth code!",JOptionPane.ERROR_MESSAGE);
                } else {
                    this.sendRequestMsg("STEALTH",code);
                }

            }
            System.out.println("code: "+code);
        } else if (type.equals("Online")) {
            if(this.client.getStatus()!=1){
                this.sendRequestMsg("ONLINE","");
            }
        } else {
            System.out.println("[ switchState() ] : " + type);
        }
    }

    public void reset(){
        this.client = new Client();
        this.chatRoom.setVisible(false);
        this.chatRoom = new ChatRoom();
        this.chatRoom.registerObserver(this);
        this.login.reset();
        Runtime.getRuntime().gc();
        this.login.setVisible(true);
    }
    public JDialog alert(String msg){
        JOptionPane pane = new JOptionPane(msg,JOptionPane.WARNING_MESSAGE);
        JDialog dialog = pane.createDialog(null,"ALERT");
        dialog.setModal(false);
        dialog.setVisible(true);
        return dialog;
    }

    public void stop() throws IOException {
        this.msgHandler.stop();
        this.client.getObjIn().close();
        this.client.getObjOut().close();
        this.client.getSocket().close();
    }
    private class MessageHandler implements Runnable {
        private Socket socket;
        private boolean stopCtl;
        private Message msg;
        private String type;
        private String sender;
        private String receiver;
        private String content;

        public MessageHandler(Socket socket) {
            this.socket = socket;
            this.stopCtl = false;
        }

        public void stop(){
            this.stopCtl = true;
        }

        private void msgReactor() throws IOException, ClassNotFoundException {
            do {
                this.readMessage();
                System.out.println(msg.toString());
                ;
                if (this.type.equals("INIT")) {
                    ClientController.this.client.setStatus(1);
                    ClientController.this.login.setVisible(false);
                    ClientController.this.initChatRoom();
                } else if (this.type.equals("CHAT")) {
                    ClientController.this.addToMsgHistory(msg);
                    ClientController.this.alertNewMsg(msg);
                    //debug
                    System.out.println(this.sender + " : " + this.content);

                } else if (this.type.equals("UPDATE")) {
                    ClientController.this.setClientList(this.content);
                    ClientController.this.refreshMessageHistory();
                    ClientController.this.updateViewClientList();
                    ClientController.this.alertTargetOffline();
                    //debug
                    System.out.print("[ALERT]online list: ");
                    for (String client : ClientController.this.client.getClientList()) {
                        System.out.println(client + ", ");
                    }

                } else if (this.type.equals("STOP")) {
                    JDialog dialog = ClientController.this.alert("Server will shutdown. Please logout after saving your data.");
                    new Timer(Integer.parseInt(content), new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            dialog.setVisible(false);
                        }
                    }).start();
                } else if(this.type.equals("FORCE_STOP")){
                    ClientController.this.stop();
                    ClientController.this.reset();
                    ClientController.this.alert(this.content);
                } else if(this.type.equals("ALERT")){
                    JOptionPane.showMessageDialog(null, this.content, "Alert",JOptionPane.WARNING_MESSAGE);

                } else if(this.type.equals("OFFLINE")) {
                    ClientController.this.stop();
                    ClientController.this.reset();
                    this.stopCtl = true;
                } else if(this.type.equals("DUPLICATED")) {
                    ClientController.this.showError(ClientController.this.login,"Username existed");
                    ClientController.this.login.setTitle("Try again");
                } else if(this.type.equals("STEALTH")){
                    ClientController.this.client.setStealthCode(this.content);
                    ClientController.this.client.setStatus(2);
                    ClientController.this.chatRoom.disableChat();
                    ClientController.this.chatRoom.clearChatContents();
                    ClientController.this.chatRoom.setCurrentChatting("Click names to start Chats");
                } else if(this.type.equals("ONLINE")){
                    ClientController.this.client.setStealthCode(null);
                    ClientController.this.client.setStatus(1);
                    ClientController.this.chatRoom.disableChat();
                    ClientController.this.chatRoom.clearChatContents();
                    ClientController.this.chatRoom.setCurrentChatting("Click names to start Chats");
                } else {
                    System.out.println("[MessageHandler] "+"[Message] "+msg.toString());
                }
            } while(!this.stopCtl);
        }

        private void readMessage() throws IOException, ClassNotFoundException {
            this.msg = (Message) ClientController.this.client.getObjIn().readObject();
            this.type = this.msg.getType();
            this.sender = msg.getSender();
            this.receiver = msg.getReceiver();
            this.content = msg.getContent();
        }

        public void run() {
            try {
                this.msgReactor();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
}