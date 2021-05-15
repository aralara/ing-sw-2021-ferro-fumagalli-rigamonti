package it.polimi.ingsw.utils.messages.server.ack;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.messages.LeaderCardAckMessage;
import it.polimi.ingsw.utils.messages.server.ServerActionMessage;

public class LeaderCardPlayAckMessage extends LeaderCardAckMessage implements ServerActionMessage {


    public LeaderCardPlayAckMessage(boolean state){
        super(state);
    }

    @Override
    public void doAction(ClientController client) {
        if(!isState()){
            System.out.println("Something went wrong, please try again");
            //client.askDiscardLeader();
        }
    }
}
