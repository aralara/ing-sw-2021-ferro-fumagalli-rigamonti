package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;

public class SaveMessage extends ClientActionMessage {

    public SaveMessage() { }


    @Override
    public void doAction(VirtualView view) {
        boolean success = view.getGameHandler().saveGame();
        view.sendMessage(new ServerAckMessage(getUuid(), success));
    }

    @Override
    public void doACKResponseAction(ClientController client) {
        client.ackNotification("Game saved successfully", true);
    }

    @Override
    public void doNACKResponseAction(ClientController client) {
        client.ackNotification("Unable to save game at this point in time", true);
    }
}
