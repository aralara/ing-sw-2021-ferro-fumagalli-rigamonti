package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.ActionMessage;
import it.polimi.ingsw.utils.messages.Message;
import it.polimi.ingsw.utils.messages.server.AskLeaderCardDiscardMessage;
import it.polimi.ingsw.utils.messages.server.NewPlayerMessage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameHandler implements Runnable {

    private final List<VirtualView> clientsVirtualView;
    private final Controller controller;

    GameHandler(int size) {
        clientsVirtualView = new ArrayList<>();
        controller = new Controller(size);
    }

    @Override
    public void run(){
        for (VirtualView virtualView : clientsVirtualView) {
            Thread thread = new Thread(virtualView);
            thread.start();
        }
        controller.initGame(clientsVirtualView);  //TODO: giusto gestire controller qui o tutto nei doAction()?
        //sendAll(new AskLeaderCardDiscardMessage());
        while(true) {
            //TODO: da metterci qualcosa?
        }
    }

    public void add(Socket client, ObjectOutputStream out, ObjectInputStream in, String nickname) {
        clientsVirtualView.add(new VirtualView(client, out, in, nickname, this));
        controller.addPlayer(nickname);
        sendAll(new NewPlayerMessage(nickname));
    }

    public void sendAll(Message message) {
        for (VirtualView virtualView : clientsVirtualView)
            virtualView.sendMessage(message);
    }

    public void handleMessage(VirtualView view, Message message) {
        if (message instanceof ActionMessage) {
            ((ActionMessage) message).doAction(view, controller);
        } else {
            System.out.println("Can't handle message");
        }
    }

    public List<String> getAllNicknames(){
        List<String> temp = new ArrayList<>();
        for (VirtualView virtualView : clientsVirtualView) {
            temp.add(virtualView.getNickname());
        }
        return temp;
    }
}
