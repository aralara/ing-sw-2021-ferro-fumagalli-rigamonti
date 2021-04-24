package it.polimi.ingsw.utils.messages;

public class AckMessage implements Message{

    private boolean state;  // true: ACK    false: NACK

    public AckMessage(boolean state) {
        this.state = state;
    }

    public boolean isState() {
        return state;
    }
}
