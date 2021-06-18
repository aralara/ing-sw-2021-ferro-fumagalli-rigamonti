package it.polimi.ingsw.server.model.market;

import it.polimi.ingsw.server.model.storage.ResourceType;

import java.io.Serializable;

/**
 * Handles methods relative to the marble
 */
public class Marble implements Serializable {

    private final MarbleColors color;
    private final ResourceType resourceType;


    /**
     * Marble constructor with parameters
     * @param color Color of the Marble
     * @param resourceType Resource type of the marble
     */
    public Marble(MarbleColors color, ResourceType resourceType) {
        this.color = color;
        this.resourceType = resourceType;
    }


    /**
     * Gets the color attribute
     * @return Returns color
     */
    public MarbleColors getColor() {
        return this.color;
    }

    /**
     * Gets the resourceType attribute
     * @return Returns resourceType
     */
    public ResourceType getResourceType() {
        return this.resourceType;
    }
}
