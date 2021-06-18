package it.polimi.ingsw.utils.messages;

import java.util.UUID;

/**
 * TODO: fare javadoc
 */
public class IdentifiedMessage implements Message {

    private final UUID uuid;

    public IdentifiedMessage() {
        this.uuid = UUID.randomUUID();
    }

    public IdentifiedMessage(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean compareTo(IdentifiedMessage message) {
        return uuid.compareTo(message.getUuid()) == 0;
    }
}
