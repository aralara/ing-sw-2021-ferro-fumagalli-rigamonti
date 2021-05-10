package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.cli.CLI;

public class AskLeaderCardDiscardMessage implements ServerActionMessage {

    @Override
    public void doAction(CLI client) {
        client.askDiscardLeader();
    }
}
