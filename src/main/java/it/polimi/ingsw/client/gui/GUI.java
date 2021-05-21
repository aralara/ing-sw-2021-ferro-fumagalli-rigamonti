package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.boards.Player;
import it.polimi.ingsw.server.model.cards.card.LorenzoCard;
import it.polimi.ingsw.server.model.storage.RequestResources;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;

import java.util.List;

public class GUI extends ClientController {

    //private GraphicalGUI graphicalGUI;

    public GUI() {
        super();
    }

    @Override
    public void setup() {
        GraphicalGUI.setup(this);
    }

    public boolean connect(String address, Integer port) {
        boolean success = getMessageHandler().connect(address, port);
        if(success)
            new Thread(getMessageHandler()).start();
        return success;
    }

    @Override
    public void run() {
        GraphicalGUI.main(null);
    }

    @Override
    public void ackNotification(String message) {

    }

    @Override
    public void askNickname() {

    }

    @Override
    public void askNewLobby(int lobbySize, int waitingPlayers) {

    }

    @Override
    public void notifyNewPlayer(String nickname) {

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
