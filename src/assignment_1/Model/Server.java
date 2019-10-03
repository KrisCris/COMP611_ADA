package assignment_1.Model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/*
 * The chat room Server.
 *
 * The MVC Pattern haven't been implemented to server, due to the limitation of time,
 * so that the server controller and view in their folder is still in progress.
 * But at least the Server.class with ServerDemo.class are fully functional.
 *
 * While I did had some idea about the "MVC" of server:
 * 1. Server controller observing both server (Model) and a CLI (View).
 * 2. Model only owns a bunch of map/list which contains sockets of every online client and some getter and setters.
 * 3. Controller:
 * 3.1. Controller has a thread for serverSocket listening for requests from new client.
 * 3.1. It also has a inner class implementing runnable for each socket listening for its client requests.
 * 3.2. Besides, there are a list of methods to deal with messages received and model/view control in the Controller.
 * 4. View
 * 4.1. The View is for inputting parameters that control the way server works including to stop the server.
 * 4.2. It also could have some abilities to output some logs to the screen or disk for debugging or something.
 * 4.3. All these functionality are observed and called by its Controller.
 * Above may be possible to be implemented in the future.
 *
 * BTW, the Client side is based on MVC.
 *
 */
public class Server {
    private static final int PORT = 2333;
    private static final int BACKLOG = 100;

    private ServerSocket serverSocket;
    private ChatHandler watch;
    private Map<String,ChatHandler> clientTable;
    private Map<String,Map<String,ChatHandler>> stealthList;
    private boolean serverStopCtrl;

    public Server(){
        this.clientTable = new HashMap<>();
        this.stealthList = new HashMap<>();
        this.serverStopCtrl = false;
    }

    public void init() throws IOException {
        init(PORT,BACKLOG);
    }

    public void init(int port, int backlog) throws IOException {
        this.serverSocket = new ServerSocket(port,backlog);
        System.out.println("Server started at " + InetAddress.getLocalHost() + " on port " + port);
    }

    /*
     * To create a thread for serverSocket listening for new connections.
     * The variable serverStopCtrl is used to stop this thread.
     */
    public void startSocketListener(){
        Thread serverSocketListener = new Thread(() -> {
            while(!Server.this.serverStopCtrl) {
                Socket socket = null;
                try {
                    socket = Server.this.serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Connection made with " + socket.getInetAddress());
                this.watch = new ChatHandler(socket);
                Thread thread = new Thread(this.watch);
                thread.start();
                if(Server.this.serverStopCtrl){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        serverSocketListener.start();
    }

    /*
     * To get the Runnable chatHandler of target client, which provides controller methods to communication.
     */
    public ChatHandler getChatHandler(String username){
        ChatHandler chatHandler = clientTable.get(username);
        return chatHandler;
    }
    public ChatHandler getChatHandler(String code,String username){
        ChatHandler chatHandler = this.stealthList.get(code).get(username);
        return chatHandler;
    }

    /*
     * Ensuring no user share the same name.
     */
    public boolean ifUserDuplicated(String username){
        Boolean online = this.clientTable.containsKey(username);
        Boolean stealth = false;
        for(Map<String,ChatHandler> map : this.stealthList.values()){
            if(map.containsKey(username)){
                stealth = true;
                break;
            }
        }
        return online||stealth;
    }

    /*
     * A synchronized method to remove any client from clientList.
     */
    public synchronized ChatHandler removeClient(String username) throws IOException {
        ChatHandler removed = null;
        if(this.clientTable.containsKey(username)){
            removed =  this.clientTable.remove(username);
        }
        this.sendOnlineList();
        return removed;
    }
    public synchronized ChatHandler removeClient(String code, String username) throws IOException {
        ChatHandler removed = null;
        if (this.stealthList.containsKey(code)){
            if(this.stealthList.get(code).containsKey(username)){
                removed = this.stealthList.get(code).remove(username);
            }
            this.sendStealthList(code);
        }
        System.out.println("[" + username + "] had been removed from stealth room ["+code+"]");
        return removed;
    }

    /*
     * A synchronized method to provide a client online list for clients.
     */
    public synchronized void sendOnlineList() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for(String client : this.clientTable.keySet()){
            stringBuilder.append(client+"&");
        }
        String clientList = stringBuilder.toString();
        if (clientList.length() > 0) {
            clientList = clientList.substring(0,clientList.length()-1);
        }
        for(ChatHandler each : this.clientTable.values()){
            each.sendMsg(new Message("UPDATE",clientList));
        }
    }
    public void sendAll(String type, String content) throws IOException {
        Message msg = new Message(type,content);
        for(ChatHandler each : this.clientTable.values()){
            each.sendMsg(msg);
        }
        for (Map<String,ChatHandler>map:this.stealthList.values()){
            for(ChatHandler each:map.values()){
                each.sendMsg(msg);
            }
        }
    }

    /*
     * To switch client to stealth mode.
     */
    public void stealth(String code, String username, ChatHandler chatHandler) throws IOException {
        if(!this.stealthList.containsKey(code)){
            this.stealthList.put(code,new HashMap<String,ChatHandler>());
        }
        this.stealthList.get(code).put(username,chatHandler);
        this.sendStealthList(code);
    }

    /*
     * A synchronized method to provide a client stealth list for stealth clients.
     */
    public synchronized void sendStealthList(String code) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for(String client : this.stealthList.get(code).keySet()){
            stringBuilder.append(client+"&");
        }
        String clientList = stringBuilder.toString();
        if (clientList.length() > 0) {
            clientList = clientList.substring(0,clientList.length()-1);
        }
        for(ChatHandler each : this.stealthList.get(code).values()){
            each.sendMsg(new Message("UPDATE",clientList));
        }
    }

    /*
     * To send a warning before stopping the server.
     */
    public void stopWarn(int timer) throws InterruptedException, IOException {
        this.sendAll("STOP",timer+"");
    }

    /*
     * To check the number of clients connected to the server.
     */
    public int countAll(){
        int online = this.clientTable.size();
        int stealth = 0;
        for(Map<String,ChatHandler> map: this.stealthList.values()){
            stealth += map.size();
        }
        return online+stealth;
    }

    /*
     * To fully shutdown the server application;
     */
    public void stopServer() throws IOException, InterruptedException, ClassNotFoundException {
        this.serverStopCtrl = true;
        Socket poison = new Socket("127.0.0.1",PORT);
        ObjectInputStream objectInputStream = new ObjectInputStream(poison.getInputStream());
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(poison.getOutputStream());
        for(ChatHandler ch: clientTable.values()){
            ch.stopChatHandler();
        }
        for(Map<String,ChatHandler> map:this.stealthList.values()){
            for(ChatHandler ch:map.values()){
                ch.stopChatHandler();
            }
        }
        System.out.println("All AFK client sockets stopped.");
        objectOutputStream.writeObject(new Message("OFFLINE",""));
        poison.shutdownInput();
        poison.shutdownOutput();
        poison.close();
        System.out.println("Poison socket stopped.");
        this.serverSocket.close();
        System.out.println("ServerSocket closed.");
        System.exit(0);
    }

    /*
     * Used to ensure a input string is positive digit.
     */
    public static boolean isPositiveDigit(String str) {
        int value = 0;
        try {
            value = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        if(value>0){
            return true;
        } else {
            return false;
        }
    }

    /*
     * The actual class used for listening to clients' requests.
     * Each client will own a single thread of it.
     * And the instances of this thread will be stored into clientList/stealthList
     */
    private class ChatHandler implements Runnable{
        private Socket socket;
        private ObjectInputStream objIn;
        private ObjectOutputStream objOut;

        private Message msg;
        private String type;
        private String sender;
        private String receiver;
        private String content;
        private String stealthCode;

        private boolean stopctl;

        public ChatHandler(Socket socket){
            this.socket = socket;
            this.stopctl = false;
        }

        private void initIOStream() throws IOException {
            this.objOut = new ObjectOutputStream(socket.getOutputStream());
            this.objIn = new ObjectInputStream(socket.getInputStream());
        }

        private void readMessage() throws IOException, ClassNotFoundException {
            this.msg = (Message) (this.objIn.readObject());
            this.type = msg.getType();
            this.sender = msg.getSender();
            this.receiver = msg.getReceiver();
            this.content = msg.getContent();
            this.stealthCode = msg.getStealthCode();
        }

        //enabling other clients sending message to the client by calling this method
        public synchronized void sendMsg(Message msg) throws IOException {
            this.objOut.writeObject(msg);
        }

        /*
         * To deal with this the login stuff of this client.
         * This method does something like ensuring the username is not duplicated,
         * adding this client to online list and notifying other clients.
         * Then it will send a message that tells the client whether or not it has successfully logged in.
         */
        private void login() throws IOException {
            if(Server.this.ifUserDuplicated(this.sender)){
                Message err_dup = new Message("DUPLICATED","username already existed");
                this.sendMsg(err_dup);
                this.stopctl = true;
            } else {
                Server.this.clientTable.put(sender,this);
                Message passed = new Message("INIT",sender+"successfully logged in");
                this.sendMsg(passed);
            }
        }

        /*
         * To do the logout stuff.
         * The message sent is to notify this client that it has successfully logged out.
         * The method notifying other clients is written in Server.this.removeClient().
         */
        private void logout() throws IOException {
            Server.this.removeClient(this.sender);
            Message offlineMsg = new Message("OFFLINE",this.sender+" are detached from server");
            this.sendMsg(offlineMsg);
        }
        private void logout(String stealthCode) throws IOException {
            Server.this.removeClient(stealthCode,this.sender);
            Message offlineMsg = new Message("OFFLINE",this.sender+" are detached from server");
            this.sendMsg(offlineMsg);
        }

        /*
         * Different reactions to different types of messages.
         */
        private void msgReactor() throws IOException, ClassNotFoundException {
            this.initIOStream();
            System.out.println("Waiting for client requests");
            do{
                /*
                 * Receiving and "decoding" the message.
                 */
                this.readMessage();
                if (this.type.equals("LOGIN")){
                    /*
                     * This is only for login request.
                     * User will be logged into Online mode by default.
                     * Directly log into other modes may be possible in the future, but not for now,
                     * since it is such a time wasting work.
                     */
                    this.login();
                    Server.this.sendOnlineList();
                    System.out.println(this.sender+" logged in");
                } else if (this.type.equals("CHAT")){
                    /*
                     * Forwarding chat message to the target user.
                     * If stealthCode is not null, it means this user is in stealth mode.
                     */
                    if(this.stealthCode==null){
                        System.out.println(this.sender+" sent msg to "+this.receiver+" : "+this.content);
                        ChatHandler target = Server.this.getChatHandler(receiver);
                        target.sendMsg(msg);
                    } else {
                        ChatHandler target = Server.this.getChatHandler(stealthCode,receiver);
                        target.sendMsg(msg);
                    }
                } else if (this.type.equals("OFFLINE")){
                    /*
                     * To deal with logout request.
                     */
                    if(this.sender.equals("")&&this.receiver.equals("")){
                        System.out.println("one is forced to stop");
                    }else {
                        if(this.stealthCode==null){
                            this.logout();
                        } else {
                            this.logout(this.stealthCode);
                        }
                        System.out.println(this.sender+" has logged out");
                    }
                    this.stopChatHandler();
                } else if (this.type.equals("STEALTH")){
                    /*
                     * In stealth messages, the stealthCode is the stealth portion client current stays,
                     * and the content contains the new stealthCode.
                     *
                     * By adding handler to a new stealth room before removing it from previous room,
                     * we are confident that no one would login with the same name between the two steps.
                     */
                    Server.this.stealth(content,this.sender,this);
                    if(this.stealthCode == null){
                        Server.this.removeClient(this.sender);
                    } else {
                        Server.this.removeClient(this.stealthCode,this.sender);
                    }
                    Message stealthMsg = new Message("STEALTH",this.content);
                    this.sendMsg(stealthMsg);
                } else if(this.type.equals("ONLINE")){
                    /*
                     * Only for those who want to switch back to online mode from stealth
                     * No one can actually send this request with online mode.
                     *
                     * This can be extended to suit any other mode, but currently only stealth mode
                     * had been implemented.
                     */
                    Server.this.clientTable.put(sender,this);
                    Server.this.removeClient(this.stealthCode,this.sender);
                    Message onlineMsg = new Message("ONLINE","");
                    this.sendMsg(onlineMsg);
                    Server.this.sendOnlineList();
                } else {
                    System.out.println(this.msg.toString());
                }
            } while (!this.stopctl);
        }

        public void run() {
            try {
                msgReactor();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        public void stopChatHandler() throws IOException {
            this.objIn.close();
            this.objOut.close();
            this.socket.close();
            this.stopctl = true;
        }

    }

}
