package assignment_1.Model;

import java.io.*;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

public class Client {
    public static final String DOMAIN = "LOCALHOST";
    public static final int PORT = 9090;

    private String username;
    private InetAddress addr;
    private Socket socket;
    private LinkedList<String> clientList;
    private ObjectOutputStream objOut;
    private ObjectInputStream objIn;
    private Map<String,ArrayList<Message>> messageHistory;

    private int status;

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public InetAddress getAddr() {
        return addr;
    }

    public void setAddr(InetAddress addr) {
        this.addr = addr;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public LinkedList<String> getClientList(){
        return this.clientList;
    }

    public void setClientList(LinkedList<String> clientList) {
        this.clientList = clientList;
    }

    public ObjectOutputStream getObjOut() {
        return objOut;
    }

    public void setObjOut(ObjectOutputStream objOut) {
        this.objOut = objOut;
    }

    public ObjectInputStream getObjIn() {
        return objIn;
    }

    public void setObjIn(ObjectInputStream objIn) {
        this.objIn = objIn;
    }

    public Map<String, ArrayList<Message>> getMessageHistory() {
        return messageHistory;
    }

    public void setMessageHistory(Map<String, ArrayList<Message>> messageHistory) {
        this.messageHistory = messageHistory;
    }

    //0 offline
    //4 error
    //3 dup username
    //2 logging
    //1 online

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        Client client = new Client();
        System.out.println("enter your username:");
        client.login(in.nextLine());
        System.out.println("after login, choose a person to chat:");

        String testMan = in.nextLine();
        System.out.println("you choice: "+testMan);
        while (true){
//            client.sendChatMsg(testMan,in.nextLine());
            System.out.println("[CLI] sent");
        }
    }
    //
    //   methods controller should call
//    public ArrayList<Message> getMessage(String username){
//        if(this.messageHistory.containsKey(username)){
//            return this.messageHistory.get(username);
//        } else {
//            ArrayList<Message> newChat = new ArrayList<>();
//            this.messageHistory.put(username,newChat);
//            return newChat;
//        }
//    }



    //
    //GUI should call this constructor when btn 'login' is clicked
    public Client(){
        this.status = 0;
        this.clientList = new LinkedList<>();
    }

//    private void conn() throws IOException {
//        this.addr = InetAddress.getByName(DOMAIN);
//        this.socket = new Socket(this.addr,PORT);
//    }
//
//    private void initIOStream() throws IOException {
//        this.objIn = new ObjectInputStream(socket.getInputStream());
//        this.objOut = new ObjectOutputStream(socket.getOutputStream());
//    }

    private void initMessageHandlerThread(){
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
        this.setStatus(0);
    }

    public int getStatus(){
        return this.status;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public void sendMsg(Message msg) throws IOException {
        objOut.writeObject(msg);
    }

    public void setClientList(String clientList){
        LinkedList<String> clients = new LinkedList<>(Arrays.asList(clientList.split("&")));
        for(String username : this.messageHistory.keySet()){
            if(clients.contains(username) && !this.messageHistory.get(username).isEmpty()){
                clients.remove(username);
                clients.addFirst(username);
            }
        }
        this.clientList = clients;
    }
    public void refreshMessageHistory(){
        for(String username : this.messageHistory.keySet()){
            if(this.clientList.contains(username) && !this.messageHistory.get(username).isEmpty()){
                ;
            } else {
                this.messageHistory.remove(username);
            }
        }
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
                    Client.this.setStatus(4);
                    Client.this.logout();
                    this.stopctl = true;
                    //TODO LOGOUT maybe
                } else if(this.type.equals("KICKED")) {
                    Client.this.logout();
                    this.stopctl = true;
                } else if(this.type.equals("DUPLICATED")) {
                    Client.this.setStatus(3);
                } else{
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
