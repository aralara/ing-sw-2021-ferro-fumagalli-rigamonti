package it.polimi.ingsw.utils.messages.server.update;

import it.polimi.ingsw.client.ClientController;

/**
 * TODO: fare javadoc
 */
public class PlayerDisconnectedMessage implements ServerUpdateMessage {

    @Override
    public void doUpdate(ClientController client) {
        client.destroy();
    }
}
