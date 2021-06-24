package it.polimi.ingsw.utils.messages;

import java.util.UUID;

/**
 * Generic message that needs to be tracked and/or associated to an ACK message
 */
public class IdentifiedMessage implements Message {

    private final UUID uuid;


    /**
     * Constructor of a IdentifiedMessage with an new random UUID identifier
     */
    public IdentifiedMessage() {
        this.uuid = UUID.randomUUID();
    }

    /**
     * Constructor of a IdentifiedMessage using an already existing UUID identifier
     * @param uuid Unique code that binds an ACK message to its original message
     */
    public IdentifiedMessage(UUID uuid) {
        this.uuid = uuid;
    }


    /**
     * Gets the uuid attribute
     * @return Return uuid value
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Compares two identified messages based on their UUIDs
     * @param message Message to compare
     * @return Returns true if the messages have identical UUIDs
     */
    public boolean compareTo(IdentifiedMessage message) {
        return uuid.compareTo(message.getUuid()) == 0;
    }
}
