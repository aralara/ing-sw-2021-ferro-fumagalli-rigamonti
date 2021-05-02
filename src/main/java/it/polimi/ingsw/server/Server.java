package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public final static int SOCKET_PORT = 1919;

    public static void main(String[] args) {
        ServerSocket socket;

        try {
            socket = new ServerSocket(SOCKET_PORT);
        } catch (IOException e) {
            System.out.println("Error! Cannot open server socket");
            System.exit(1);
            return;
        }

        while (true) {
            try {
                Socket client = socket.accept();
                ClientHandler clientHandler = new ClientHandler(client);
                Thread thread = new Thread(clientHandler, "server_" + client.getInetAddress()) ;
                thread.start();
            } catch (IOException e) {
                System.out.println("Error! Connection dropped");
            }
        }
    }
}
