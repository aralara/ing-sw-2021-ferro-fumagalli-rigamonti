package it.polimi.ingsw.utils.messages.server.ack;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.utils.messages.AckMessage;
import it.polimi.ingsw.utils.messages.server.ServerActionMessage;

public class ConnectionAckMessage extends AckMessage implements ServerActionMessage {


    public ConnectionAckMessage(boolean state){
        super(state);
    }

    @Override
    public void doAction(CLI client) {    //TODO: ci sono print nel messaggio
        if (!isState()) {
            System.out.println("Nickname is not available, please choose another one");
            client.askNickname();
        }
    }
}
