package assignment_1.Model;

import java.net.Socket;

public class MessageListener implements Runnable{
    private Socket socket;
    private boolean stopctl;
    private Message msg;
    private String type;
    private String sender;
    private String receiver;
    private String content;

    public MessageListener(Socket socket){
        this.socket = socket;
        this.stopctl = false;
    }

    public void run(){
        //TODO blablabla
    }

}
