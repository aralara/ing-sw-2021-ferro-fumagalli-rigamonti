package it.polimi.ingsw.utils.messages.server.update;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.structures.DevelopmentDeckView;
import it.polimi.ingsw.server.model.cards.deck.DevelopmentDeck;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DevelopmentDecksMessage implements ServerUpdateMessage {

    private final List<DevelopmentDeckView> developmentDecks;


    public DevelopmentDecksMessage(List<DevelopmentDeck> developmentDecks) {
        this.developmentDecks = new ArrayList<>();
        developmentDecks.forEach(d -> this.developmentDecks.add(new DevelopmentDeckView(d)));
    }


    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void doUpdate(ClientController client) {
        List<DevelopmentDeckView> clientDecks = client.getDevelopmentDecks();
        //If this message is sent as an update for a single DevelopmentDeck it will only update the corresponding deck
        if(clientDecks.size() == Constants.DEVELOPMENT_DECK_NUMBER.value() && developmentDecks.size() == 1) {
            DevelopmentDeckView serverDeck = developmentDecks.get(0);
            for (DevelopmentDeckView clientDeck : clientDecks) {
                if (clientDeck.getDeckColor() == serverDeck.getDeckColor() &&
                        clientDeck.getDeckLevel() == serverDeck.getDeckLevel()) {
                    Collections.replaceAll(clientDecks, clientDeck, serverDeck);
                    break;
                }
            }
        }
        else
            client.setDevelopmentDecks(developmentDecks);
    }
}
