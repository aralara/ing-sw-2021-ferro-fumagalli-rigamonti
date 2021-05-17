package it.polimi.ingsw.utils.messages.server.update;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.structures.DevelopmentDeckView;
import it.polimi.ingsw.server.model.cards.deck.DevelopmentDeck;
import it.polimi.ingsw.utils.Constants;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

public class DevelopmentDecksMessage implements ServerUpdateMessage {

    private final List<DevelopmentDeck> developmentDecks;


    public DevelopmentDecksMessage(List<DevelopmentDeck> developmentDecks) {
        this.developmentDecks = developmentDecks;
    }


    public List<DevelopmentDeck> getDevelopmentDecks() {
        return developmentDecks;
    }

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void doUpdate(ClientController client) {
        List<DevelopmentDeckView> clientDevelopmentDecks = client.getDevelopmentDecks();
        if(clientDevelopmentDecks.size() == Constants.DEVELOPMENT_DECK_NUMBER.value() && developmentDecks.size() == 1) {
            DevelopmentDeckView serverDevelopmentDeck = new DevelopmentDeckView(
                    developmentDecks.get(0).getDeck(),
                    developmentDecks.get(0).getDeckColor(),
                    developmentDecks.get(0).getDeckLevel()
            );
            for (DevelopmentDeckView clientDevelopmentDeck : clientDevelopmentDecks) {
                if (clientDevelopmentDeck.getDeckColor() == serverDevelopmentDeck.getDeckColor() &&
                        clientDevelopmentDeck.getDeckLevel() == serverDevelopmentDeck.getDeckLevel()) {
                    Collections.replaceAll(clientDevelopmentDecks, clientDevelopmentDeck, serverDevelopmentDeck);
                    break;
                }
            }
        }
        else{
            for (DevelopmentDeck developmentDeck : developmentDecks)
                clientDevelopmentDecks.add(new DevelopmentDeckView(developmentDeck.getDeck(),
                        developmentDeck.getDeckColor(), developmentDeck.getDeckLevel()));
        }
    }
}
