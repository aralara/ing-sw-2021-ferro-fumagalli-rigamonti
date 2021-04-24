package it.polimi.ingsw.server.model.market;

import it.polimi.ingsw.server.model.storage.ResourceType;

public class Marble {

    private MarbleColors color;
    private ResourceType resourceType;


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
