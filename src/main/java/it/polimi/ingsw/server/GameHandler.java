package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.LibraryNotLoadedException;
import it.polimi.ingsw.exceptions.NotExistingNicknameException;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.games.Game;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.saves.GameLibrary;
import it.polimi.ingsw.server.saves.GameSave;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.client.ClientActionMessage;
import it.polimi.ingsw.utils.messages.Message;
import it.polimi.ingsw.utils.messages.server.action.*;
import it.polimi.ingsw.utils.messages.server.update.DevelopmentDecksMessage;
import it.polimi.ingsw.utils.messages.server.update.FaithTrackMessage;
import it.polimi.ingsw.utils.messages.server.update.MarketMessage;
import it.polimi.ingsw.utils.messages.server.update.PlayerBoardSetupMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GameHandler implements Runnable {

    private boolean active;
    private final int size;
    private final AtomicInteger sizeSetup;
    private final List<VirtualView> clientsVirtualView;
    private Controller controller;
    private GameSave save;

    GameHandler(int size) {
        this.active = true;
        this.size = size;
        this.sizeSetup = new AtomicInteger(0);
        clientsVirtualView = new ArrayList<>();
        controller = null;
        save = null;
    }

    @Override
    public void run() {
        try {
            GameLibrary library = GameLibrary.getInstance();
            List<String> players = getAllNicknames();
            if(library.checkSave(players))
                clientsVirtualView.get(0).sendMessage(new GameSavesMessage(library.getSaves(players)));
            else
                startNewGame();
        } catch(LibraryNotLoadedException e) {
            startNewGame();
        }
    }

    public void startFromSave(GameSave save) {  //TODO: controllare se i listener vengono preservati nel salvataggio
        this.save = save;
        try {
            save.load();
            Game game = save.getGame();
            controller = new Controller(game);
            controller.initSavedGame(clientsVirtualView);
            sizeSetup.set(size);
            updateClients(game);
            resumeGame();
        } catch(IOException | ClassNotFoundException e) {
            startNewGame();
        }
    }

    public void startNewGame() {
        controller = new Controller(size);
        Game game = controller.getGame();
        controller.initNewGame(clientsVirtualView);
        try {
            GameLibrary library = GameLibrary.getInstance();
            save = library.createSave(game);
        } catch(LibraryNotLoadedException e) {
            e.printStackTrace();
        }
        updateClients(game);
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
    }

    private void updateClients(Game game) {
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

    public void resumeGame() {
        sendAll(new StartTurnMessage(controller.getGame().getPlayingNickname()));
    }

    public boolean playerFinishedSetup() {
        return sizeSetup.incrementAndGet() == size;
    }

    public void add(Socket client, ObjectOutputStream out, ObjectInputStream in, String nickname) {
        VirtualView view = new VirtualView(client, out, in, nickname, this);
        clientsVirtualView.add(view);
        new Thread(view).start();
        sendAll(new NewPlayerMessage(nickname));
    }

    public void sendAll(Message message) {
        for (VirtualView virtualView : clientsVirtualView)
            virtualView.sendMessage(message);
    }

    public void handleMessage(VirtualView view, Message message) {
        if (message instanceof ClientActionMessage) {
            ((ClientActionMessage) message).doAction(view);
        } else {
            System.out.println("Can't handle message");
        }
    }

    public void stop() {
        if(active) {
            active = false;
            for (VirtualView virtualView : clientsVirtualView)
                virtualView.stop(false);
            if(sizeSetup.get() == size) {
                try {
                    save.save();
                } catch (IOException e) {
                    System.out.println("Unable to save game");
                }
            }
            else {
                try {
                    GameLibrary.getInstance().deleteSave(save);
                } catch (LibraryNotLoadedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isActive() {
        return active;
    }

    public int getSize() {
        return size;
    }

    public boolean isFull() {
        return size == clientsVirtualView.size();
    }

    public List<String> getAllNicknames() {
        return clientsVirtualView.stream().map(VirtualView::getNickname).collect(Collectors.toList());
    }

    public VirtualView getFromNickname(String nickname) throws NotExistingNicknameException{
        for (VirtualView virtualView : clientsVirtualView)
            if(virtualView.getNickname().equals(nickname))
                return virtualView;
        throw new NotExistingNicknameException();
    }

    public Controller getController() {
        return controller;
    }

}
