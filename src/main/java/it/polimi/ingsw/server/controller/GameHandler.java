package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.client.cli.GraphicalCLI;
import it.polimi.ingsw.server.model.games.SingleGame;
import it.polimi.ingsw.utils.exceptions.LibraryNotLoadedException;
import it.polimi.ingsw.utils.exceptions.NotExistingNicknameException;
import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.games.Game;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.saves.GameLibrary;
import it.polimi.ingsw.server.saves.GameSave;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.client.ClientActionMessage;
import it.polimi.ingsw.utils.messages.Message;
import it.polimi.ingsw.utils.messages.server.action.*;
import it.polimi.ingsw.utils.messages.server.update.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Class that handles messages, connection and the setup/end of the game
 */
public class GameHandler implements Runnable {

    private final AtomicBoolean active;
    private final int size;
    private final AtomicInteger sizeSetup;
    private final List<VirtualView> clientsVirtualView;
    private Controller controller;
    private GameSave save;

    /**
     * Constructor for a GameHandler with a specified size
     * @param size Size of the game that will be created
     */
    public GameHandler(int size) {
        this.active = new AtomicBoolean(true);
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

    /**
     * Initializes a new Game and its components from a saved one
     * @param save Save that will be used as a reference
     */
    public void startFromSave(GameSave save) {
        this.save = save;
        try {
            save.load();
            Game game = save.getGame();
            controller = new Controller(game);
            controller.initSavedGame(clientsVirtualView);
            sizeSetup.set(size);
            if(game.isFinished()) {     // In case the loaded game has already ended, displays the results to all the players and stops the game
                sendAll(new EndGameMessage(game.getEndPlayerList(), false));
                stop(false);
            }
            else {
                updateClients(game);
                resumeGame();
            }
        } catch(IOException | ClassNotFoundException e) {
            startNewGame();
        }
    }

    /**
     * Initializes a brand new game and its components
     */
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

    /**
     * Sends to the players all the relevant information about the game
     * @param game Game that will be used as a reference
     */
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
            if(size == 1)
                virtualView.sendMessage(new LorenzoFaithMessage(((SingleGame)game).getLorenzoBoard().getFaith()));
        }
    }

    /**
     * Resumes the game by sending a StartTurnMessage to all the players
     */
    public void resumeGame() {
        sendAll(new StartTurnMessage(controller.getGame().getPlayingNickname()));
    }

    /**
     * Checks if all the players have finished their setup
     * @return Returns true if all the players have finished their setup, false otherwise
     */
    public boolean playerFinishedSetup() {
        return sizeSetup.incrementAndGet() == size;
    }

    /**
     * Adds a player to the game
     * @param view VirtualView of the player that needs to be added
     * @return Returns true if all the player is added correctly, false otherwise
     */
    public boolean add(VirtualView view) {
        view.setGameHandler(this);
        clientsVirtualView.add(view);
        new Thread(view).start();
        return sendAll(new NewPlayerMessage(view.getNickname()));
    }

    /**
     * Sends a message to all the players
     * @param message Message that needs to be sent
     * @return Returns true if all the messages are sent correctly, false otherwise
     */
    public boolean sendAll(Message message) {
        boolean success = true;
        for (VirtualView virtualView : clientsVirtualView)
            success &= virtualView.sendMessage(message);
        return success;
    }

    /**
     * Activates the action of a given message
     * @param view VirtualView of the player that sent the message
     * @param message Message received
     */
    public void handleMessage(VirtualView view, Message message) {
        if (message instanceof ClientActionMessage) {
            ((ClientActionMessage) message).doAction(view);
        } else {
            GraphicalCLI.printlnString("Can't handle message");
        }
    }

    /**
     * Saves the game on its dedicated file
     * @param nickname Nickname of the player that is requesting to save the game in order to check if it's their turn
     *                 (if null, it will always save the game)
     * @return Returns true if the game has been saved successfully, false otherwise
     */
    public boolean saveGame(String nickname) {
        if(controller.getPlayingNickname().equals(nickname) &&
                controller.getPlayerBoard(nickname).isTurnPlayed() &&       // The player must have already played their action and it must be their turn
                sizeSetup.get() >= size) {                                  // It's not possible to save a game during the setup phase
            try {
                save.save();
                return true;
            } catch (IOException e) {
                GraphicalCLI.printlnString("Unable to save game");
            }
        }
        return false;
    }

    /**
     * Interrupts the game, sends a non-propagating stop message to all of the players and tries to save the game
     * @param sendMessage If true sends a message to all the clients, notifying them of the disconnection
     */
    public void stop(boolean sendMessage) {
        if(active.getAndSet(false)) {
            if(save != null && !save.isSaved())    // Deletes the save in case the game hasn't been saved or loaded from a save
                try {
                    GameLibrary.getInstance().deleteSave(save);
                } catch(LibraryNotLoadedException e) {
                    e.printStackTrace();
                }
            for (VirtualView virtualView : clientsVirtualView)
                virtualView.stop(false, sendMessage);
        }
    }

    /**
     * Gets if the game is full
     * @return Returns true if the theoretical size of the game matches the number of players, false otherwise
     */
    public boolean isFull() {
        return size == clientsVirtualView.size();
    }

    /**
     * Gets a list of the nicknames of all the players
     * @return Returns a list of nicknames
     */
    public List<String> getAllNicknames() {
        return clientsVirtualView.stream().map(VirtualView::getNickname).collect(Collectors.toList());
    }

    /**
     * Returns a VirtualView given the nickname of its player
     * @return Returns the VirtualView
     */
    public VirtualView getFromNickname(String nickname) throws NotExistingNicknameException {
        for (VirtualView virtualView : clientsVirtualView)
            if(virtualView.getNickname().equals(nickname))
                return virtualView;
        throw new NotExistingNicknameException();
    }

    /**
     * Gets the active attribute
     * @return Returns active value
     */
    public boolean isActive() {
        return active.get();
    }

    /**
     * Gets the size attribute
     * @return Returns size value
     */
    public int getSize() {
        return size;
    }

    /**
     * Gets the sizeSetup attribute
     * @return Returns sizeSetup value
     */
    public int getSizeSetup() {
        return sizeSetup.get();
    }

    /**
     * Gets the controller attribute
     * @return Returns controller value
     */
    public Controller getController() {
        return controller;
    }

}
