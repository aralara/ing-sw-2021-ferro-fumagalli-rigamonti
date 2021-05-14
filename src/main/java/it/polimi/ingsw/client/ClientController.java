package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.GraphicalCLI;
import it.polimi.ingsw.client.structures.DevelopmentDeckView;
import it.polimi.ingsw.client.structures.FaithTrackView;
import it.polimi.ingsw.client.structures.MarketView;
import it.polimi.ingsw.client.structures.PlayerBoardView;
import it.polimi.ingsw.exceptions.NotExistingNickname;
import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.cards.card.DevelopmentCard;
import it.polimi.ingsw.server.model.storage.Production;
import it.polimi.ingsw.server.model.storage.Resource;

import java.util.ArrayList;
import java.util.List;

public abstract class ClientController {
    private String nickname;
    private int numberOfPlayers;
    private int lorenzoFaith;                   //TODO: gestione lorenzo WIP
    private final List<PlayerBoardView> playerBoards;
    private final MarketView marketView;
    private final List<DevelopmentDeckView> developmentDecks;
    private final FaithTrackView faithTrackView;
    private List<Resource> resourcesToPut;      //TODO: valutare se serve memorizzare
    private DevelopmentCard cardToBuy;
    private int spaceToPlace;
    private List<Production> productionsToActivate;
    private boolean mainActionPlayed/*, endTurn*/; //TODO: valutare se serve endTurn
    private final MessageHandler messageHandler;

    public ClientController() {
        lorenzoFaith = -1;
        playerBoards = new ArrayList<>();
        marketView = new MarketView();
        developmentDecks = new ArrayList<>();
        faithTrackView = new FaithTrackView();
        mainActionPlayed = false;
        messageHandler = new MessageHandler(this);
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

    public MarketView getMarketView() {
        return marketView;
    }

    public List<DevelopmentDeckView> getDevelopmentDecks() {
        return developmentDecks;
    }

    public FaithTrackView getFaithTrackView() {
        return faithTrackView;
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

    public PlayerBoardView playerBoardFromNickname(String nickname) throws NotExistingNickname {
        for(PlayerBoardView playerBoard : playerBoards)
            if(playerBoard.getNickname().equals(nickname))
                return playerBoard;
        throw new NotExistingNickname();
    }

    public PlayerBoardView getLocalPlayerBoard() throws NotExistingNickname{
        return playerBoardFromNickname(nickname);
    }

}
