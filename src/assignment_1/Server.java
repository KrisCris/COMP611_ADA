package assignment_1;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Server {
    private static final int PORT = 9090;
    private static final int BACKLOG = 100;

    private ServerSocket serverSocket;
    private Map<String,ChatHandler> clientTable;

    public static void main(String[] args) throws IOException {
        Server chatServer = new Server();
        chatServer.init();
        chatServer.startSocketListener();
    }

    public Server(){
        this.clientTable = new HashMap<>();
    }

    public void init() throws IOException {
        this.serverSocket = new ServerSocket(PORT,BACKLOG);
        System.out.println("Server started at " + InetAddress.getLocalHost() + " on port " + PORT);
    }

    public void init(int port, int backlog) throws IOException {
        this.serverSocket = new ServerSocket(port,backlog);
        System.out.println("Server started at " + InetAddress.getLocalHost() + " on port " + PORT);
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

    public boolean ifUserDuplicated(String username){
        return this.clientTable.containsKey(username);
    }

    public synchronized ChatHandler removeClient(String username) throws IOException {
        ChatHandler removed =  this.clientTable.remove(username);
        this.sendOnlineList();
        return removed;
    }

    public synchronized void sendOnlineList() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for(String client : this.clientTable.keySet()){
            stringBuilder.append(client+"&");
        }
        String clientList = stringBuilder.toString();
        clientList = clientList.substring(0,clientList.length()-1);
        for(ChatHandler each : this.clientTable.values()){
            each.sendMsg(new Message("UPDATE",clientList));
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
        }

        //enabling other clients sending message to the client by calling this method
        public void sendMsg(Message msg) throws IOException {
            this.objOut.writeObject(msg);
        }

        private void login() throws IOException {
            System.out.println("[NOTIFICATION] "+this.sender+"/"+this.socket.getInetAddress()+" tried to login");
            if(Server.this.ifUserDuplicated(this.sender)){
                Message err_dup = new Message("ERROR","username already existed");
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
            this.stopctl = true;
        }

        private void msgReactor() throws IOException, ClassNotFoundException {
            this.initIOStream();
            System.out.println("---Waiting for client request---");
            do{
                //get message from IOStream
                this.readMessage();
                System.out.println(this.msg.toString());
                //deal with different types of MESSAGE
                if (this.type.equals("LOGIN")){
                    this.login();
                    Server.this.sendOnlineList();
                    System.out.println(this.sender+" logged in");
                } else if (this.type.equals("CHAT")){
                    //get target client's chatHandler by username
                    ChatHandler target = Server.this.getChatHandler(receiver);
                    target.sendMsg(msg);

                } else if (this.type.equals("LOGOUT")){
                    this.logout();

                } else {
                    //TODO TBD
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
