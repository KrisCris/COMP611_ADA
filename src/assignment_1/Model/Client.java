package assignment_1.Model;

import java.io.*;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

public class Client {
    public static final String DOMAIN = "LOCALHOST";
    public static final int PORT = 2333;

    private InetAddress addr;
    private Socket socket;
    private ObjectOutputStream objOut;
    private ObjectInputStream objIn;

    private int status;
    private String username;

    private LinkedList<String> clientList;
    private Map<String,ArrayList<Message>> messageHistory;


    public Client(){
        this.status = 0;
        this.clientList = new LinkedList<>();
        this.messageHistory = new LinkedHashMap<>();
    }

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

    public int getStatus(){
        return this.status;
    }

    public void setStatus(int status){
        this.status = status;
    }

    //0 offline
    //4 error
    //3 dup username
    //2 logging
    //1 online

//    public static void main(String[] args) throws IOException {
//        Scanner in = new Scanner(System.in);
//        Client client = new Client();
//        System.out.println("enter your username:");
//        client.login(in.nextLine());
//        System.out.println("after login, choose a person to chat:");
//
//        String testMan = in.nextLine();
//        System.out.println("you choice: "+testMan);
//        while (true){
////            client.sendChatMsg(testMan,in.nextLine());
//            System.out.println("[CLI] sent");
//        }
//    }


}
