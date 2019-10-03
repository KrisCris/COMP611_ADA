package assignment_1.View;

import assignment_1.Controller.ServerController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/*
 * ReadMe:
 * This class is still in progress.
 * The running of server only relies on Server.class and ServerDemo.class
 */
public class ServerCLI {
    private ServerController controller;
    public ServerCLI(){
        /*
         * A thread listening for stop instruction, which is written in ServerDemo.class in this version.
         */
        new Thread(()->{

            String stopCtrl = "";
            Scanner scanner = new Scanner(System.in);
            while(!(stopCtrl = scanner.next()).equals("q"));
            //TODO to call method in controller to stop the server.
        }).start();
    }

    public void registerObserver(ServerController controller){
        this.controller = controller;
    }

    public void print(String type,String content){
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sb.append(df.format(new Date()));
        sb.append("  [ "+type+" ]  ");
        sb.append(content);

    }

    public void log(){

    }
}
