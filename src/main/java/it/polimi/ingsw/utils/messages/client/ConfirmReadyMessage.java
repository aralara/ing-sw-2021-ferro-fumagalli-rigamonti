package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.controller.GameHandler;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;

/**
 * Client message that notifies the server when the player is ready
 */
public class ConfirmReadyMessage extends ClientActionMessage {

    /**
     * Default constructor for ConfirmReadyMessage
     */
    public ConfirmReadyMessage() { }


    @Override
    public void doAction(VirtualView view) {
        GameHandler gameHandler = view.getGameHandler();
        if(gameHandler.getSizeSetup() < gameHandler.getSize()) {
            boolean success = gameHandler.playerFinishedSetup();
            view.sendMessage(new ServerAckMessage(getUuid(), success));
            if (success)
                gameHandler.resumeGame();
        }
        else
            view.sendMessage(new ServerAckMessage(getUuid(), true));
    }

    @Override
    public void doACKResponseAction(ClientController client) { }

    @Override
    public void doNACKResponseAction(ClientController client) {
        client.ackNotification("Waiting for other players to finish their setup actions", true);
    }
}
