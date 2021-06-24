package it.polimi.ingsw.utils.messages.server.update;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.structures.DevelopmentDecksView;
import it.polimi.ingsw.server.model.cards.deck.DevelopmentDeck;

import java.util.List;

/**
 * Server update message for the game's development decks
 */
public class DevelopmentDecksMessage implements ServerUpdateMessage {

    private final DevelopmentDecksView developmentDecks;


    /**
     * Constructor for a DevelopmentDecksMessage given a list of development decks
     * @param developmentDecks development decks contained in the DevelopmentDecksMessage
     */
    public DevelopmentDecksMessage(List<DevelopmentDeck> developmentDecks) {
        this.developmentDecks = new DevelopmentDecksView(developmentDecks);
    }


    @Override
    public void doUpdate(ClientController client) {
        DevelopmentDecksView clientDecks = client.getDevelopmentDecks();
        //If this message is sent as an update for a single DevelopmentDeck it will only update the corresponding deck
        if(developmentDecks.size() == 1)
            clientDecks.updateDeck(developmentDecks.getDecks().get(0));
        else
            client.setDevelopmentDecks(developmentDecks);
    }
}
