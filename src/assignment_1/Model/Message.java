package assignment_1.Model;

import java.io.Serializable;

public class Message implements Serializable {
    private static String MESSAGE;

    private String type;
    private String sender;
    private String receiver;
    private String content;

    public Message(String msg) {
        MESSAGE = msg;
        divMsg(MESSAGE);
    }

    /*
     * generally used constructor,
     * which are suitable for all kinds of message
     */
    public Message(String type,String sender,String receiver,String content){
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }
    /*
     * constructor for Server to send notifications to target clients,
     * which neither sender nor receiver parameter are needed.
     */
    public Message(String type,String content){
        this.type = type;
        this.sender = "";
        this.receiver = "";
        this.content = content;
    }
    /*
     * This is the legacy way i decided to use for different kind of messages,
     * which is replaced by sending a Message object directly,
     * thus save efforts implementing further functionality like sending images.
     *
     * The message is something like {TYPE=LOGIN}{SENDER=hello}{RECEIVER=world}{CONTENT=helloWorld}
     * When we read message like this, divMsg() is used to divide that msg into the one we can easily understood.
     */
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

    /*
     * testing the legacy method.
     * no need anymore.
     */
//    public static void main(String[] args){
//        String eg = "{TYPE=LOGIN}{SENDER=hello}{RECEIVER=world}{CONTENT=helloWorld}";
//        Message msg = new Message(eg);
//        System.out.println(msg.getType()+"\n"+msg.getSender()+"\n"+msg.getReceiver()+"\n"+msg.getContent());
//    }
}
