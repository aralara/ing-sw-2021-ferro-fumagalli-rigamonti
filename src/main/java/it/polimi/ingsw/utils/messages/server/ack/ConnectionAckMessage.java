package it.polimi.ingsw.utils.messages.server.ack;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.GraphicalCLI;
import it.polimi.ingsw.utils.messages.AckMessage;
import it.polimi.ingsw.utils.messages.server.ServerActionMessage;

public class ConnectionAckMessage extends AckMessage implements ServerActionMessage {


    public ConnectionAckMessage(boolean state){
        super(state);
    }

    @Override
    public void doAction(ClientController client) {
        if (!isState()) {
            ((CLI) client).getGraphicalCLI().printString("Nickname is not available, please choose another one: ");
            ((CLI) client).askNickname();   //TODO: CAST A CLI ORRENDI, BRUTTI E ASSOLUTAMENTE TEMPORANEI IN TUTTO IL METODO
        }
    }
}
