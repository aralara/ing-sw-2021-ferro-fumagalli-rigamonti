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
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;


public class GUI extends ClientController {

    private final GUIApplication guiApplication;
    private List<Resource> resourcesToEqualize;
    private List<Resource> resourcesToPlace;
    private List<Resource> resourcesToDiscard;
    private int waitingPlayers;

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
        updateLeaderHandToDiscard();
        try {
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
            Platform.runLater(() -> (guiApplication.getController(SceneNames.PLAYER_BOARD)).
                    showAlert(Alert.AlertType.INFORMATION, "It's your turn",
                            "Now you can play your actions", ""));
            Platform.runLater(() -> ((PlayerBoardController) guiApplication.
                    getController(SceneNames.PLAYER_BOARD)).setIsPlayerTurn(true));
        }
        else {
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
                    getController(SceneNames.RESOURCE_CHOICE_MENU)).setIsMarbleAction(true));
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

    }

    @Override
    public void notifyLastRound() {

    }

    @Override
    public void notifyEndGame(List<Player> players) {

    }

    @Override
    public void selectMarket() {

    }

    @Override
    public void selectDevDecks() {
        /*Platform.runLater(() -> ((PlayerBoardController)guiApplication.getController(SceneNames.PLAYER_BOARD))
            .showDevelopmentBSpaces());*/
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

    }

    @Override
    public void chooseDevelopmentStorages(DevelopmentCard cardToBuy, int spaceToPlace, List<Resource> cost) {
        Platform.runLater(() -> ((DepotsController)guiApplication.getController(SceneNames.DEPOTS))
                .setDevCardToBuy(cardToBuy));
        Platform.runLater(() ->
                ((DepotsController)guiApplication.getController(SceneNames.DEPOTS))
                        .setSpaceToPlace(spaceToPlace));
        chooseStorages(cost, 1);    //TODO: 1 e 2 possono diventare degli enum
    }

    @Override
    public void chooseProductionStorages(List<Production> productionsToActivate, List<Resource> consumed) {
        Platform.runLater(() -> ((DepotsController)guiApplication.getController(SceneNames.DEPOTS))
                .setProductionsToActivate(productionsToActivate));
        chooseStorages(consumed, 2);    //TODO: 1 e 2 possono diventare degli enum
    }

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

    public void sendNickname(String nickname){
        getMessageHandler().sendClientMessage(new ConnectionMessage(nickname));
        setNickname(nickname);
        ((PlayerBoardController)guiApplication.getController(SceneNames.PLAYER_BOARD)).setPlayer_label(nickname);
    }

    public void setLobbySize(int size){
        setNumberOfPlayers(size);
        getMessageHandler().sendClientMessage(new NewLobbyMessage(size));
        if(size==1)
            Platform.runLater(() -> guiApplication.setActiveScene(SceneNames.LOADING));
        else
            Platform.runLater(() -> guiApplication.setActiveScene(SceneNames.MULTI_PLAYER_WAITING));
    }

    public void updateLeaderHandToDiscard(){
        try {
            List<Integer> listID = new ArrayList<>();
            for(Card card : getLocalPlayerBoard().getLeaderBoard().getHand())
                listID.add(card.getID());
            Platform.runLater(() -> ((FirstPhaseController)guiApplication.getController(SceneNames.LEADER_CHOICE_MENU))
                    .setLeaders(listID));
        } catch (NotExistingNicknameException e) {
            e.printStackTrace();
        }
    }

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

    public void sendLeaderCardDiscardMessage(int index){
        List<Integer> oneElementList = new ArrayList<>();
        oneElementList.add(index);
        sendLeaderCardDiscardMessage(oneElementList);
    }

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
                    getController(SceneNames.RESOURCE_CHOICE_MENU)).setIsMarbleAction(false));
            Platform.runLater(() -> guiApplication.setActiveScene(SceneNames.RESOURCE_CHOICE_MENU));
        }
    }

    public void sendShelvesConfigurationMessage(List<Shelf> shelves, List<Resource> toDiscard){
        resourcesToDiscard.addAll(toDiscard);
        getMessageHandler().sendClientMessage(new ShelvesConfigurationMessage(shelves, resourcesToPlace, resourcesToDiscard));
        resourcesToPlace.clear();
        resourcesToDiscard.clear();
    }

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

    public void sendMarketMessage(int row, int col){
        getMessageHandler().sendClientMessage(new SelectMarketMessage(row, col));
    }

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

    public void updateResourcesToPlace(){
        for(Resource resource : resourcesToPlace){
            Platform.runLater(() -> ((PlayerBoardController)guiApplication.getController(SceneNames.PLAYER_BOARD))
                    .setResToPlaceQuantity(resource.getResourceType(),resource.getQuantity()));
        }
    }

    public void updateWarehouse(){
        Platform.runLater(() ->
                ((PlayerBoardController)guiApplication.getController(SceneNames.PLAYER_BOARD)).showWarehouse());
    }

    public void setResourcesToPlace(List<Resource> resources){
        resourcesToPlace=resources;
    }

    public void sendEndTurnMessage(){
        getMessageHandler().sendClientMessage(new EndTurnMessage());
    }

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

    public void controlResourcesToPlace(List<Resource> resources){
        resourcesToPlace.clear();
        resourcesToPlace.addAll(resources);

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

    private boolean checkOnlyWildcard(List<Resource> resources){
        boolean onlyWildcard = true;
        for(Resource resource : resources)
            if(resource.getResourceType() != ResourceType.WILDCARD && resource.getResourceType() != ResourceType.FAITH) {
                onlyWildcard = false;
                break;
            }
        return onlyWildcard;
    }

    public void sendBuyDevelopmentCardMessage(CardColors devColor, int devLevel, int space){
        getMessageHandler().sendClientMessage(new CanBuyDevelopmentCardMessage(
                getDevelopmentCard(devColor, devLevel), space));
    }

    private DevelopmentCard getDevelopmentCard(CardColors devColor, int devLevel){
        for(int i=0; i<getDevelopmentDecks().size(); i++){
            DevelopmentDeckView developmentDeck = getDevelopmentDecks().getDecks().get(i);
            if(developmentDeck.getDeckColor()==devColor && developmentDeck.getDeckLevel()==devLevel)
                return (DevelopmentCard) developmentDeck.getDeck().get(0);
        }
        return null;
    }

    public List<Deck> getDevelopmentBoard(){
        try {
            return getLocalPlayerBoard().getDevelopmentBoard().getSpaces();
        } catch (NotExistingNicknameException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Production getBasicProduction(){
        try {
            return getLocalPlayerBoard().getBasicProduction();
        } catch (NotExistingNicknameException e) {
            e.printStackTrace();
        }
        return new Production();
    }

    public List<Production> getLeaderProductions(){
        try {
            return getLocalPlayerBoard().getActiveAbilityProductions();
        } catch (NotExistingNicknameException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendCanActivateProductionsMessage(List<Production> productions){
        getMessageHandler().sendClientMessage(new CanActivateProductionsMessage(productions));
    }

    public List<Resource> getStrongboxResources(){
        try {
            return getLocalPlayerBoard().getStrongbox().getResources();
        } catch (NotExistingNicknameException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public int getOpponentIndex(String nickname){
        for(int i=0; i<getOpponents().size(); i++) {
            if (getOpponents().get(i).getNickname().equals(nickname))
                return i;
        }
        return -1;
    }

    public List<PlayerBoardView> getOpponents(){
        List<PlayerBoardView> opponents = new ArrayList<>();
        for(PlayerBoardView pb : getPlayerBoards()){
            if(!pb.getNickname().equals(getNickname()))
                opponents.add(pb);
        }
        return opponents;
    }
}
