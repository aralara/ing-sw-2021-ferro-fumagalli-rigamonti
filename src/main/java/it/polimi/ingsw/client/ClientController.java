package it.polimi.ingsw.client;

import it.polimi.ingsw.client.structures.DevelopmentDeckView;
import it.polimi.ingsw.client.structures.FaithTrackView;
import it.polimi.ingsw.client.structures.MarketView;
import it.polimi.ingsw.client.structures.PlayerBoardView;
import it.polimi.ingsw.exceptions.NotExistingNicknameException;
import it.polimi.ingsw.server.model.boards.Player;
import it.polimi.ingsw.server.model.cards.card.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.card.LorenzoCard;
import it.polimi.ingsw.server.model.storage.Production;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;

import java.util.ArrayList;
import java.util.List;

public abstract class ClientController {
    private String nickname;
    private int numberOfPlayers;
    private int lorenzoFaith;                   //TODO: gestione lorenzo WIP
    private final List<PlayerBoardView> playerBoards;
    private final MarketView market;
    private final List<DevelopmentDeckView> developmentDecks;
    private final FaithTrackView faithTrack;
    private List<Resource> resourcesToPut;      //TODO: valutare se serve memorizzare
    private DevelopmentCard cardToBuy;
    private int spaceToPlace;
    private List<Production> productionsToActivate;
    private boolean mainActionPlayed, playerTurn;
    private final MessageHandler messageHandler;


    public ClientController() {
        lorenzoFaith = -1;
        playerBoards = new ArrayList<>();
        market = new MarketView();
        developmentDecks = new ArrayList<>();
        faithTrack = new FaithTrackView();
        resourcesToPut = new ArrayList<>();
        productionsToActivate = new ArrayList<>();
        mainActionPlayed = false;
        messageHandler = new MessageHandler();
        playerTurn = false;
    }


    public abstract void setup();

    public abstract void connect();

    public abstract void run();

    public abstract void askNickname();

    public abstract void askNewLobby(int lobbySize, int waitingPlayers);

    public abstract void notifyNewPlayer(String nickname);

    public abstract void askLeaderDiscard();

    public abstract void askResourceEqualize(List<Resource> resources);

    public abstract void notifyStartTurn(String nickname);

    public abstract void addMarketResources(List<Resource> resources, List<ResourceType> availableAbilities);

    public abstract void notifyLorenzoCard(LorenzoCard lorenzoCard);

    public abstract void notifyLastRound();

    public abstract void notifyEndGame(List<Player> players);

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

    public List<DevelopmentDeckView> getDevelopmentDecks() {
        return developmentDecks;
    }

    public FaithTrackView getFaithTrack() {
        return faithTrack;
    }

    public List<Resource> getResourcesToPut() {
        return resourcesToPut;
    }

    public void setResourcesToPut(List<Resource> resourcesToPut) {
        this.resourcesToPut = resourcesToPut;
    }

    public DevelopmentCard getCardToBuy() {
        return cardToBuy;
    }

    public void setCardToBuy(DevelopmentCard cardToBuy) {
        this.cardToBuy = cardToBuy;
    }

    public int getSpaceToPlace() {
        return spaceToPlace;
    }

    public void setSpaceToPlace(int spaceToPlace) {
        this.spaceToPlace = spaceToPlace;
    }

    public List<Production> getProductionsToActivate() {
        return productionsToActivate;
    }

    public void setProductionsToActivate(List<Production> productionsToActivate) {
        this.productionsToActivate = productionsToActivate;
    }

    public boolean isMainActionPlayed() {
        return mainActionPlayed;
    }

    public void setMainActionPlayed(boolean mainActionPlayed) {
        this.mainActionPlayed = mainActionPlayed;
    }

    public MessageHandler getPacketHandler() {
        return messageHandler;
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
}
