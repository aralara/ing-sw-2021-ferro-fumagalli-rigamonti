package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.Message;
import it.polimi.ingsw.utils.messages.server.NewPlayerMessage;
import it.polimi.ingsw.utils.messages.server.PlayerBoardSetupMessage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameHandler implements Runnable{

    private final List<VirtualView> clientsVirtualView;
    private final Controller controller;

    GameHandler(int size) {
        clientsVirtualView = new ArrayList<>();
        controller = new Controller(size);
    }

    @Override
    public void run(){
        for (VirtualView virtualView : clientsVirtualView) {
            Thread thread = new Thread(virtualView.getClient());
            thread.start();
        }
        controller.initGame(clientsVirtualView);
        while(true) {
            //TODO: da metterci qualcosa?
        }
    }

    public void add(Socket client, ObjectOutputStream out, ObjectInputStream in, String nickname) {
        clientsVirtualView.add(new VirtualView(nickname, new ClientHandler(client, out, in, controller)));
        controller.addPlayer(nickname);
        sendAll(new NewPlayerMessage(nickname));
    }

    public void sendAll(Message message){
        for (VirtualView virtualView : clientsVirtualView)
            virtualView.sendMessage(message);
    }
}
