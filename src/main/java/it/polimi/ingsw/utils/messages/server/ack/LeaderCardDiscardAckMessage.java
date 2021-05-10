package it.polimi.ingsw.utils.messages.server.ack;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.utils.messages.LeaderCardAckMessage;
import it.polimi.ingsw.utils.messages.server.ServerActionMessage;

public class LeaderCardDiscardAckMessage extends LeaderCardAckMessage implements ServerActionMessage {


    public LeaderCardDiscardAckMessage(boolean state){
        super(state);
    }

    @Override
    public void doAction(CLI client) {
        //TODO: Deve fare qualcosa?
    }
}
