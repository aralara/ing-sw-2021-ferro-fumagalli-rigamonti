package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.view.VirtualView;

import java.util.ArrayList;
import java.util.List;

public class GameHandler implements Runnable{

    private List<VirtualView> clientsVirtualView;
    private int size;
    private boolean isStarted;
    private Controller controller;
    Thread gameHandler;

    GameHandler(int size) {
        clientsVirtualView = new ArrayList<>();
        isStarted = false;
        this.size = size;
        controller = new Controller(size);
    }

    @Override
    public void run(){

        gameHandler = new Thread(this::handleGame);
        gameHandler.start();

    }

    public void add(VirtualView client) {
        clientsVirtualView.add(client);
        controller.addPlayer(client.getNickname());
        notify(client.getNickname());
    }

    public void notify(String nickname){
        for(int i = 0; i < clientsVirtualView.size();i++){
            clientsVirtualView.get(i).getClient().notifyNewPlayer(nickname);
        }
    }

    private void handleGame() {
        //TODO: non entra se partita da 2 persone, si se partita da 1 persona(solo la prima volta), perche?
        while (true) {  //TODO: gestione game
            if (!isStarted) {
                if (size == clientsVirtualView.size()) {
                    isStarted = true;
                    System.out.println("Il gioco sta per iniziare!!");
                    for (int i = 0; i < clientsVirtualView.size(); i++) {
                        Thread thread = new Thread(clientsVirtualView.get(i).getClient());  //fa partire client handler
                        thread.start();
                    }
                }
            }
        }
    }
}
