package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.controller.GameHandler;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;

/**
 * TODO: fare javadoc
 */
public class ConfirmReadyMessage extends ClientActionMessage {

    public ConfirmReadyMessage() { }


    @Override
    public void doAction(VirtualView view) {
        GameHandler gameHandler = view.getGameHandler();
        boolean success = view.getGameHandler().playerFinishedSetup();
        view.sendMessage(new ServerAckMessage(getUuid(), success));
        if(success)
            gameHandler.resumeGame();
    }

    @Override
    public void doACKResponseAction(ClientController client) { }

    @Override
    public void doNACKResponseAction(ClientController client) {
        client.ackNotification("Waiting for other players to finish their setup actions", false);
    }
}
