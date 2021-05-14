package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.utils.messages.Message;

public interface ServerUpdateMessage extends Message {
    void doUpdate(CLI client);
}
