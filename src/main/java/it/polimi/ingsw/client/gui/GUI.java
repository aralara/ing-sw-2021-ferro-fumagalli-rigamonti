package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.gui.controllers.*;
import it.polimi.ingsw.client.structures.DevelopmentDeckView;
import it.polimi.ingsw.client.structures.DevelopmentDecksView;
import it.polimi.ingsw.client.structures.MarketView;
import it.polimi.ingsw.client.structures.PlayerBoardView;
import it.polimi.ingsw.exceptions.NotExistingNicknameException;
import it.polimi.ingsw.server.model.boards.Player;
import it.polimi.ingsw.server.model.cards.card.*;
import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.server.saves.GameSave;
import it.polimi.ingsw.utils.listeners.Listeners;
import it.polimi.ingsw.utils.listeners.client.*;
import it.polimi.ingsw.utils.messages.client.*;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;
import it.polimi.ingsw.utils.messages.server.action.ServerActionMessage;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;


public class GUI extends ClientController {

    private final GUIApplication guiApplication;
    private List<Resource> resourcesToEqualize;
    private List<Resource> resourcesToPlace;
    private List<Resource> resourcesToDiscard;
    private int waitingPlayers;

    /**
     * GUI constructor with parameters
     * @param guiApplication Lower limit for the VaticanReport
     */
    public GUI(GUIApplication guiApplication) {
        super();
        this.guiApplication = guiApplication;
    }

    @Override
    public void setup() {
        resourcesToEqualize = new ArrayList<>();
        resourcesToPlace = new ArrayList<>();
        resourcesToDiscard = new ArrayList<>();
    }

    /**
     * TODO: RIGA fai il javadoc c':
     */
    public void attachListeners() {
        PlayerBoardController playerBoardController = (PlayerBoardController) guiApplication
                .getController(SceneNames.PLAYER_BOARD);
        DecksBoardController developmentDecksController = (DecksBoardController) guiApplication
                .getController(SceneNames.DECKS_BOARD);
        MarketBoardController marketController = (MarketBoardController) guiApplication
                .getController(SceneNames.MARKET_BOARD);
        getPlayerBoards().forEach(pb -> {
                pb.getLeaderBoard().addListener(Listeners.BOARD_LEADER_BOARD.value(),
                        new LeaderBBoardViewListener(playerBoardController));
                pb.getLeaderBoard().addListener(Listeners.BOARD_LEADER_HAND.value(),
                        new LeaderBHandViewListener(playerBoardController));
                pb.getDevelopmentBoard().addListener(Listeners.BOARD_DEV_SPACES.value(),
                        new DevelopmentBSpacesViewListener(playerBoardController));
                pb.getFaithBoard().addListener(Listeners.BOARD_FAITH_FAITH.value(),
                        new FaithBFaithViewListener(playerBoardController));
                pb.getFaithBoard().addListener(Listeners.BOARD_FAITH_POPE.value(),
                        new FaithBPopeChangeViewListener(playerBoardController));
                pb.getWarehouse().addListener(Listeners.BOARD_WAREHOUSE.value(),
                        new WarehouseViewListener(playerBoardController));
                pb.getStrongbox().addListener(Listeners.BOARD_STRONGBOX.value(),
                        new StrongboxViewListener(playerBoardController));
        });
        getDevelopmentDecks().addListener(Listeners.GAME_DEV_DECK.value(),
                new DevelopmentDeckViewListener(developmentDecksController));
        getMarket().addListener(Listeners.GAME_MARKET.value(),
                new MarketViewListener(marketController));
    }

    /**
     * Connects to the server given by parameter
     * @param address Server's address
     * @param port Server's port
     * @return Returns true if it connects successfully, false otherwise
     */
    public boolean connect(String address, Integer port) {
        boolean success = getMessageHandler().connect(address, port);
        if(success)
            new Thread(getMessageHandler()).start();
        return success;
    }

    @Override
    public void run() {
        LinkedBlockingQueue<ServerActionMessage> actionQueue = getMessageHandler().getActionQueue();
        LinkedBlockingQueue<ServerAckMessage> responseQueue = getMessageHandler().getResponseQueue();
        List<ClientMessage> confirmationList = getMessageHandler().getConfirmationList();

        new Thread(getUpdateMessageReader()).start();

        while(true) {
            try {
                if (confirmationList.size() != 0)
                    responseQueue.take().activateResponse(this);
                else if (actionQueue.size() > 0)
                        actionQueue.poll().doAction(this);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void ackNotification(String message, boolean visual) {
        //messaggio ricevuto da ack
        if(visual)
            Platform.runLater(() -> guiApplication.getController(guiApplication.getActiveSceneName()).
                    showAlert(Alert.AlertType.INFORMATION,"Notification","Event notification", message));
    }

    @Override
    public void askNickname() {
        guiApplication.changeNicknameMenuStatus();
    }

    @Override
    public void askNewLobby(int lobbySize, int waitingPlayers) {
        if (lobbySize == waitingPlayers){
            Platform.runLater(() -> guiApplication.setActiveScene(SceneNames.GAME_MODE_MENU));
        }
        else {
            setNumberOfPlayers(lobbySize);
            Platform.runLater(() -> guiApplication.setActiveScene(SceneNames.MULTI_PLAYER_WAITING));
        }
        this.waitingPlayers = waitingPlayers;
    }

    @Override
    public void displaySaves(List<GameSave> saves) {
        //TODO: da fare?
    }

    @Override
    public void notifyNewPlayer(String nickname) {
        Platform.runLater(() -> ((SetupController)guiApplication.
                getController(SceneNames.MULTI_PLAYER_WAITING)).notifyNewPlayer(nickname));

        if(getNumberOfPlayers() == ++this.waitingPlayers){
            Platform.runLater(() -> guiApplication.setActiveScene(SceneNames.LOADING));
        }
    }

    @Override
    public void askLeaderDiscard() {
        attachListeners();  //TODO: TEMPORANEO, non valido quando si carica un gioco dal salvataggio
        try {
            List<Integer> listID = new ArrayList<>();
            for(Card card : getLocalPlayerBoard().getLeaderBoard().getHand())
                listID.add(card.getID());
            Platform.runLater(() -> ((FirstPhaseController)guiApplication.
                    getController(SceneNames.LEADER_CHOICE_MENU)).setLeaders(listID));
            if (getLocalPlayerBoard().isInkwell()) {
                Platform.runLater(() -> ((PlayerBoardController) guiApplication.
                        getController(SceneNames.PLAYER_BOARD)).enableInkwell());
            }
        }catch(NotExistingNicknameException e){
            e.printStackTrace();
        }
        Platform.runLater(() -> ((PlayerBoardController)guiApplication.
                getController(SceneNames.PLAYER_BOARD)).disableActivateProductionsAction());
        Platform.runLater(() -> ((PlayerBoardController) guiApplication.
                getController(SceneNames.PLAYER_BOARD)).disableActivateProductionsAction());
        Platform.runLater(() -> ((MarketBoardController) guiApplication.
                getController(SceneNames.MARKET_BOARD)).disableMarketAction());
        Platform.runLater(() -> ((DecksBoardController) guiApplication.
                getController(SceneNames.DECKS_BOARD)).disableBuyCardAction());
        Platform.runLater(() -> guiApplication.setActiveScene(SceneNames.PLAYER_BOARD));
        Platform.runLater(() -> guiApplication.setActiveScene(SceneNames.LEADER_CHOICE_MENU));
    }

    @Override
    public void askResourceEqualize(List<Resource> resources) {
        resourcesToEqualize.addAll(resources);
        checkFaithResource(resourcesToEqualize);
    }

    @Override
    public void notifyStartTurn(String nickname) {
        if(nickname.equals(getNickname())) {
            if(getLorenzoFaith()==-1)
                Platform.runLater(() -> (guiApplication.getController(SceneNames.PLAYER_BOARD)).
                    showAlert(Alert.AlertType.INFORMATION, "It's your turn",
                            "Now you can play your actions", ""));
            Platform.runLater(() -> ((PlayerBoardController) guiApplication.
                    getController(SceneNames.PLAYER_BOARD)).setIsPlayerTurn(true));
        }
        else {
            if(getLorenzoFaith()==-1)
                Platform.runLater(() -> (guiApplication.getController(SceneNames.PLAYER_BOARD)).
                    showAlert(Alert.AlertType.INFORMATION,"Wait your turn",
                             "Now it's "+nickname+"'s turn", ""));
            Platform.runLater(() -> ((PlayerBoardController) guiApplication.
                    getController(SceneNames.PLAYER_BOARD)).setIsPlayerTurn(false));
        }
    }

    @Override
    public void addMarketResources(List<Resource> resources, List<ResourceType> availableAbilities) {
        int countWildcard = 0;
        for(Resource resource : resources)
            if(resource.getResourceType()==ResourceType.WILDCARD)
                countWildcard += resource.getQuantity();
        if(countWildcard==0)
            controlResourcesToPlace(resources);
        else if(availableAbilities!=null && availableAbilities.size()>1){
            int finalCountWildcard = countWildcard;
            Platform.runLater(() -> ((WildcardResolverController) guiApplication.
                    getController(SceneNames.RESOURCE_CHOICE_MENU)).
                    setChooseResources_label("Choose " + finalCountWildcard +" resources to resolve white marbles"));
            Platform.runLater(() -> ((WildcardResolverController) guiApplication.
                    getController(SceneNames.RESOURCE_CHOICE_MENU)).setTotalResources(finalCountWildcard));
            Platform.runLater(() -> ((WildcardResolverController) guiApplication.
                    getController(SceneNames.RESOURCE_CHOICE_MENU)).setIsFirstPhase(false));
            Platform.runLater(() -> ((WildcardResolverController) guiApplication.
                    getController(SceneNames.RESOURCE_CHOICE_MENU)).setIsMarbleAction(true, resources));
            Platform.runLater(() -> ((WildcardResolverController) guiApplication.
                    getController(SceneNames.RESOURCE_CHOICE_MENU)).enableButtons(availableAbilities));
            Platform.runLater(() -> guiApplication.setActiveScene(SceneNames.RESOURCE_CHOICE_MENU));
        } else {
            if (availableAbilities != null && availableAbilities.size() == 1) {
                for (Resource resource : resources) {
                    if (resource.getResourceType() == ResourceType.WILDCARD) {
                        resource.setResourceType(availableAbilities.get(0));
                    }
                }
            }
            controlResourcesToPlace(resources);
        }
    }

    @Override
    public void notifyLorenzoCard(LorenzoCard lorenzoCard) {
        Platform.runLater(() -> guiApplication.getController(SceneNames.PLAYER_BOARD).showAlert(Alert.AlertType.INFORMATION,
                "It's Lorenzo's turn", "Lorenzo pulls a card from his deck", lorenzoCard.toString()+
                "\nNow you can play your actions"));
    }

    @Override
    public void notifyLastRound() { //TODO: controllare quante volte arriva
        Platform.runLater(() -> guiApplication.getController(SceneNames.PLAYER_BOARD).showAlert(Alert.AlertType.INFORMATION,
                "Notification", "Last round before the game ends!", ""));
    }

    @Override
    public void notifyEndGame(List<Player> players) {
        Platform.runLater(() -> guiApplication.getController(SceneNames.PLAYER_BOARD).showAlert(Alert.AlertType.INFORMATION,
                "Notification", "The game has ended!", ""));
        RankingController rc = (RankingController) guiApplication.getController(SceneNames.RANKING);
        List<Player> playerRanking = players.stream().sorted(Comparator.comparingInt(
                Player::getFinalPosition)).collect(Collectors.toList());
        if(getLorenzoFaith()==-1) {
            for (Player player : playerRanking) {
                rc.setNames_label(player.getNickname());
                rc.setScores_label(getLorenzoFaith() >= 0 && player.getNickname().equals("Lorenzo") ?
                        "-" : Integer.toString(player.getTotalVP()));
            }
        }
        else {
            if(players.get(0).getFinalPosition() == 1) {
                Platform.runLater(() -> rc.setNames_label(playerRanking.get(0).getNickname()
                        + "\nLorenzo il Magnifico"));
                Platform.runLater(() -> rc.setScores_label(Integer.toString(playerRanking.get(0).getTotalVP())));
                Platform.runLater(() -> rc.setScores_label("-"));
            }
            else {
                Platform.runLater(() -> rc.setNames_label("Lorenzo il Magnifico\n"
                        + playerRanking.get(0).getNickname()));
                Platform.runLater(() -> rc.setScores_label("-"));
                Platform.runLater(() -> rc.setScores_label(Integer.toString(playerRanking.get(0).getTotalVP())));
            }
        }
        Platform.runLater(() -> guiApplication.setActiveScene(SceneNames.RANKING));
    }

    @Override
    public void selectMarket() {
        //TODO: da fare?
    }

    @Override
    public void selectDevDecks() {
        Platform.runLater(() -> guiApplication.setActiveScene(SceneNames.DECKS_BOARD));
        Platform.runLater(() -> guiApplication.getController(SceneNames.DECKS_BOARD).showAlert(Alert.AlertType.ERROR,
                "Error", "You can't buy this card and place in the selected space",
                "Please choose another card or another space"));
    }

    @Override
    public void selectProductions() {
        Platform.runLater(() -> guiApplication.getController(SceneNames.PLAYER_BOARD).showAlert(Alert.AlertType.ERROR,
                "Error", "You can't activate the selected productions",
                "Please choose another configuration"));
    }

    @Override
    public void placeResourcesOnShelves(List<Resource> resources) {
        //TODO: da fare?
    }

    @Override
    public void chooseDevelopmentStorages(DevelopmentCard cardToBuy, int spaceToPlace, List<Resource> cost) {
        Platform.runLater(() -> ((DepotsController)guiApplication.getController(SceneNames.DEPOTS))
                .setDevCardToBuy(cardToBuy));
        Platform.runLater(() ->
                ((DepotsController)guiApplication.getController(SceneNames.DEPOTS))
                        .setSpaceToPlace(spaceToPlace));
        Storage.aggregateResources(cost);
        List<Resource> realCost = cost;
        try {
            realCost = Storage.calculateDiscount(cost, getLocalPlayerBoard().getActiveAbilityDiscounts());
        } catch(NotExistingNicknameException e) {
            e.printStackTrace();
        }
        chooseStorages(realCost, 1);    //TODO: 1 e 2 possono diventare degli enum
    }

    @Override
    public void chooseProductionStorages(List<Production> productionsToActivate, List<Resource> consumed) {
        Platform.runLater(() -> ((DepotsController)guiApplication.getController(SceneNames.DEPOTS))
                .setProductionsToActivate(productionsToActivate));
        chooseStorages(consumed, 2);    //TODO: 1 e 2 possono diventare degli enum
    }

    /**
     * Shows Depots scene
     * @param resources Consumed resources' list to resolve
     * @param action 1 to resolve development decks' resources or 2 to resolve productions' resources
     */
    private void chooseStorages(List<Resource> resources, int action) {
        Platform.runLater(() -> ((DepotsController)guiApplication.getController(SceneNames.DEPOTS))
                .setResourcesLabels(resources));
        Platform.runLater(() -> ((DepotsController)guiApplication.getController(SceneNames.DEPOTS))
                .setAction(action));
        Platform.runLater(() -> guiApplication.setActiveScene(SceneNames.DEPOTS));
    }

    @Override
    public void setMarket(MarketView market) {
        super.setMarket(market);
        Platform.runLater(() -> ((MarketBoardController)guiApplication.getController(SceneNames.MARKET_BOARD))
                .showMarket(market));
    }

    @Override
    public void setDevelopmentDecks(DevelopmentDecksView developmentDecks) {
        super.setDevelopmentDecks(developmentDecks);
        Platform.runLater(() -> ((DecksBoardController)guiApplication.getController(SceneNames.DECKS_BOARD))
                .showDevelopmentDeck());
    }

    @Override
    public void setMainActionPlayed(boolean mainActionPlayed) {
        super.setMainActionPlayed(mainActionPlayed);
        ((PlayerBoardController)guiApplication.getController(SceneNames.PLAYER_BOARD)).setMainActionPlayed(mainActionPlayed);
    }

    /**
     * Sends to the server the nickname given by parameter
     * @param nickname Nickname to send
     */
    public void sendNickname(String nickname){
        getMessageHandler().sendClientMessage(new ConnectionMessage(nickname));
        setNickname(nickname);
        ((PlayerBoardController)guiApplication.getController(SceneNames.PLAYER_BOARD)).setPlayer_label(nickname);
    }

    /**
     * Sets the size of the lobby and show a new scene accordingly
     * @param size Size of the lobby
     */
    public void setLobbySize(int size){
        setNumberOfPlayers(size);
        getMessageHandler().sendClientMessage(new NewLobbyMessage(size));
        if(size==1)
            Platform.runLater(() -> guiApplication.setActiveScene(SceneNames.LOADING));
        else
            Platform.runLater(() -> guiApplication.setActiveScene(SceneNames.MULTI_PLAYER_WAITING));
    }

    /**
     * Sends to the server a list of leaders to discard
     * @param indexes List of leaders' IDs to discard
     */
    public void sendLeaderCardDiscardMessage(List<Integer> indexes){
        try {
            List<LeaderCard> leadersToDiscard = new ArrayList<>();
            for(int index : indexes)
                leadersToDiscard.add((LeaderCard) getLocalPlayerBoard().getLeaderBoard().getHand().get(index));
            getMessageHandler().sendClientMessage(new LeaderCardDiscardMessage(leadersToDiscard, true));
            guiApplication.closePopUpStage();
            callAskResourceToEqualize();
        } catch (NotExistingNicknameException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets and shows the popUp stage to equalize initial resources
     */
    private void callAskResourceToEqualize(){
        int size = 0;
        for(Resource resource : resourcesToEqualize)
            if(resource.getResourceType() != ResourceType.FAITH)
                size += resource.getQuantity();
        String title;
        if(size > 0) {
            if(size > 1) {
                title = "Choose two resources to take";
            }
            else{
                title = "Choose one resource to take";
            }
            int finalSize = size;
            Platform.runLater(() -> ((WildcardResolverController) guiApplication.
                    getController(SceneNames.RESOURCE_CHOICE_MENU)).setTotalResources(finalSize));
            Platform.runLater(() -> ((WildcardResolverController) guiApplication.
                    getController(SceneNames.RESOURCE_CHOICE_MENU)).setChooseResources_label(title));
            Platform.runLater(() -> ((WildcardResolverController) guiApplication.
                    getController(SceneNames.RESOURCE_CHOICE_MENU)).setIsFirstPhase(true));
            Platform.runLater(() -> ((WildcardResolverController) guiApplication.
                    getController(SceneNames.RESOURCE_CHOICE_MENU)).setIsMarbleAction(false, null));
            Platform.runLater(() -> guiApplication.setActiveScene(SceneNames.RESOURCE_CHOICE_MENU));
        }
    }

    /**
     * Sends to the server a shelves' configuration to be validated
     * @param shelves List of shelves to be validated
     * @param toDiscard Extra resources to discard
     */
    public void sendShelvesConfigurationMessage(List<Shelf> shelves, List<Resource> toDiscard){
        resourcesToDiscard.addAll(toDiscard);
        getMessageHandler().sendClientMessage(new ShelvesConfigurationMessage(shelves, resourcesToPlace, resourcesToDiscard));
        resourcesToPlace.clear();
        resourcesToDiscard.clear();
    }

    /**
     * Controls and adds faith to discarded resources' list if present
     * @param resources
     * @return
     */
    private boolean checkFaithResource(List<Resource> resources){
        int faithQuantity=0;
        for(Resource resource : resources){
            if(resource.getResourceType() == ResourceType.FAITH)
                faithQuantity++;
        }
        if (faithQuantity>0) {
            resourcesToDiscard.add(new Resource(ResourceType.FAITH, faithQuantity));
            return true;
        }
        return false;
    }

    /**
     * Sends to the server a message with the selected row or column of the market
     * @param row Selected row of the market, -1 if the column is selected
     * @param col Selected column of the market, -1 if the row is selected
     */
    public void sendMarketMessage(int row, int col){
        getMessageHandler().sendClientMessage(new SelectMarketMessage(row, col));
    }

    /**
     * Gets a copy of the local playerboard's warehouse
     * @return Returns the copy of the warehouse
     */
    public List<Shelf> getWarehouseShelvesCopy(){
        List<Shelf> shelvesCopy = new ArrayList<>();
        try {
            for(Shelf shelf : getLocalPlayerBoard().getWarehouse().getShelves())
                shelvesCopy.add(new Shelf(shelf.getResourceType(), new Resource(shelf.getResources().getResourceType(),
                        shelf.getResources().getQuantity()), shelf.getLevel(), shelf.isLeader()));
        } catch (NotExistingNicknameException e) {
            e.printStackTrace();
        }
        return shelvesCopy;
    }

    /**
     * Updates graphical components of the resources to place
     */
    public void updateResourcesToPlace(){
        Storage.aggregateResources(resourcesToPlace);
        for(Resource resource : resourcesToPlace){
            Platform.runLater(() -> ((PlayerBoardController)guiApplication.getController(SceneNames.PLAYER_BOARD))
                    .setResToPlaceQuantity(resource.getResourceType(),resource.getQuantity()));
        }
    }

    /**
     * Updates graphical components of the warehouse
     */
    public void updateWarehouse(){
        Platform.runLater(() ->
                ((PlayerBoardController)guiApplication.getController(SceneNames.PLAYER_BOARD)).showWarehouse());
    }

    /**
     * Stores resources to place in case setting them fails and they need to be placed again
     * @param resources Resources to be stored
     */
    public void setResourcesToPlace(List<Resource> resources){
        resourcesToPlace=resources;
    }

    /**
     * Sends to the server a message to notify the end of the turn
     */
    public void sendEndTurnMessage(){
        getMessageHandler().sendClientMessage(new EndTurnMessage());
    }

    /**
     * Sends to the server a message to active or discard a list of leader
     * @param positions List of positions of the leaders in the hand
     * @param toActivate True to activate leaders, false otherwise
     */
    public void sendLeaderMessage(List<Integer> positions, boolean toActivate){
        List<LeaderCard> leaderHand = new ArrayList<>(), leaderCards = new ArrayList<>();
        try {
            for(Card card : getLocalPlayerBoard().getLeaderBoard().getHand())
                leaderHand.add((LeaderCard)card);
        } catch (NotExistingNicknameException e) {
            e.printStackTrace();
        }
        if (positions.size() == 1 && leaderHand.size()>1)
            leaderCards.add(leaderHand.get(positions.get(0)));
        else if (positions.size() == 1 && leaderHand.size()==1)
            leaderCards.add(leaderHand.get(0));
        else
            leaderCards.addAll(leaderHand);
        if(toActivate)
            getMessageHandler().sendClientMessage(new LeaderCardPlayMessage(leaderCards));
        else
            getMessageHandler().sendClientMessage(new LeaderCardDiscardMessage(leaderCards));
    }

    /**
     * Controls which type of resources are to place and calls methods accordingly
     * If there are resources, updates resources to place and shows them on the playerBoard
     * If there is only faith, adds it to the discarded resources and send a message to the server
     * @param resources
     */
    public void controlResourcesToPlace(List<Resource> resources){
        resourcesToPlace.clear();
        resourcesToPlace.addAll(resources);
        Storage.aggregateResources(resourcesToPlace);

        boolean faith = checkFaithResource(resourcesToPlace);
        if(!checkOnlyWildcard(resourcesToPlace)) {
            Platform.runLater(() -> (guiApplication.getController(SceneNames.PLAYER_BOARD))
                    .showAlert(Alert.AlertType.INFORMATION, "Success!", "Resources taken",
                            "Now you need to place each taken resource"));
            updateResourcesToPlace();
        }
        else if(faith) {
            Platform.runLater(() -> (guiApplication.getController(SceneNames.PLAYER_BOARD))
                    .showAlert(Alert.AlertType.INFORMATION, "Success!", "Resources taken",
                            "Your faith has been updated"));
            Platform.runLater(() -> ((PlayerBoardController)guiApplication.getController(SceneNames.PLAYER_BOARD))
                    .enableButtons());
            try {
                sendShelvesConfigurationMessage(getLocalPlayerBoard().getWarehouse().getShelves(),new ArrayList<>());
            } catch (NotExistingNicknameException e) {
                e.printStackTrace();
            }
        }
        else {
            Platform.runLater(() -> (guiApplication.getController(SceneNames.PLAYER_BOARD))
                    .showAlert(Alert.AlertType.INFORMATION, "Success!", "Resources taken",
                            "You've nothing to place"));
            Platform.runLater(() -> ((PlayerBoardController)guiApplication.getController(SceneNames.PLAYER_BOARD))
                    .enableButtons());
        }
    }

    /**
     * Controls if there are resources to place
     * @param resources Resources to place
     * @return Returns true if there are only wildcards or faith, false otherwise
     */
    private boolean checkOnlyWildcard(List<Resource> resources){
        for(Resource resource : resources)
            if(resource.getResourceType() != ResourceType.WILDCARD && resource.getResourceType() != ResourceType.FAITH) {
                return false;
            }
        return true;
    }

    /**
     * Sends to the server a message with the development card that would be bought
     * @param devColor Color of the development card
     * @param devLevel Level of the development card
     * @param space Space where place the card
     */
    public void sendCanBuyDevelopmentCardMessage(CardColors devColor, int devLevel, int space){
        getMessageHandler().sendClientMessage(new CanBuyDevelopmentCardMessage(
                getDevelopmentCard(devColor, devLevel), space));
    }

    /**
     * Gets development card given by parameter
     * @param devColor Color of the development card
     * @param devLevel Level of the development card
     * @return Returns the development card at the top of the selected deck
     */
    private DevelopmentCard getDevelopmentCard(CardColors devColor, int devLevel){
        for(int i=0; i<getDevelopmentDecks().size(); i++){
            DevelopmentDeckView developmentDeck = getDevelopmentDecks().getDecks().get(i);
            if(developmentDeck.getDeckColor()==devColor && developmentDeck.getDeckLevel()==devLevel)
                return (DevelopmentCard) developmentDeck.getDeck().get(0);
        }
        return null;
    }

    /**
     * Gets the developmentBoard of the local player
     * @return Returns the developmentBoard
     */
    public List<Deck> getDevelopmentBoard(){
        try {
            return getLocalPlayerBoard().getDevelopmentBoard().getSpaces();
        } catch (NotExistingNicknameException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Gets the basic production
     * @return Returns the basic production
     */
    public Production getBasicProduction(){
        try {
            return getLocalPlayerBoard().getBasicProduction();
        } catch (NotExistingNicknameException e) {
            e.printStackTrace();
        }
        return new Production();
    }

    /**
     * Gets the leaders' productions
     * @return Returns the leaders' productions
     */
    public List<Production> getLeaderProductions(){
        try {
            return getLocalPlayerBoard().getActiveAbilityProductions();
        } catch (NotExistingNicknameException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the leaderBoard of the local player
     * @return Returns the leaderBoard
     */
    public Deck getLeaderBoard(){
        try {
            return getLocalPlayerBoard().getLeaderBoard().getBoard();
        } catch (NotExistingNicknameException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sends to the server a message with the productions that would be activated
     * @param productions List of productions to be activated
     */
    public void sendCanActivateProductionsMessage(List<Production> productions){
        getMessageHandler().sendClientMessage(new CanActivateProductionsMessage(productions));
    }

    /**
     * Gets the resources in the strongbox of the local player
     * @return Returns the resources in the strongbox
     */
    public List<Resource> getStrongboxResources(){
        try {
            return getLocalPlayerBoard().getStrongbox().getResources();
        } catch (NotExistingNicknameException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Gets the index of the opposing player given by parameter
     * @param nickname Opposing player's nickname
     * @return Return the index of the opposing player
     */
    public int getOpponentIndex(String nickname){
        for(int i=0; i<getOpponents().size(); i++) {
            if (getOpponents().get(i).getNickname().equals(nickname))
                return i;
        }
        return -1;
    }

    /**
     * Gets the list of opposing playerBoards
     * @return Return the list of playerBoards
     */
    public List<PlayerBoardView> getOpponents(){
        List<PlayerBoardView> opponents = new ArrayList<>();
        for(PlayerBoardView pb : getPlayerBoards()){
            if(!pb.getNickname().equals(getNickname()))
                opponents.add(pb);
        }
        return opponents;
    }
}