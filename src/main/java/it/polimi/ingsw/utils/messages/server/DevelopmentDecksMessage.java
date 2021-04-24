package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.server.model.cards.deck.DevelopmentDeck;
import it.polimi.ingsw.utils.messages.Message;

import java.util.List;

public class DevelopmentDecksMessage implements Message {

    private List<DevelopmentDeck> developmentDecks;


    public DevelopmentDecksMessage(List<DevelopmentDeck> developmentDecks) {
        this.developmentDecks = developmentDecks;
    }


    public List<DevelopmentDeck> getDevelopmentDecks() {
        return developmentDecks;
    }
}
