package it.polimi.ingsw.utils.messages.server.update;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.structures.DevelopmentDeckView;
import it.polimi.ingsw.client.structures.DevelopmentDecksView;
import it.polimi.ingsw.server.model.cards.deck.DevelopmentDeck;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DevelopmentDecksMessage implements ServerUpdateMessage {

    private final DevelopmentDecksView developmentDecks;


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
