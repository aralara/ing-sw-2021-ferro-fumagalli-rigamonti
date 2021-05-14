package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.messages.Message;

public interface ServerActionMessage extends Message {
    void doAction(ClientController client);
}
