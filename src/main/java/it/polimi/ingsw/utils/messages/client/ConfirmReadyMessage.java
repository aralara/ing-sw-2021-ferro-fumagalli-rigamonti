package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.GameHandler;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;

public class ConfirmReadyMessage extends ClientActionMessage {

    public ConfirmReadyMessage() { }


    @Override
    public void doAction(VirtualView view, Controller controller) {
        GameHandler gameHandler = view.getGameHandler();
        boolean success = view.getGameHandler().playerFinishedSetup();
        view.sendMessage(new ServerAckMessage(getUuid(), success));
        if(success)
            gameHandler.resumeGame();
    }

    @Override
    public void doNACKResponseAction(ClientController client) {
        client.ackNotification("Waiting for other players to finish their setup actions");
    }
}
