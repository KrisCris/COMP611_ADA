package assignment_1.Test;

import assignment_1.Model.Server;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerDemo {
    public static void main(String[] args) throws IOException {
        Server chatServer = new Server();
        chatServer.init();
        chatServer.startSocketListener();

        //shutdown 5

    }

}
