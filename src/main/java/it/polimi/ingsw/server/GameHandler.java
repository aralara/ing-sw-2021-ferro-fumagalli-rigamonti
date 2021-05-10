package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.games.Game;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.ActionMessage;
import it.polimi.ingsw.utils.messages.Message;
import it.polimi.ingsw.utils.messages.server.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
        setup();
        sendAll(new AskLeaderCardDiscardMessage());
        getResourcesToEqualize(); //TODO: RIGA DA TOGLIERE
        sendAll(new StartTurnMessage(clientsVirtualView.get(0).getNickname()));//TODO: RIGA DA TOGLIERE
        while(true) {
            //TODO: da metterci qualcosa?
        }
    }

    private void getResourcesToEqualize(){   //TODO: METODO DA TOGLIERE, solo x test
        List<Resource> res1 = new ArrayList<>();
        List<Resource> res2 = new ArrayList<>(); res2.add(new Resource(ResourceType.WILDCARD, 1));
        List<Resource> res3 = new ArrayList<>(); res3.add(new Resource(ResourceType.WILDCARD, 1));
                                                 res3.add(new Resource(ResourceType.FAITH, 1));
        List<Resource> res4 = new ArrayList<>(); res4.add(new Resource(ResourceType.WILDCARD, 2));
                                                 res4.add(new Resource(ResourceType.FAITH, 1));

        int num = clientsVirtualView.size();
        ResourcesEqualizeMessage message;
        message = new ResourcesEqualizeMessage(res1);
        clientsVirtualView.get(0).sendMessage(message);
        if(num>1){
            message = new ResourcesEqualizeMessage(res2);
            clientsVirtualView.get(1).sendMessage(message);
        }
        if(num>2){
            message = new ResourcesEqualizeMessage(res3);
            clientsVirtualView.get(2).sendMessage(message);
        }
        if(num>3){
            message = new ResourcesEqualizeMessage(res4);
            clientsVirtualView.get(3).sendMessage(message);
        }
    }

    private void setup() {
        controller.initGame(clientsVirtualView);

        Game game = controller.getGame();
        List<PlayerBoard> playerBoards = game.getPlayerBoards();
        for (VirtualView virtualView : clientsVirtualView) {
            for(PlayerBoard pBoard : playerBoards) {
                PlayerBoardSetupMessage pBoardMessage = new PlayerBoardSetupMessage(pBoard);

                // The cards relative to the hand are hidden to the player if the message isn't being sent to the
                // owner of the LeaderCard hand
                if(!pBoard.getPlayer().getNickname().equals(virtualView.getNickname()))
                    pBoardMessage.hide();

                virtualView.sendMessage(pBoardMessage);
            }

            virtualView.sendMessage(new MarketMessage(game.getMarket()));
            virtualView.sendMessage(new DevelopmentDecksMessage(game.getDevelopmentDecks()));
            virtualView.sendMessage(new FaithTrackMessage(game.getFaithTrack()));
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
