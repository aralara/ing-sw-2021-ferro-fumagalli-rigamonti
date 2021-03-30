package it.polimi.ingsw.market;

import it.polimi.ingsw.storage.ResourceType;

public class Marble {

    private MarbleColors color;
    private ResourceType resourceType;


    public Marble(MarbleColors color, ResourceType resourceType) {

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
