package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.Controller;

import java.util.ArrayList;
import java.util.List;

public class GameHandler implements Runnable{

    private List<ClientHandler> clients;
    private int size;
    private boolean isStarted;
    private Controller controller;
    Thread gameHandler;

    GameHandler(int size) {
        clients=new ArrayList<>();
        isStarted = false;
        this.size = size;
        controller = new Controller(size);
    }

    @Override
    public void run(){

        gameHandler = new Thread(this::handleGame);
        gameHandler.start();

    }

    public void add(ClientHandler client) {
        clients.add(client);
        controller.addPlayer(client.getNickname());
        notify(client.getNickname());
    }

    public void notify(String nickname){
        for(int i = 0; i < clients.size();i++){
            clients.get(i).notifyNewPlayer(nickname);
        }
    }

    private void handleGame() {
        //TODO: non entra se partita da 2 persone, si se partita da 1 persona(solo la prima volta), perche?
        while (true) {  //TODO: gestione game
            if (!isStarted) {
                if (size == clients.size()) {
                    isStarted = true;
                    System.out.println("Il gioco sta per iniziare!!");
                    for (int i = 0; i < clients.size(); i++) {
                        Thread thread = new Thread(clients.get(i));
                        thread.start();
                    }
                }
            }
        }
    }
}
