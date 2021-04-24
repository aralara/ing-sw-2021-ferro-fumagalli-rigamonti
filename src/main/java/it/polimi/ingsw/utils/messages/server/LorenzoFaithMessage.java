package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.utils.messages.Message;

public class LorenzoFaithMessage implements Message {

    private int faith;


    public LorenzoFaithMessage(int faith) {
        this.faith = faith;
    }


    public int getFaith() {
        return faith;
    }
}
