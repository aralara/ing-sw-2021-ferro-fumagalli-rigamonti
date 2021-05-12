package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.cli.CLI;

public class LorenzoFaithMessage implements ServerActionMessage {

    private final int faith;


    public LorenzoFaithMessage(int faith) {
        this.faith = faith;
    }


    public int getFaith() {
        return faith;
    }

    @Override
    public void doAction(CLI client) {
        client.setLorenzoFaith(faith);
    }
}
