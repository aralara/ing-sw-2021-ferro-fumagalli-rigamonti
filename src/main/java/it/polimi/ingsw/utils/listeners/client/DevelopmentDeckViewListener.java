package it.polimi.ingsw.utils.listeners.client;

import it.polimi.ingsw.client.gui.controllers.DecksBoardController;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class DevelopmentDeckViewListener implements PropertyChangeListener {

    private final DecksBoardController decksBoardController;

    public DevelopmentDeckViewListener(DecksBoardController decksBoardController) {
        this.decksBoardController = decksBoardController;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        decksBoardController.showDevelopmentDeck();
    }
}
