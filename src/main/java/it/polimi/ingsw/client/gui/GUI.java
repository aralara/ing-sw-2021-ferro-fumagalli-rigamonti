package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.UpdateMessageReader;
import it.polimi.ingsw.client.gui.controllers.SetupController;
import it.polimi.ingsw.server.model.boards.Player;
import it.polimi.ingsw.server.model.cards.card.LorenzoCard;
import it.polimi.ingsw.server.model.storage.RequestResources;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import it.polimi.ingsw.server.saves.GameSave;
import it.polimi.ingsw.utils.messages.client.ClientMessage;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;
import it.polimi.ingsw.utils.messages.server.action.ServerActionMessage;
import javafx.application.Platform;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class GUI extends ClientController {

    private final GUIApplication guiApplication;

    public GUI(GUIApplication guiApplication) {
        super();
        this.guiApplication = guiApplication;
    }

    @Override
    public void setup() {

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

    }

    @Override
    public void askNickname() {

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


}
