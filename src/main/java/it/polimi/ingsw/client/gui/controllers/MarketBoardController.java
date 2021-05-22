package it.polimi.ingsw.client.gui.controllers;

import javafx.event.ActionEvent;

public class MarketBoardController extends GenericController {
    public void goBack(ActionEvent actionEvent) {
        getGUIApplication().closeSecondStage();
    }

    public void playMarket(ActionEvent actionEvent) {
    }
}
