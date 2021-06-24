package it.polimi.ingsw.utils.messages;

import java.util.UUID;

/**
 * Generic acknowledgment message
 */
public class AckMessage extends IdentifiedMessage {

    private final boolean state;  // true: ACK    false: NACK

    /**
     * Constructor for a AckMessage given a UUID code and its state
     * @param uuid Unique code that binds an ACK message to its original message
     * @param state Identifies the message as an ACK or a NACK
     */
    public AckMessage(UUID uuid, boolean state) {
        super(uuid);
        this.state = state;
    }

    /**
     * Getter for the state attribute
     * @return Return state value
     */
    public boolean isState() {
        return state;
    }
}
