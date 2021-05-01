package it.polimi.ingsw.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private Socket client;

    ClientHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run()
    {
        try {
            handleClientConnection();
        } catch (IOException e) {
            System.out.println("client " + client.getInetAddress() + " connection dropped");
        }
    }

    private void handleClientConnection() throws IOException{
        System.out.println("Connected to " + client.getInetAddress());

        ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream input = new ObjectInputStream(client.getInputStream());


        //TODO: da completare con codice client
    }
}
