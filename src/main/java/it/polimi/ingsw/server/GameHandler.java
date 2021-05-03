package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameHandler implements Runnable{

    private List<Socket> clients;

    GameHandler() {
        clients=new ArrayList<>();
    }

    @Override
    public void run(){
        try {
            handleGame();
        } catch (IOException e) {
            System.out.println("client " + clients.get(clients.size()-1).getInetAddress() + " connection dropped");
        }
    }

    public void add(Socket client) {
        clients.add(client);
    }

    private void handleGame() throws IOException{

        while (true){  //TODO: gestione game



        }

    }
}
