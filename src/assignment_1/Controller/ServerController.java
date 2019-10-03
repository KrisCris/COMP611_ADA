package assignment_1.Controller;

import assignment_1.Model.Message;
import assignment_1.Model.Server;
import assignment_1.View.ServerCLI;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerController {
    /*
     * No need to check this class because the MVC Pattern haven't been implemented to server, due to the limit of time.
     * Currently, the running of server only relies on /Model/Server.class and /Test/ServerDemo.class.
     *
     * While I did had some idea about the "MVC" of server:
     * 1. Server controller observing both server (Model) and a CLI (View).
     * 2. Model only owns a bunch of map/list which contains sockets of every online client and some getter and setters.
     * 3. Controller:
     * 3.1. Controller has a thread for serverSocket listening for requests from new client.
     * 3.1. It also has a inner class, which is now in the Server.class, implementing runnable for each socket listening for its client requests.
     * 3.2. Besides, there are a list of methods to deal with messages received and model/view control in the Controller.
     * 3.3  Most of the methods in Server.class will be moved to here.
     * 4. View
     * 4.1. The View is for inputting parameters that control the way server works including to stop the server.
     * 4.2. It also could have some abilities to output some logs to the screen or disk for debugging or something.
     * 4.3. All these functionality are observed and called by its Controller.
     * Above may be possible to be implemented in the future.
     *
     * BTW, the Client side is based on MVC.
     */
    private Server model;
    private ServerCLI view;

    public ServerController(Server model,ServerCLI view){
        this.model = model;
        this.view = view;
    }

    /*
     * Some methods that control the model:
     */
    private void addToOnlineList(){

    }
    private void addToStealthList(){

    }
    private void stopServer(){

    }

    /*
     * Some methods that listen for the view
     */
    public void processCommand(String cmd){
        //decoding command
        //to call other methods
    }

    /*
     * Some methods controlling the view
     */
    private void print(String type,String content){
        //calling actual print methods in view
    }
    private void log(int begin, int end){
        //calling actual print methods in view
    }

    /*
     * Some methods for communication
     */
    private void init(){

    }

    private void sendMsgToAll(Message msg){

    }



    /*
     * The listener for client-server communications.
     */
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

        private boolean stopCtrl;

        public ChatHandler(Socket socket){
            this.socket = socket;
            this.stopCtrl = false;
        }

        /*
         * deal with different types of messages.
         */
        private void msgReactor(){

        }

        /*
         * The real methods for sending messages.
         */
        public void sendMsg(Message msg){

        }

        @Override
        public void run() {
            do{

            } while(!stopCtrl);
        }

        /*
         * To stop this client-server communication thread.
         */
        public void setStopCtrl(boolean bool){
            this.stopCtrl = bool;
        }
    }


}
