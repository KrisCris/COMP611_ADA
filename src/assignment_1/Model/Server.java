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
 * The MVC Pattern haven't been fully implemented, due to the limitation of time.
 * And because of that, most methods are still in Class Sever than its controller.
 * However, the Client side is based on MVC.
 *
 *
 */
public class Server {
    private static final int PORT = 2333;
    private static final int BACKLOG = 100;

    private ServerSocket serverSocket;
    private Map<String,ChatHandler> clientTable;
    private Map<String,Map<String,ChatHandler>> stealthList;

    public static void main(String[] args) throws IOException {
        Server chatServer = new Server();
        chatServer.init();
        chatServer.startSocketListener();
    }

    public Server(){
        this.clientTable = new HashMap<>();
        this.stealthList = new HashMap<>();
    }

    public void init() throws IOException {
        init(PORT,BACKLOG);
    }

    public void init(int port, int backlog) throws IOException {
        this.serverSocket = new ServerSocket(port,backlog);
        System.out.println("Server started at " + InetAddress.getLocalHost() + " on port " + port);
    }

    public void startSocketListener() throws IOException {
        while(true) {
            Socket socket = this.serverSocket.accept();
            System.out.println("Connection made with " + socket.getInetAddress());
            ChatHandler chatHandler = new ChatHandler(socket);
            Thread thread = new Thread(chatHandler);
            thread.start();
        }
    }

    public ChatHandler getChatHandler(String username){
        ChatHandler chatHandler = clientTable.get(username);
        return chatHandler;
    }

    public ChatHandler getChatHandler(String code,String username){
        ChatHandler chatHandler = this.stealthList.get(code).get(username);
        return chatHandler;
    }

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

    public void stopServer(int time) throws InterruptedException, IOException {
        Timer timer = new Timer();
        timer.wait(time);
        //TODO times
        this.sendAll("STOP","Server is down");
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

    public void stealth(String code, String username, ChatHandler chatHandler) throws IOException {
        if(!this.stealthList.containsKey(code)){
            this.stealthList.put(code,new HashMap<String,ChatHandler>());
        }
        this.stealthList.get(code).put(username,chatHandler);
        this.sendStealthList(code);
    }

    public void sendStealthList(String code) throws IOException {
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

//    private class stopListener implements Runnable{
//
//        @Override
//        public void run() {
//            Scanner scanner = new Scanner(System.in);
//            String ctrl = "";
//            System.out.println("enter shutdown + Minute to stop server");
//            do{
//                ctrl = scanner.nextLine();
//                String[] ctrls = ctrl.split(" ");
//                if(ctrls.length == 2 && (ctrls[0].equals("shutdown") && isPositiveDigital(ctrls[1]))){
//                    break;
//                } else {
//                    System.out.println("[Warn] Wrong format!");
//                    System.out.println("[Info] Please follow the format:");
//                    System.out.println("[Info] \"shutdown\" + MINUTES");
//                    System.out.println("[Info] e.g. shutdown 10");
//                }
//            } while (true);
//            //TODO SHUTDOWN SERVER
//        }
//    }

    public static boolean isPositiveDigital(String str) {
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

        private void login() throws IOException {
            System.out.println("[NOTIFICATION] "+this.sender+"/"+this.socket.getInetAddress()+" tried to login");
            if(Server.this.ifUserDuplicated(this.sender)){
                Message err_dup = new Message("DUPLICATED","username already existed");
                this.sendMsg(err_dup);
                this.stopctl = true;
                System.out.println("[ERROR] "+this.sender+"/"+this.socket.getInetAddress()+" username conflicted");
            } else {
                Server.this.clientTable.put(sender,this);
                Message passed = new Message("INIT",sender+"successfully logged in");
                this.sendMsg(passed);
                System.out.println("[SUCCEED] "+sender+" has login!");
            }
        }

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

        private void msgReactor() throws IOException, ClassNotFoundException {
            this.initIOStream();
            System.out.println("---Waiting for client request---");
            do{
                this.readMessage();
                System.out.println(this.msg.toString());

                if (this.type.equals("LOGIN")){
                    /*
                     * Implemented for only the login request.
                     * User will be logged into Online mode by default.
                     *
                     * Directly log into other modes may be possible in the future, but not for now,
                     * since it is such a time wasting work.
                     */
                    this.login();
                    Server.this.sendOnlineList();
                    System.out.println(this.sender+" logged in");
                } else if (this.type.equals("CHAT")){
                    /*
                     * Forwarding chat message to the target user.
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
                     *
                     * A online user list updating notification is integrated into the logout method.
                     */
                    if(this.stealthCode==null){
                        this.logout();
                    } else {
                        this.logout(this.stealthCode);
                    }
                    System.out.println(this.sender+" has logged out");
                    this.stopctl = true;
                } else if (this.type.equals("STEALTH")){
                    /*
                     * In stealth msg, stealthCode is the stealth portion current stays,
                     * and content contains the new stealthCode.
                     *
                     * By adding handler to a new stealth room before removing it from previous room,
                     * we are confident that no one would login with the same name between the two steps.
                     */
                    Server.this.stealth(content,this.sender,this);
                    if(this.stealthCode == null){
                        Server.this.removeClient(this.sender);
                    } else {
                        System.out.println("current stealth code = "+ this.stealthCode);
                        Server.this.removeClient(this.stealthCode,this.sender);
                    }
                    Message stealthMsg = new Message("STEALTH",this.content);
                    this.sendMsg(stealthMsg);
                    System.out.println(this.sender+" switched to stealth room: "+this.content+ " from "+this.stealthCode);

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

    }

}
