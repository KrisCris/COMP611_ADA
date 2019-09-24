package assignment_1.Controller;

import assignment_1.Model.Client;
import assignment_1.Model.Message;
import assignment_1.Model.MessageListener;
import assignment_1.View.ChatRoom;
import assignment_1.View.GBC;
import assignment_1.View.Login;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;


public class ChatRoomController {
    //model
    private Client client;
    private MessageListener messageListener;

    //view
    private ChatRoom chatRoom;
    private Login login;




    private Map<GBC, String> messageList;

    public ChatRoomController(){
        this.client = new Client();
        this.messageList = new LinkedHashMap<>();

        this.login.setBtnListener(e -> {
            long startTime =  System.currentTimeMillis();
            String username = this.login.getUsername();
            if(username.length()==0){
                this.login.setLabel("USERNAME INVALID");
            }else{
                try {
                    this.client.login(username);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            int status = this.client.getStatus();
            while(status!=1 && (System.currentTimeMillis()-startTime)/1000<60){
                status = this.client.getStatus();
                if(status==4){
                    this.login.setLabel("ERROR TRY AGAIN");
                } else if(status==3){
                    this.login.setLabel("DUPLICATED USERNAME");
                } else if(status==2){
                    this.login.setLabel("LOGGING IN");
                }
            }
            if(status == 1){
                //TODO blablabla
            }
        });
    }

    //control views
    public void showLogin(){
        this.chatRoom.setVisible(false);
        this.login.setVisible(true);
    }

    public void initLogin(){
        EventQueue.invokeLater(()->{
            this.login = new Login();

            this.login.setVisible(true);
        });
    }

    public Map<GBC,String> processMessage(String target){
        ArrayList<Message> rawMsg = client.getMessage(target);
        String username = this.client.getUsername();
        for(Message m: rawMsg) {
            int index = rawMsg.indexOf(m);
            int side = 0;
            if (m.getSender().equals(username)) {
                side = 1;
            }
            String content = m.getContent();
            StringBuilder reBuildStr = new StringBuilder();
            reBuildStr.append("<html>");
            for (String str : content.split("\n")) {
                while (str.length() > 20) {
                    reBuildStr.append(str.substring(0, 20));
                    reBuildStr.append("<br>");
                    str = str.substring(20);
                }
                reBuildStr.append(str);
                reBuildStr.append("<br>");
            }
            reBuildStr.append("</html>");
            content = reBuildStr.toString();
            GBC gbc = new GBC(side, index, 1, 1).setWeight(100, 0).setFill(GBC.HORIZONTAL);
            this.messageList.put(gbc,content);
        }
        return this.messageList;
    }

    public void updateChatMessage(){
        this.chatRoom.clearChatContents();
        for(Map.Entry<GBC,String> each : this.messageList.entrySet()){
            this.chatRoom.addChatContent(each.getValue(),each.getKey());
        }
        this.chatRoom.refreshChatContents();
    }

    public void updateClientList(){
        LinkedList<String> clientList= this.client.getClientList();
        int index = 0;
        for(String username : clientList){
            index = clientList.indexOf(username);
            GBC gbc = new  GBC(0, index, 1, 1).setWeight(100, 0).setFill(GBC.HORIZONTAL).setIpad(0, 25);
            this.chatRoom.addUser(username,gbc);
        }
        GBC dummy = new GBC(0, index+1, 1, 1).setWeight(0, 1);
        this.chatRoom.userListFiller(dummy);
    }

    //control models
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
        MessageListener messageHandler = new MessageListener(this.client.getSocket());
        Thread thread = new Thread(messageHandler);
        thread.start();
    }

    





}