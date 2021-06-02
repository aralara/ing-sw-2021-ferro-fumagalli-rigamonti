package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.cards.card.DevelopmentCard;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;

import java.util.ArrayList;
import java.util.List;

public class CanBuyDevelopmentCardMessage extends ClientActionMessage {

    private final DevelopmentCard developmentCard;
    private final int space;
    private List<Resource> cost = new ArrayList<>();


    public CanBuyDevelopmentCardMessage(DevelopmentCard developmentCard, int space) {
        this.developmentCard = developmentCard;
        this.space = space;
        for(Resource resource : developmentCard.getCost()){
            this.cost.add(resource.makeClone());
        }
    }


    public DevelopmentCard getDevelopmentCard() {
        return developmentCard;
    }

    public int getSpace() {
        return space;
    }

    public List<Resource> getCost() {
        return this.cost;
    }

    @Override
    public void doAction(VirtualView view) {
        boolean success = view.getGameHandler().getController()
                .canBuyDevCard(view.getNickname(), developmentCard, space);
        view.getGameHandler().getController().applyDiscount(view.getNickname(),getCost());
        view.sendMessage(new ServerAckMessage(getUuid(), success));
    }

    @Override
    public void doACKResponseAction(ClientController client) {
        client.setMainActionPlayed(true);
        client.chooseDevelopmentStorages(developmentCard, space, this.cost);
    }

    @Override
    public void doNACKResponseAction(ClientController client) {
        client.setMainActionPlayed(false);
        client.selectDevDecks();
    }
}
