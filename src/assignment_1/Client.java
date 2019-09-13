package assignment_1;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class Client {
    private static final String DOMAIN = "LOCALHOST";
    private static final int PORT = 9090;

    private String username;
    private InetAddress addr;
    private Socket socket;
    private String[] clientList;
    private ObjectOutputStream objOut;
    private ObjectInputStream objIn;

    //-1 offline
    //0 logging
    //1 online
    private int status;
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        Client client = new Client();
        System.out.println("enter your username:");
        client.login(in.nextLine());
        System.out.println("after login, choose a person to chat:");

        String testMan = in.nextLine();
        System.out.println("you choice: "+testMan);
        while (true){
            client.sendChatMsg(testMan,in.nextLine());
            System.out.println("[CLI] sent");
        }
    }
    //GUI should call this constructor when btn 'login' is clicked
    public Client(){
        this.status = -1;
        this.clientList = new String[1];
    }

    private void conn() throws IOException {
        this.addr = InetAddress.getByName(DOMAIN);
        this.socket = new Socket(this.addr,PORT);
    }

    private void initIOStream() throws IOException {
        this.objIn = new ObjectInputStream(socket.getInputStream());
        this.objOut = new ObjectOutputStream(socket.getOutputStream());
    }

    private void initMHThread(){
        MessageHandler messageHandler = new MessageHandler(this.socket);
        Thread thread = new Thread(messageHandler);
        thread.start();
    }

    public void login(String username) throws IOException {
        this.username = username;
        this.conn();
        this.initIOStream();
        this.initMHThread();
        this.sendMsg(new Message("LOGIN",this.username,"",""));
    }

    public void logout() throws IOException {
        Message msg_logout = new Message("LOGOUT",this.username,"","");
        this.sendMsg(msg_logout);
        this.setStatus(-1);
    }

    public int getStatus(){
        return this.status;
    }

    public void setStatus(int status){
        this.status = status;
    }
    public void sendChatMsg(String receiver,String content) throws IOException {
        Message chat = new Message("CHAT",this.username,receiver,content);
        this.sendMsg(chat);
    }
    public void sendMsg(Message msg) throws IOException {
        objOut.writeObject(msg);
    }

    public void setClientList(String clientList){
        this.clientList = clientList.split("&");
    }
    private class MessageHandler implements Runnable {
        private Socket socket;
        private boolean stopctl;
        private Message msg;
        private String type;
        private String sender;
        private String receiver;
        private String content;

        public MessageHandler(Socket socket) {
            this.socket = socket;
            this.stopctl = false;
        }

        private void msgReactor() throws IOException, ClassNotFoundException {
            do{
                this.readMessage();
                if(this.type.equals("INIT")){
                    Client.this.setStatus(1);
                    //test
                    System.out.println("login successfully");

                    System.out.println("now enter a name to start a chat");
                    //
                } else if(this.type.equals("CHAT")){
                    System.out.println(this.sender+" : "+this.content);
                } else if(this.type.equals("UPDATE")) {
                    Client.this.setClientList(this.content);
                    System.out.println("online list: ");
                    for(String client: Client.this.clientList){
                        System.out.println(client);
                    }
                    System.out.println("///////////");
                } else if(this.type.equals("ERROR")) {
                    Client.this.logout();
                    this.stopctl = true;
                    //TODO LOGOUT maybe
                } else if(this.type.equals("KICKED")) {
                    Client.this.logout();
                    this.stopctl = true;
                } else {
                    //TODO TBD
                    System.out.println("[MSG REACTOR][ELSE]:");
                    System.out.println("TYPE: "+this.type);
                    System.out.println("SENDER: "+this.sender);
                    System.out.println("RECEIVER: "+this.receiver);
                    System.out.println("CONTENT:" +this.content);
                    System.out.println("[MSG REACTOR END]");
                }
            } while(!this.stopctl);
        }

        private void readMessage() throws IOException, ClassNotFoundException {
            this.msg = (Message) Client.this.objIn.readObject();
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
