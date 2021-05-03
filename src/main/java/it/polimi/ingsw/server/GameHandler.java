package it.polimi.ingsw.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameHandler implements Runnable{

    private List<ClientHandler> clients;
    private int size;
    private boolean isStarted;

    GameHandler() {
        clients=new ArrayList<>();
        isStarted = false;
    }

    @Override
    public void run(){
        try {
            handleGame();
        } catch (IOException e) {
            System.out.println("client " + clients.get(clients.size()-1).getSocket().getInetAddress() + " connection dropped");
        }
    }

    public void add(ClientHandler client) {
        clients.add(client);
        notify(client.getNickname());
    }

    public void notify(String nickname){
        for(int i = 0; i < clients.size();i++){
            clients.get(i).notifyNewPlayer(nickname);
        }
    }

    public void setSize(int size) {
        this.size = size;
    }

    private void handleGame() throws IOException{
        while (true){  //TODO: gestione game
            if(!isStarted){
                if(size == clients.size()){
                    for(int i = 0 ; i < clients.size(); i++){

                    }
                }
            }
        }
    }
}
