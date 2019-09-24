package assignment_1.Test;

import assignment_1.Model.Server;

import java.io.IOException;

public class ServerDemo {
    public static void main(String[] args) throws IOException {
        Server chatServer = new Server();
        chatServer.init();
        chatServer.startSocketListener();
    }
}
