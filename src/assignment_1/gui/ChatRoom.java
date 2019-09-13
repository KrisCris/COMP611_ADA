package assignment_1.gui;

import assignment_1.Message;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

public class ChatRoom extends JFrame{
    private static final int DEFAULT_WIDTH = 950;
    private static final int DEFAULT_HEIGHT = 700;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel userList;
    private JPanel chatContents;
    private JScrollPane userScrollPane;
    private JScrollPane chatScrollPane;
    private JScrollPane inputScrollPane;
    private JTextArea chatInput;
    private JButton user;
    private JButton receiver;
    private JButton sendBtn;
    private JButton you;
    private JPanel toolbar;


    public ChatRoom(){
        this.leftPanel = new JPanel();
        this.rightPanel = new JPanel();
        this.userList = new JPanel();
        this.chatContents = new JPanel();
        this.userScrollPane = new JScrollPane();
        this.chatScrollPane = new JScrollPane();
        this.inputScrollPane = new JScrollPane();
        this.chatInput = new JTextArea();
        this.user = new JButton("KRIS");
        this.receiver = new JButton("TEST RECEIVER");
        this.you = new JButton("YOU");
        this.sendBtn = new JButton("SEND");
        this.toolbar = new JPanel();

        this.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
        this.setLayout(new GridBagLayout());
        this.add(this.leftPanel,new GBC(0,0,1,1).setFill(GBC.BOTH).setWeight(0,100).setIpad(130,0));
        this.add(this.rightPanel,new GBC(1,0,1,1).setFill(GBC.BOTH).setWeight(100,100).setIpad(720,0));
        this.leftPanel.setLayout(new GridBagLayout());
        this.leftPanel.add(this.user,new GBC(0,0,1,1).setFill(GBC.BOTH).setWeight(100,0).setIpad(0,25));
        this.leftPanel.add(this.userScrollPane,new GBC(0,1,1,1).setFill(GBC.BOTH).setWeight(100,100).setIpad(0,675));
        this.rightPanel.setLayout(new GridBagLayout());
        this.rightPanel.add(this.receiver,new GBC(0,0,1,1).setFill(GBC.BOTH).setWeight(100,0).setIpad(0,25));
        this.rightPanel.add(this.you,new GBC(1,0,1,1).setFill(GBC.BOTH).setWeight(100,0).setIpad(75,25));
        this.rightPanel.add(this.chatScrollPane,new GBC(0,1,2,1).setFill(GBC.BOTH).setWeight(100,100).setIpad(0,325));
        this.rightPanel.add(this.inputScrollPane,new GBC(0,2,2,1).setFill(GBC.BOTH).setWeight(100,5).setIpad(0,0));
        this.rightPanel.add(this.sendBtn,new GBC(2,2,1,1).setFill(GBC.BOTH).setWeight(0,100).setIpad(0,0));
        this.rightPanel.add(this.toolbar,new GBC(2,0,1,2).setFill(GBC.BOTH).setWeight(0,100));
        this.toolbar.setBackground(Color.BLACK);
        this.userList.setLayout(new GridBagLayout());
        this.inputScrollPane.setViewportView(this.chatInput);
        this.userScrollPane.setViewportView(this.userList);
        this.chatScrollPane.setViewportView(this.chatContents);

        this.chatInput.setLineWrap(true);
        this.chatInput.setWrapStyleWord(true);


        for(int i=0;i<40;i++){
            JButton t = new JButton(""+i);
            this.userList.add(t,new GBC(0,i,1,1).setWeight(100,0).setFill(GBC.HORIZONTAL).setIpad(0,25));
        }
        //a dummy JLabel below all the user btn, to take all the redundant spaces
        this.userList.add(new JLabel(""),new GBC(0,40,1,1).setWeight(0,1));

        ArrayList<Message> al= new ArrayList<>();
        al.add(new Message("CHAT","A","B","京东上的所有商品信息、客户评价、商品咨询、网友讨论等内容，是京东重要的经营资源，未经许可，禁止非法转载使用。\n" +
                "注：本站商品信息均来自于合作方，其真实性、准确性和合法性由信息拥有者（合作方）负责。本站不提供任何保证，并不承担任何法律责任。"));
        al.add(new Message("CHAT","A","B","babla"));
        al.add(new Message("CHAT","B","A","babla"));
        al.add(new Message("CHAT","A","B","京东上的所有商品信息、客户评价、商品咨询、网友讨论等内容，是京东重要的经营资源，未经许可，禁止非法转载使用。\n" +
                "注：本站商品信息均来自于合作方，其真实性、准确性和合法性由信息拥有者（合作方）负责。本站不提供任何保证，并不承担任何法律责任。"));
        al.add(new Message("CHAT","A","B","babla"));
        al.add(new Message("CHAT","B","A","划线价：商品展示的划横线价格为参考价，并非原价，该价格可能是品牌专柜标价、商品吊牌价或由品牌供应商提供的正品零售价（如厂商指导价、建议零售价等）或该商品在京东平台上曾经展示过的销售价；由于地区、时间的差异性和市场行情波动，品牌专柜标价、商品吊牌价等可能会与您购物时展示的不一致，该价格仅供您参考。"));
        al.add(new Message("CHAT","B","A","bablsddsafadsfasfdfasdfsafads\naffdsfdsfsdfsdfsdfdfdsfsdfsdfd"));
        al.add(new Message("CHAT","A","B","babla"));
        al.add(new Message("CHAT","B","A","babla"));
        al.add(new Message("CHAT","A","B","babdsfadfdsafdsfasdfdsfsdfdsfsdfsdfsfdsfsdla"));
        this.chatContents.setLayout(new GridBagLayout());
        String user = "A";
        for(Message m: al){
            int index = al.indexOf(m);
            int side = 0;
            if(m.getSender().equals(user)){
                side = 1;
            }
            String content = m.getContent();
            //
            StringBuilder reBuildStr = new StringBuilder();
            reBuildStr.append("<html>");
            for(String str:content.split("\n"))
            {
                while(str.length()>20){
                    reBuildStr.append(str.substring(0,20));
                    reBuildStr.append("<br>");
                    str = str.substring(20);
                }
                reBuildStr.append(str);
                reBuildStr.append("<br>");
            }
            reBuildStr.append("</html>");
            content = reBuildStr.toString();
            //
            this.chatContents.add(new JButton(content),new GBC(side,index,1,1).setWeight(100,0).setFill(GBC.HORIZONTAL));
            //keep scrolling to bottom
            JScrollBar scrollBar = chatScrollPane.getVerticalScrollBar();
            scrollBar.setValue((scrollBar.getMaximum()));
        }





    }

    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            chatRoom.setVisible(true);
        });
    }
}