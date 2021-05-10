package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.utils.messages.Message;

public interface ServerActionMessage extends Message {
    void doAction(CLI client);
}
