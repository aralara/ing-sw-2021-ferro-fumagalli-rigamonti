package it.polimi.ingsw.utils.messages.server.action;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.cards.card.LorenzoCard;
import it.polimi.ingsw.utils.messages.Message;

public class LorenzoCardMessage implements ServerActionMessage {

    private final LorenzoCard lorenzoCard;


    public LorenzoCardMessage(LorenzoCard lorenzoCard) {
        this.lorenzoCard = lorenzoCard;
    }


    public LorenzoCard getLorenzoCard() {
        return lorenzoCard;
    }

    @Override
    public void doAction(ClientController client) {
        client.notifyLorenzoCard(lorenzoCard);
    }
}
