package assignment_1.Test;

import assignment_1.Model.Server;

import java.io.IOException;
import java.util.Scanner;


public class ServerDemo {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        /*
         * To start a server.
         *
         * If you face some problem with the default port,
         * you can use chatServer.init(int port, int backlog)
         * to specify a port yourself.
         * Do not forget to change the port in client if you did that above.
         *
         */
        Server chatServer = new Server();
        chatServer.init();
        chatServer.startSocketListener();

        /*
         * Listening for a server-stopping instruction, which is "q".
         * I wrote a method for customer timer input, but I removed it by mistake,
         * so you can set a different timer manually before starting the server if you want to.
         *
         * To stop the server, we will warn all the clients first by calling stopWarn(int timer),
         * then server will waiting for all the clients to logout.
         * As soon as no client connected, the server will then stop immediately.
         * Or some clients will be forced to logout when time is over, by sending a force offline
         * message to these clients and logout for them automatically.
         *
         * When stopping the server, we stop the looping in the thread of serverSocket,
         * which is followed by a "poison pill" socket created to treat serverSocket.accept().
         * After the serverSocket accepted the poison pill, it will call Thread.sleep(int),
         * which enables poison pill to be stopped and any other procedures related to serverSocket
         * to be done
         *
         * Finally all the threads and sockets are closed perfectly, server process exit.
         */
        final int timer = 10;
        Scanner scanner = new Scanner(System.in);
        String stopCtrl = "";
        /*
         * Waiting for the correct instruction.
         */
        while(!(stopCtrl = scanner.next()).equals("q"));
        /*
         * To notify clients to logout.
         */
        chatServer.stopWarn(timer);
        int count = 0;
        long startTime =  System.currentTimeMillis();
        /*
         * Waiting for user to logout.
         */
        while((count = chatServer.countAll())>0){
            /*
             * To send force stop request to clients who have not logout when time is out.
             */
            if((System.currentTimeMillis()-startTime)/1000 >= timer){
                chatServer.sendAll("FORCE_STOP","Server Stopped!");
                break;
            }
        }
        /*
         * To stop the server.
         */
        chatServer.stopServer();
    }

}
