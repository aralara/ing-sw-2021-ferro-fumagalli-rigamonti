package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.SceneNames;
import javafx.event.ActionEvent;

public class PlayerBoardController extends GenericController {
    public void goToMarket(ActionEvent actionEvent) {
        getGUIApplication().setActiveScene(SceneNames.MARKET_BOARD);
    }

    public void goToDecks(ActionEvent actionEvent) {
        getGUIApplication().setActiveScene(SceneNames.DECKS_BOARD);
    }

    public void activateProductions(ActionEvent actionEvent) {
    }

    public void activateLeaderCard(ActionEvent actionEvent) {
    }

    public void discardLeaderCard(ActionEvent actionEvent) {
    }

    public void rearrangeWarehouse(ActionEvent actionEvent) {
    }

    public void viewOpponents(ActionEvent actionEvent) {
    }
}
