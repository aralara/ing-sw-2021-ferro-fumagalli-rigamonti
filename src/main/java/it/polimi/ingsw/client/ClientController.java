package it.polimi.ingsw.client;

import it.polimi.ingsw.client.structures.*;
import it.polimi.ingsw.utils.exceptions.NotExistingNicknameException;
import it.polimi.ingsw.server.controller.GameHandler;
import it.polimi.ingsw.server.model.boards.Player;
import it.polimi.ingsw.server.model.cards.card.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.card.LorenzoCard;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.server.saves.GameSave;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.PipedPair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a generic client capable to play the game
 */
public abstract class ClientController {
    private String nickname;
    private int numberOfPlayers;
    private int lorenzoFaith;
    private final List<PlayerBoardView> playerBoards;
    private MarketView market;
    private DevelopmentDecksView developmentDecks;
    private FaithTrackView faithTrack;
    private boolean mainActionPlayed, playerTurn;
    private final MessageHandler messageHandler;
    private final UpdateMessageReader updateMessageReader;
    private boolean alive;

    private GameHandler gameHandler;


    /**
     * Constructor for a client controller
     */
    public ClientController() {
        lorenzoFaith = -1;
        playerBoards = new ArrayList<>();
        market = null;
        developmentDecks = null;
        faithTrack = null;
        mainActionPlayed = false;
        playerTurn = false;
        messageHandler = new MessageHandler();
        updateMessageReader = new UpdateMessageReader(this, messageHandler.getUpdateQueue());
        alive = false;
        gameHandler = null;
    }


    /**
     * Setup method used to establish a connection and decide the game mode
     */
    public abstract void setup();

    /**
     * Client main method
     */
    public abstract void run();

    /**
     * Visualizes a notification contained in an ACk message
     * @param message Text to visualize
     * @param visual Optional flag, can be used to decide if the message must be visualized or not
     */
    public abstract void ackNotification(String message, boolean visual);

    /**
     * Asks the nickname to the player
     */
    public abstract void askNickname();

    /**
     * Notifies the client of the lobby's size and the waiting players, eventually asks for a new lobby
     * @param lobbySize Size of the lobby (if 0, a new lobby must be created)
     * @param waitingPlayers Currently waiting players inside the lobby
     */
    public abstract void askNewLobby(int lobbySize, int waitingPlayers);

    /**
     * Notifies that a new player has been added to the lobby
     * @param nickname Nickname of the new player
     */
    public abstract void notifyNewPlayer(String nickname);

    /**
     * Displays a list of saves from which the user can choose from
     * @param saves List of saves to display
     */
    public abstract void displaySaves(List<GameSave> saves);

    /**
     * Asks the user to discard their leader cards at the start of the game
     */
    public abstract void askLeaderDiscard();

    /**
     * Asks the user to equalize a list of resources at the start of the game
     * @param resources List of resources to equalize
     */
    public abstract void askResourceEqualize(List<Resource> resources);

    /**
     * Notifies the user when a new turn has begun
     * @param nickname Nickname of the new current player
     */
    public abstract void notifyStartTurn(String nickname);

    /**
     * Makes the player add the resources chosen from the market
     * @param resources List of chosen resources
     * @param availableAbilities List of available marble abilities
     */
    public abstract void addMarketResources(List<Resource> resources, List<ResourceType> availableAbilities);

    /**
     * Notifies the player that Lorenzo has pulled a new card (in a single player game)
     * @param lorenzoCard Card that lLorenzo has pulled
     */
    public abstract void notifyLorenzoCard(LorenzoCard lorenzoCard);

    /**
     * Notifies the player that it's the last round of the game
     */
    public abstract void notifyLastRound();

    /**
     * Notifies the player that the game has ended
     * @param players List of scores that the players achieved during the game (null if the end of the game was
     *                reached due to a disconnection)
     * @param disconnection True if the end of the game was reached due to a disconnection
     */
    public abstract void notifyEndGame(List<Player> players, boolean disconnection);

    /**
     * Makes the player choose a development card to buy from the available development decks
     */
    public abstract void selectDevDecks();

    /**
     * Makes the player choose a list of productions to activate from the ones available to them
     */
    public abstract void selectProductions();

    /**
     * Makes the player choose how to place a list of resources on their warehouse shelves
     * @param resources List of resources to place
     */
    public abstract void placeResourcesOnShelves(List<Resource> resources);

    /**
     * Makes the player choose from which storage they want to take the resources from in order to
     * buy a development card
     * @param cardToBuy Card that the player wants to buy
     * @param spaceToPlace Space where the player wants to place the bought card
     * @param cost Gross cost of the bought card
     */
    public abstract void chooseDevelopmentStorages(DevelopmentCard cardToBuy,
                                                                     int spaceToPlace, List<Resource> cost);

    /**
     * Makes the player choose from which storage they want to take the resources from in order to
     * activate a list of productions
     * @param productionsToActivate Productions that the player wants to activate
     * @param consumed Gross resources consumed by the productions
     */
    public abstract void chooseProductionStorages(List<Production> productionsToActivate,
                                                                    List<Resource> consumed);


    /**
     * Setup routine for a local game
     * @throws IOException Throws an IOException if there is problem during the pipe connection
     */
    public void localSetup() throws IOException {
        PipedPair pipedPairHandler = new PipedPair();
        PipedPair pipedPairView = pipedPairHandler.connect();
        Thread t = new Thread(() -> {
            boolean success;
            do
                success = getMessageHandler().connect(pipedPairView);
            while(!success);
            getMessageHandler().run();
        });
        t.start();

        VirtualView virtualView = new VirtualView(
                new ObjectOutputStream(pipedPairHandler.getPipeOut()),
                new ObjectInputStream(pipedPairHandler.getPipeIn()),
                getNickname());
        setLocalGameHandler(new GameHandler(1));
        getLocalGameHandler().add(virtualView);
        getLocalGameHandler().run();
    }

    /**
     * Destroys the client object and its related threads
     */
    public void destroy() {
        alive = false;
        messageHandler.stop();
        updateMessageReader.stop();
    }

    /**
     * Gets the nickname attribute
     * @return Returns nickname value
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the nickname attribute
     * @param nickname Attribute to be set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Gets the numberOfPlayers attribute
     * @return Returns numberOfPlayers value
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     * Sets the numberOfPlayers attribute
     * @param numberOfPlayers Attribute to be set
     */
    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        if(numberOfPlayers == 1)
            setLorenzoFaith(0);
    }

    /**
     * Gets the lorenzoFaith attribute
     * @return Returns lorenzoFaith value
     */
    public int getLorenzoFaith() {
        return lorenzoFaith;
    }

    /**
     * Sets the lorenzoFaith attribute
     * @param lorenzoFaith Attribute to be set
     */
    public void setLorenzoFaith(int lorenzoFaith) {
        this.lorenzoFaith = lorenzoFaith;
    }

    /**
     * Gets the playerBoards attribute
     * @return Returns playerBoards value
     */
    public List<PlayerBoardView> getPlayerBoards() {
        return playerBoards;
    }

    /**
     * Gets the market attribute
     * @return Returns market value
     */
    public MarketView getMarket() {
        return market;
    }

    /**
     * Sets the market attribute
     * @param market Attribute to be set
     */
    public void setMarket(MarketView market) {
        this.market = market;
    }

    /**
     * Gets the developmentDecks attribute
     * @return Returns developmentDecks value
     */
    public DevelopmentDecksView getDevelopmentDecks() {
        return developmentDecks;
    }

    /**
     * Sets the developmentDecks attribute
     * @param developmentDecks Attribute to be set
     */
    public void setDevelopmentDecks(DevelopmentDecksView developmentDecks) {
        this.developmentDecks = developmentDecks;
    }

    /**
     * Gets the faithTrack attribute
     * @return Returns faithTrack value
     */
    public FaithTrackView getFaithTrack() {
        return faithTrack;
    }

    /**
     * Sets the faithTrack attribute
     * @param faithTrack Attribute to be set
     */
    public void setFaithTrack(FaithTrackView faithTrack) {
        this.faithTrack = faithTrack;
    }

    /**
     * Gets the mainActionPlayed attribute
     * @return Returns mainActionPlayed value
     */
    public boolean isMainActionPlayed() {
        return mainActionPlayed;
    }

    /**
     * Sets the mainActionPlayed attribute
     * @param mainActionPlayed Attribute to be set
     */
    public void setMainActionPlayed(boolean mainActionPlayed) {
        this.mainActionPlayed = mainActionPlayed;
    }

    /**
     * Gets a playerBoard from the nickname of the associated player
     * @param nickname Nickname of the chosen player
     * @return Returns the playerBoard
     * @throws NotExistingNicknameException Throws a NotExistingNicknameException if the nickname of the
     *                                      player doesn't exist
     */
    public PlayerBoardView playerBoardFromNickname(String nickname) throws NotExistingNicknameException {
        for(PlayerBoardView playerBoard : playerBoards)
            if(playerBoard.getNickname().equals(nickname))
                return playerBoard;
        throw new NotExistingNicknameException();
    }

    /**
     * Gets the playerBoard of the local player
     * @return Returns the playerBoard
     * @throws NotExistingNicknameException Throws a NotExistingNicknameException if the nickname of the
     *                                      player doesn't exist
     */
    public PlayerBoardView getLocalPlayerBoard() throws NotExistingNicknameException {
        return playerBoardFromNickname(nickname);
    }

    /**
     * Gets the playerTurn attribute
     * @return Returns playerTurn value
     */
    public boolean isPlayerTurn() {
        return playerTurn;
    }

    /**
     * Sets the playerTurn attribute
     * @param playerTurn Attribute to be set
     */
    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }

    /**
     * Gets the messageHandler attribute
     * @return Returns messageHandler value
     */
    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    /**
     * Gets the updateMessageReader attribute
     * @return Returns updateMessageReader value
     */
    public UpdateMessageReader getUpdateMessageReader() {
        return updateMessageReader;
    }

    /**
     * Gets the alive attribute
     * @return Returns alive value
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Sets the alive attribute
     * @param alive Attribute to be set
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * Gets the gameHandler attribute
     * @return Returns gameHandler value
     */
    public GameHandler getLocalGameHandler() {
        return gameHandler;
    }

    /**
     * Sets the gameHandler attribute
     * @param gameHandler Attribute to be set
     */
    public void setLocalGameHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }
}
