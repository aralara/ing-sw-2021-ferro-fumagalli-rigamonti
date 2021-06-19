package it.polimi.ingsw.client.structures;

import it.polimi.ingsw.server.model.cards.deck.DevelopmentDeck;
import it.polimi.ingsw.utils.listeners.Listened;
import it.polimi.ingsw.utils.listeners.Listeners;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Handles developmentDecks client methods
 */
public class DevelopmentDecksView extends Listened implements Serializable {

    private final List<DevelopmentDeckView> decks;

    /**
     * DevelopmentBoardView constructor with parameters
     * @param decks Decks to set
     */
    public DevelopmentDecksView(List<DevelopmentDeck> decks) {
        this.decks = new ArrayList<>();
        decks.forEach(d -> this.decks.add(new DevelopmentDeckView(d)));
    }


    /**
     * Replaces a deck matching its color and its level
     * @param deck Deck that will replace the one contained in the list
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void updateDeck(DevelopmentDeckView deck) {
        for (DevelopmentDeckView clientDeck : decks) {
            if (clientDeck.getDeckColor() == deck.getDeckColor() &&
                    clientDeck.getDeckLevel() == deck.getDeckLevel()) {
                Collections.replaceAll(decks, clientDeck, deck);
                break;
            }
        }
        fireUpdate(Listeners.GAME_DEV_DECK.value(), decks);
    }

    /**
     * Gets the size of the decks list
     * @return Returns an int containing the size
     */
    public int size() {
        return decks.size();
    }

    /**
     * Gets the decks attribute
     * @return Returns decks
     */
    public List<DevelopmentDeckView> getDecks() {
        return decks;
    }
}
