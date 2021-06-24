package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.cards.card.DevelopmentCard;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;

/**
 * Client message that checks if it is possible to buy a development card
 */
public class CanBuyDevelopmentCardMessage extends ClientActionMessage {

    private final DevelopmentCard developmentCard;
    private final int space;


    /**
     * Constructor for the message from a development card and its space
     * @param developmentCard Development card to buy
     * @param space Position of the card
     */
    public CanBuyDevelopmentCardMessage(DevelopmentCard developmentCard, int space) {
        this.developmentCard = developmentCard;
        this.space = space;
    }


    /**
     * Gets the developmentCard attribute
     * @return Returns the value of developmentCard
     */
    public DevelopmentCard getDevelopmentCard() {
        return developmentCard;
    }

    /**
     * Gets the space attribute
     * @return Returns the value of space
     */
    public int getSpace() {
        return space;
    }

    @Override
    public void doAction(VirtualView view) {
        boolean success = view.getGameHandler().getController()
                .canBuyDevCard(view.getNickname(), developmentCard, space);
        view.sendMessage(new ServerAckMessage(getUuid(), success));
    }

    @Override
    public void doACKResponseAction(ClientController client) {
        client.setMainActionPlayed(true);
        client.chooseDevelopmentStorages(developmentCard, space, developmentCard.getCost());
    }

    @Override
    public void doNACKResponseAction(ClientController client) {
        client.setMainActionPlayed(false);
        client.selectDevDecks();
    }
}
