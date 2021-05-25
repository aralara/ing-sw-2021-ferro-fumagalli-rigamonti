package it.polimi.ingsw.client;

import it.polimi.ingsw.client.structures.DevelopmentDeckView;
import it.polimi.ingsw.client.structures.FaithTrackView;
import it.polimi.ingsw.client.structures.MarketView;
import it.polimi.ingsw.client.structures.PlayerBoardView;
import it.polimi.ingsw.exceptions.NotExistingNicknameException;
import it.polimi.ingsw.server.model.boards.Player;
import it.polimi.ingsw.server.model.cards.card.LorenzoCard;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.server.saves.GameSave;

import java.util.ArrayList;
import java.util.List;

public abstract class ClientController {
    private String nickname;
    private int numberOfPlayers;
    private int lorenzoFaith;
    private final List<PlayerBoardView> playerBoards;
    private MarketView market;
    private List<DevelopmentDeckView> developmentDecks;
    private FaithTrackView faithTrack;
    private boolean mainActionPlayed, playerTurn;
    private final MessageHandler messageHandler;
    private final UpdateMessageReader updateMessageReader;


    public ClientController() {
        lorenzoFaith = -1;
        playerBoards = new ArrayList<>();
        market = null;
        developmentDecks = new ArrayList<>();
        faithTrack = null;
        mainActionPlayed = false;
        playerTurn = false;
        messageHandler = new MessageHandler();
        updateMessageReader = new UpdateMessageReader(this, messageHandler.getUpdateQueue());
    }


    public abstract void setup();

    public abstract void run();

    public abstract void ackNotification(String message);


    public abstract void askNickname();

    public abstract void askNewLobby(int lobbySize, int waitingPlayers);

    public abstract void notifyNewPlayer(String nickname);

    public abstract void displaySaves(List<GameSave> saves);

    public abstract void askLeaderDiscard();

    public abstract void askResourceEqualize(List<Resource> resources);

    public abstract void notifyStartTurn(String nickname);

    public abstract void addMarketResources(List<Resource> resources, List<ResourceType> availableAbilities);

    public abstract void notifyLorenzoCard(LorenzoCard lorenzoCard);

    public abstract void notifyLastRound();

    public abstract void notifyEndGame(List<Player> players);


    public abstract void selectMarket();

    public abstract void selectDevDecks();

    public abstract void selectProductions();

    public abstract void placeResourcesOnShelves(List<Resource> resources);

    public abstract List<RequestResources> chooseStorages(List<Resource> resources);

    public void destroy() {
        messageHandler.stop();
        updateMessageReader.stop();
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        if(numberOfPlayers == 1)
            setLorenzoFaith(0);
    }

    public int getLorenzoFaith() {
        return lorenzoFaith;
    }

    public void setLorenzoFaith(int lorenzoFaith) {
        this.lorenzoFaith = lorenzoFaith;
    }

    public List<PlayerBoardView> getPlayerBoards() {
        return playerBoards;
    }

    public MarketView getMarket() {
        return market;
    }

    public void setMarket(MarketView market) {
        this.market = market;
    }

    public List<DevelopmentDeckView> getDevelopmentDecks() {
        return developmentDecks;
    }

    public void setDevelopmentDecks(List<DevelopmentDeckView> developmentDecks) {
        this.developmentDecks = developmentDecks;
    }

    public FaithTrackView getFaithTrack() {
        return faithTrack;
    }

    public void setFaithTrack(FaithTrackView faithTrack) {
        this.faithTrack = faithTrack;
    }

    public boolean isMainActionPlayed() {
        return mainActionPlayed;
    }

    public void setMainActionPlayed(boolean mainActionPlayed) {
        this.mainActionPlayed = mainActionPlayed;
    }

    public PlayerBoardView playerBoardFromNickname(String nickname) throws NotExistingNicknameException {
        for(PlayerBoardView playerBoard : playerBoards)
            if(playerBoard.getNickname().equals(nickname))
                return playerBoard;
        throw new NotExistingNicknameException();
    }

    public PlayerBoardView getLocalPlayerBoard() throws NotExistingNicknameException {
        return playerBoardFromNickname(nickname);
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public UpdateMessageReader getUpdateMessageReader() {
        return updateMessageReader;
    }
}
