package it.polimi.ingsw.utils.messages.server.ack;

import it.polimi.ingsw.utils.messages.AckMessage;

public class NewLobbyAckMessage extends AckMessage {


    public NewLobbyAckMessage(boolean state){
        super(state);
    }
}
