package it.polimi.ingsw.utils.messages.server.action;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.cards.card.LorenzoCard;

/**
 * Server message that notifies the player of which card Lorenzo pulled from his deck
 */
public class LorenzoCardMessage implements ServerActionMessage {

    private final LorenzoCard lorenzoCard;


    /**
     * Constructor for a LorenzoCardMessage given a LorenzoCard
     * @param lorenzoCard LorenzoCard picked by Lorenzo
     */
    public LorenzoCardMessage(LorenzoCard lorenzoCard) {
        this.lorenzoCard = lorenzoCard;
    }


    @Override
    public void doAction(ClientController client) {
        client.notifyLorenzoCard(lorenzoCard);
    }
}
