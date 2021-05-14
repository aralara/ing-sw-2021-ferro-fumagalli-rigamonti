package it.polimi.ingsw.utils.messages.server.ack;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.messages.LeaderCardAckMessage;
import it.polimi.ingsw.utils.messages.server.ServerActionMessage;

public class LeaderCardDiscardAckMessage extends LeaderCardAckMessage implements ServerActionMessage {


    public LeaderCardDiscardAckMessage(boolean state){
        super(state);
    }

    @Override
    public void doAction(ClientController client) {
        //TODO: Deve fare qualcosa? (LARA: copiato da come era prima, va bene?)
        if (!isState()) {
            System.out.println("Something went wrong, please try again");
            //client.askDiscardLeader();
        }
    }
}
