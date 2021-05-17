package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.NotExistingNicknameException;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.games.Game;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.client.ClientActionMessage;
import it.polimi.ingsw.utils.messages.Message;
import it.polimi.ingsw.utils.messages.server.action.AskLeaderCardDiscardMessage;
import it.polimi.ingsw.utils.messages.server.action.NewPlayerMessage;
import it.polimi.ingsw.utils.messages.server.action.ResourcesEqualizeMessage;
import it.polimi.ingsw.utils.messages.server.action.StartTurnMessage;
import it.polimi.ingsw.utils.messages.server.update.DevelopmentDecksMessage;
import it.polimi.ingsw.utils.messages.server.update.FaithTrackMessage;
import it.polimi.ingsw.utils.messages.server.update.MarketMessage;
import it.polimi.ingsw.utils.messages.server.update.PlayerBoardSetupMessage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameHandler implements Runnable {

    private final List<VirtualView> clientsVirtualView;
    private final Controller controller;

    GameHandler(int size) {
        clientsVirtualView = new ArrayList<>();
        controller = new Controller(size);
    }

    @Override
    public void run() {
        for (VirtualView virtualView : clientsVirtualView) {
            Thread thread = new Thread(virtualView);
            thread.start();
        }
        setup();
        while(true) {
            //TODO: busy wait
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
        sendAll(new AskLeaderCardDiscardMessage());
        Map<String, List<Resource>> resEqualize = controller.getResourcesToEqualize();
        if (resEqualize != null) {
            for (Map.Entry<String, List<Resource>> entry : resEqualize.entrySet())
                try {
                    getFromNickname(entry.getKey()).sendMessage(new ResourcesEqualizeMessage(entry.getValue()));
                } catch (NotExistingNicknameException e) {
                    e.printStackTrace();
                }
        }
        sendAll(new StartTurnMessage(controller.getGame().getPlayingNickname()));
    }

    public void add(Socket client, ObjectOutputStream out, ObjectInputStream in, String nickname) {
        clientsVirtualView.add(new VirtualView(client, out, in, nickname, this));
        sendAll(new NewPlayerMessage(nickname));
    }

    public void sendAll(Message message) {
        for (VirtualView virtualView : clientsVirtualView)
            virtualView.sendMessage(message);
    }

    public void handleMessage(VirtualView view, Message message) {
        if (message instanceof ClientActionMessage) {
            ((ClientActionMessage) message).doAction(view, controller);
        } else {
            System.out.println("Can't handle message");
        }
    }

    public List<String> getAllNicknames() {
        List<String> temp = new ArrayList<>();
        for (VirtualView virtualView : clientsVirtualView) {
            temp.add(virtualView.getNickname());
        }
        return temp;
    }

    public VirtualView getFromNickname(String nickname) throws NotExistingNicknameException{
        for (VirtualView virtualView : clientsVirtualView)
            if(virtualView.getNickname().equals(nickname))
                return virtualView;
        throw new NotExistingNicknameException();
    }

}
