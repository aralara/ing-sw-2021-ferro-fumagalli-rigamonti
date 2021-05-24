package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.UpdateMessageReader;
import it.polimi.ingsw.client.gui.controllers.DecksBoardController;
import it.polimi.ingsw.client.gui.controllers.MarketBoardController;
import it.polimi.ingsw.client.gui.controllers.PlayerBoardController;
import it.polimi.ingsw.client.gui.controllers.SetupController;
import it.polimi.ingsw.server.model.boards.Player;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.model.cards.card.LorenzoCard;
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
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class GUI extends ClientController {

    private final GUIApplication guiApplication;
    private List<LeaderCard> leadersToDiscard; //TODO: va bene qua?
    private List<Resource> resourcesToPlace;

    public GUI(GUIApplication guiApplication) {
        super();
        this.guiApplication = guiApplication;
    }

    @Override
    public void setup() {
        leadersToDiscard = new ArrayList<>();
        resourcesToPlace = new ArrayList<>();
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

    }

    @Override
    public void displaySaves(List<GameSave> saves) {

    }

    @Override
    public void notifyNewPlayer(String nickname) {
        Platform.runLater(() -> ((SetupController)guiApplication.
                getController(SceneNames.MULTI_PLAYER_WAITING)).notifyNewPlayer(nickname));
    }

    @Override
    public void askLeaderDiscard() {
        //TODO:settare parti gioco
        //getPlayerBoards().get(0).get

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
    }

    public void addLeaderToDiscard(int i){ //TODO: va bene così?
        //da fare, quando raggiunge size 2 inviare il messaggio e torna true se ho finito
        leadersToDiscard.add(new LeaderCard()); //temp.. int ritorna [1,4]
        if(leadersToDiscard.size() == 2){
            getMessageHandler().sendClientMessage(new LeaderCardDiscardMessage(leadersToDiscard));
            guiApplication.closePopUpStage();
            guiApplication.setActiveScene(SceneNames.RESOURCE_CHOICE_MENU);
        }
    }

    public void addResourceToPlace(ResourceType resourceType){

    }
}
