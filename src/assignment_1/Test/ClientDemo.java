package assignment_1.Test;

import assignment_1.Controller.ClientController;
import assignment_1.Model.Client;
import assignment_1.View.ChatRoom;
import assignment_1.View.Login;

public class ClientDemo {
    public static void main(String[] args) {
        //model
        Client client = new Client();
        //view
        Login login = new Login();
        ChatRoom chatRoom = new ChatRoom();
        //controller
        ClientController clientController = new ClientController(client,login,chatRoom);
        //To register observer
        login.registerObserver(clientController);
        chatRoom.registerObserver(clientController);
    }
}
