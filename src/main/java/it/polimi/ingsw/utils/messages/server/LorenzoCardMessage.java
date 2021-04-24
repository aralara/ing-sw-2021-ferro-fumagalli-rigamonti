package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.server.model.cards.card.LorenzoCard;
import it.polimi.ingsw.utils.messages.Message;

public class LorenzoCardMessage implements Message {

    private LorenzoCard lorenzoCard;


    public LorenzoCardMessage(LorenzoCard lorenzoCard) {
        this.lorenzoCard = lorenzoCard;
    }


    public LorenzoCard getLorenzoCard() {
        return lorenzoCard;
    }
}
