package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.server.model.cards.card.DevelopmentCard;
import it.polimi.ingsw.utils.messages.Message;

public class BuyDevelopmentCardMessage implements Message {

    private DevelopmentCard developmentCard;


    public BuyDevelopmentCardMessage(DevelopmentCard developmentCard) {
        this.developmentCard = developmentCard;
    }


    public DevelopmentCard getDevelopmentCard() {
        return developmentCard;
    }
}
