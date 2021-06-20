package it.polimi.ingsw.utils.listeners.client;

import it.polimi.ingsw.client.gui.controllers.DecksBoardController;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Listener for the development decks spaces in a DevelopmentDecksView
 */
public class DevelopmentDeckViewListener implements PropertyChangeListener {

    private final DecksBoardController decksBoardController;

    /**
     * Constructor for the listener
     * @param decksBoardController Associated GUI Controller
     */
    public DevelopmentDeckViewListener(DecksBoardController decksBoardController) {
        this.decksBoardController = decksBoardController;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        decksBoardController.showDevelopmentDeck();
    }
}
