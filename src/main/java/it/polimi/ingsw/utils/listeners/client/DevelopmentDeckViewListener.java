package it.polimi.ingsw.utils.listeners.client;

import it.polimi.ingsw.client.gui.controllers.DecksBoardController;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class DevelopmentDeckViewListener implements PropertyChangeListener {

    private final DecksBoardController decksBoardController;

    public DevelopmentDeckViewListener(DecksBoardController decksBoardController) {
        this.decksBoardController = decksBoardController;
    }

    @SuppressWarnings("unchecked")
    public void propertyChange(PropertyChangeEvent evt) {
        decksBoardController.setDevelopmentDeck((List<Integer>) evt.getNewValue());
    }
}
