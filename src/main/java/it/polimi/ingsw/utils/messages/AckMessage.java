package it.polimi.ingsw.utils.messages;

import java.util.UUID;

/**
 * Generic acknowledgment message
 */
public class AckMessage extends IdentifiedMessage {

    private final boolean state;  // true: ACK    false: NACK

    public AckMessage(UUID uuid, boolean state) {
        super(uuid);
        this.state = state;
    }

    public boolean isState() {
        return state;
    }
}
