package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.cards.card.DevelopmentCard;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;

public class CanBuyDevelopmentCardMessage extends ClientActionMessage {

    private final DevelopmentCard developmentCard;
    private final int space;


    public CanBuyDevelopmentCardMessage(DevelopmentCard developmentCard, int space) {
        this.developmentCard = developmentCard;
        this.space = space;
    }


    public DevelopmentCard getDevelopmentCard() {
        return developmentCard;
    }

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
