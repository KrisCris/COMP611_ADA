package assignment_1;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Scanner;

public class Client {
    private static final String DOMAIN = "LOCALHOST";
    private static final int PORT = 9090;

    private String username;
    private InetAddress addr;
    private Socket socket;
    private LinkedList<String> clientList;
    private ObjectOutputStream objOut;
    private ObjectInputStream objIn;

    //GUI should call this constructor when btn 'login' is clicked
    public Client(String username){
        this.username = username;
        try {
            this.addr = InetAddress.getByName(DOMAIN);
            this.socket = new Socket(this.addr,PORT);
            this.objOut = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
//            this.objIn = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            this.sendMsg("LOGIN","","");
            wait(1);
            this.objIn = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            MessageHandler messageHandler = new MessageHandler(this.socket);
            Thread thread = new Thread(messageHandler);
            thread.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Client could not make connection: " + e);
            System.exit(-1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //sending message
    public void sendMsg(String type,String receiver,String content) throws IOException {
        Message msg = new Message(type,this.username,receiver,content);
        objOut.writeObject(msg);
    }

    //dealing with sockets sent to the client
    private class MessageHandler implements Runnable{
        private Socket socket;
//        private String username;
        public MessageHandler(Socket socket){
            this.socket = socket;
//            this.username = username;
        }

        @Override
        public void run() {
            Message msg = null;
            String type = "";
            String sender = "";
            String content = "";
            try {
                msg = (Message)objIn.readObject();
                type = msg.getType();
                sender = msg.getSender();
                content = msg.getContent();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            do{
                if(type == "CHAT"){
                    //TODO output content
                    System.out.println(sender+" send a message: "+content);
                } else {
                    //TODO TBD
                    System.out.println("other than chat");
                    System.out.println(type);
                }
            } while(true);
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("input your username:");
        Scanner input = new Scanner(System.in);
        String username = input.nextLine();
        Client client = new Client(username);

        //simulating an event triggered msg-sending procedure
        while(true){
            //target user
            System.out.println("please input a target username");
            String target = input.nextLine();
            System.out.println("please type a message");
            String content = input.nextLine();
            client.sendMsg("CHAT",target,content);
        }
    }
}
