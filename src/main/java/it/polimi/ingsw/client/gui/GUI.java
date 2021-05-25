package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.UpdateMessageReader;
import it.polimi.ingsw.client.gui.controllers.*;
import it.polimi.ingsw.client.structures.DevelopmentDeckView;
import it.polimi.ingsw.exceptions.NotExistingNicknameException;
import it.polimi.ingsw.server.model.boards.Player;
import it.polimi.ingsw.server.model.cards.card.Card;
import it.polimi.ingsw.server.model.cards.card.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.model.cards.card.LorenzoCard;
import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.server.model.market.Marble;
import it.polimi.ingsw.server.model.market.MarbleColors;
import it.polimi.ingsw.server.model.storage.RequestResources;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import it.polimi.ingsw.server.saves.GameSave;
import it.polimi.ingsw.utils.messages.client.ClientMessage;
import it.polimi.ingsw.utils.messages.client.ConnectionMessage;
import it.polimi.ingsw.utils.messages.client.LeaderCardDiscardMessage;
import it.polimi.ingsw.utils.messages.client.NewLobbyMessage;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;
import it.polimi.ingsw.utils.messages.server.action.ServerActionMessage;
import it.polimi.ingsw.server.model.cards.card.Card;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import static it.polimi.ingsw.utils.Constants.MARKET_COLUMN_SIZE;
import static it.polimi.ingsw.utils.Constants.MARKET_ROW_SIZE;

public class GUI extends ClientController {

    private final GUIApplication guiApplication;
    private List<LeaderCard> leadersToDiscard; //TODO: va bene qua?
    private List<Resource> resourcesToPlace, resourcesToDiscard, resourcesToEqualize;
    private int waitingPlayers;

    public GUI(GUIApplication guiApplication) {
        super();
        this.guiApplication = guiApplication;
    }

    @Override
    public void setup() {
        leadersToDiscard = new ArrayList<>();
        resourcesToPlace = new ArrayList<>();
        resourcesToDiscard = new ArrayList<>();
        resourcesToEqualize = new ArrayList<>();
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

        new Thread(new UpdateMessageReader(this, getMessageHandler().getUpdateQueue())).start();

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
    public void ackNotification(String message) {
        //messaggio ricevuto da ack
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
        //TODO:settare parti gioco
        updateMarket();
        updateDevDecks();
        updateLeaderHandToDiscard();
        try {
            if (getLocalPlayerBoard().isInkwell()) {
                Platform.runLater(() -> ((PlayerBoardController) guiApplication.
                        getController(SceneNames.PLAYER_BOARD)).enableInkwell());
            }
        }catch(NotExistingNicknameException e){
            e.printStackTrace();
        }
        //altro?

        //???
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

    }

    @Override
    public void notifyStartTurn(String nickname) {

    }

    @Override
    public void addMarketResources(List<Resource> resources, List<ResourceType> availableAbilities) {

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

    }

    @Override
    public void selectProductions() {

    }

    @Override
    public void placeResourcesOnShelves(List<Resource> resources) {

    }

    @Override
    public List<RequestResources> chooseStorages(List<Resource> resources) {
        return null;
    }

    public void sendNickname(String nickname){
        getMessageHandler().sendClientMessage(new ConnectionMessage(nickname));
        setNickname(nickname);
        ((PlayerBoardController)guiApplication.getController(SceneNames.PLAYER_BOARD)).setPlayer_label(nickname);
    }

    public void setLobbySize(int size){
        getMessageHandler().sendClientMessage(new NewLobbyMessage(size));
        if(size==1)
            guiApplication.setActiveScene(SceneNames.LOADING);
    }

    public void updateMarket(){
        List<MarbleColors> marbleColors = new ArrayList<>();
        for(int row = 0; row < MARKET_ROW_SIZE.value(); row++)
            for(int col = 0; col < MARKET_COLUMN_SIZE.value(); col++)
                marbleColors.add(getMarket().getMarbleMatrix()[row][col].getColor());
        Platform.runLater(() -> ((MarketBoardController)guiApplication.getController(SceneNames.MARKET_BOARD))
                .updateMarket(marbleColors, getMarket().getFloatingMarble().getColor()));
    }

    public void updateDevDecks(){
        List<Integer> listID = new ArrayList<>();
        for(DevelopmentDeckView deck : getDevelopmentDecks())
            listID.add(deck.getDeck().get(0).getID());
        Platform.runLater(() -> ((DecksBoardController)guiApplication.getController(SceneNames.DECKS_BOARD))
                .updateDevDecks(listID));
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

    public void addLeaderToDiscard(int index){
        try {
            leadersToDiscard.add((LeaderCard) getLocalPlayerBoard().getLeaderBoard().getHand().get(index-1));
            if(leadersToDiscard.size() == 2){
                getMessageHandler().sendClientMessage(new LeaderCardDiscardMessage(leadersToDiscard));
                guiApplication.closePopUpStage();
                callAskResourceToEqualize();
            }
        } catch (NotExistingNicknameException e) {
            e.printStackTrace();
        }
    }

    private void callAskResourceToEqualize(){
        int size = 0;
        for(Resource resource : resourcesToEqualize){
            if(resource.getResourceType() == ResourceType.FAITH){
                addResourceToDiscard(resource);
                resourcesToEqualize.remove(resource);
                break;
            }
            else
                size += resource.getQuantity();
        }
        String title;
        if(size > 0) {
            if(size > 1) {
                title = "Choose two cards to take";
                Platform.runLater(() -> ((FirstPhaseController) guiApplication.getController(SceneNames.RESOURCE_CHOICE_MENU)).
                        enableLabels());
            }
            else{
                title = "Choose one card to take";
            }
            Platform.runLater(() -> ((FirstPhaseController) guiApplication.getController(SceneNames.RESOURCE_CHOICE_MENU)).
                    setChooseResources_label(title));
            Platform.runLater(() -> guiApplication.setActiveScene(SceneNames.RESOURCE_CHOICE_MENU));
        }
    }

    public void addResourceToDiscard(Resource toDiscard){
        this.resourcesToDiscard.add(toDiscard);
    }

    public void addResourceToPlace(ResourceType resourceType){

    }
}
