package assignment_1;

import java.io.Serializable;

public class Message implements Serializable {
    private String MESSAGE;

    private String type;
    private String sender;
    private String receiver;
    private String content;

    public Message(String msg) {
        this.MESSAGE = msg;
        divMsg(this.MESSAGE);
    }

    public Message(String type,String sender,String receiver,String content){
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }

    public Message(String type,String content){
        this.type = type;
        this.sender = "";
        this.receiver = "";
        this.content = content;
    }
    //divide {TYPE=LOGIN}{SENDER=hello}{RECEIVER=world}{CONTENT=helloWorld} into separated strings
    private void divMsg(String msg){
        int indexType = msg.indexOf("{TYPE=");
        int indexSender = msg.indexOf("{SENDER=");
        int indexReceiver = msg.indexOf("{RECEIVER=");
        int indexContent = msg.indexOf("{CONTENT=");
        this.type = msg.substring(indexType+6,indexSender-1);
        this.sender = msg.substring(indexSender+8,indexReceiver-1);
        this.receiver = msg.substring(indexReceiver+10,indexContent-1);
        this.content = msg.substring(indexContent+9,msg.length()-1);
    }

    public String getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

    public static void main(String[] args){
        String eg = "{TYPE=LOGIN}{SENDER=hello}{RECEIVER=world}{CONTENT=helloWorld}";
        Message msg = new Message(eg);
        System.out.println(msg.getType()+"\n"+msg.getSender()+"\n"+msg.getReceiver()+"\n"+msg.getContent());
    }
}
