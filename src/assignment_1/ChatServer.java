package assignment_1;

/**
 A class that represents a server in a number guessing game where
 GuessClient objects connect to this GuessServer and try to guess
 a random integer value between min (incl) and max (excl)
 The game initiates with a response from the server and ends when
 the server responds with "Correct guess!"
 @author Andrew Ensor
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {

    private ArrayList<String> userList = new ArrayList<String>();
    public static final int PORT = 7777; // some unused port number

    private boolean stopRequested;

    public ChatServer() {
        stopRequested = false;
    }

    // start the server if not already started and repeatedly listen
// for client connections until stop requested
    public void startServer() {
        stopRequested = false;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started at " + InetAddress.getLocalHost() + " on port " + PORT);
        } catch (IOException e) {
            System.err.println("Server can't listen on port: " + e);
            System.exit(-1);
        }
        try {
            while (!stopRequested) {
                Socket socket = serverSocket.accept();
                System.out.println("Connection made with " + socket.getInetAddress());
                // TODO: add code to get name of the user
                ChatTransaction chatTransaction = new ChatTransaction(socket);
                Thread thread = new Thread(chatTransaction);
                thread.start();
            }
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Can't accept client connection: " + e);
        }
        System.out.println("Server finishing");
    }

    // stops server AFTER the next client connection has been made
// (since this server socket doesn't timeout on client connections)
    public void requestStop() {
        stopRequested = true;
    }

    // driver main method to test the class
    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.startServer();
    }

    // inner class that represents a single game played across a socket
    private class ChatTransaction implements Runnable {
        private String name;
        private Socket socket; // socket for client/server communication
// constructor for a guess game to guess value across a socket
// for client/server communication

        public ChatTransaction(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            PrintWriter pw; // output stream to client
            BufferedReader br; // input stream from client
            try {
                pw = new PrintWriter(socket.getOutputStream(), true);
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // TODO: send the list of users to the client

                do {
                    System.out.println("Waiting for client request");
                    String clientRequest = br.readLine();
                    System.out.println("New client message received:" + clientRequest);
                    String response;

                    if (clientRequest.startsWith("Initialise")) {
                        // TODO: process initial information
                        pw.println("Client initialised");
                    } else if (clientRequest.startsWith("Msg")) {
                        // TODO: process new message
                        pw.println("Msg received");
                    } else if (clientRequest.startsWith("sendUpdate")) {
                        // TODO: send new userlist if updated
                        // TODO: send all new messages for this client
                        pw.println("Client sent updates");
                    } else if (clientRequest.startsWith("terminate")) {
                        // TODO: remove client from the list
                        pw.println("Client terminating, closing socket");
                        break;
                    } else {
                        pw.println("Unexpected client message");
                    }

                } while (true);
                pw.close();
                br.close();
                System.out.println("Closing connection with " + socket.getInetAddress());
                socket.close();
            } catch (IOException e) {
                System.err.println("Server error with game: " + e);
            }
        }
    }
}
