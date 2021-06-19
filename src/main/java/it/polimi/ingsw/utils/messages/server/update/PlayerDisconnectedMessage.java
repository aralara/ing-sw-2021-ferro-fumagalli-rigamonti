package it.polimi.ingsw.utils.messages.server.update;

import it.polimi.ingsw.client.ClientController;

/**
 * Server update message for when another players disconnects from the game
 */
public class PlayerDisconnectedMessage implements ServerUpdateMessage {

    @Override
    public void doUpdate(ClientController client) {
        client.destroy();
    }
}
