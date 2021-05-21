package it.polimi.ingsw.utils.messages.server.action;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.messages.server.update.ServerUpdateMessage;

public class PlayerDisconnectedMessage implements ServerUpdateMessage {

    @Override
    public void doUpdate(ClientController client) {
        client.getMessageHandler().stop();
    }
}
