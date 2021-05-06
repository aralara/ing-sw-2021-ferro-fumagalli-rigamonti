package it.polimi.ingsw.client.structures;

import it.polimi.ingsw.server.model.market.MarbleColors;
import it.polimi.ingsw.server.model.storage.ResourceType;

public class MarbleView { //TODO: uguale a quella nel package Server

    private MarbleColors color; //TODO: serve enum(magari tutti in utils) o cambiare con String?
    private ResourceType resourceType;


    public MarbleView(MarbleColors color, ResourceType resourceType) {
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
     * Sets the color attribute
     * @param color New attribute value
     */
    public void setColor(MarbleColors color){
        this.color = color;
    }

    /**
     * Gets the resourceType attribute
     * @return Returns resourceType
     */
    public ResourceType getResourceType() {
        return this.resourceType;
    }

    /**
     * Sets the resourceType attribute
     * @param resourceType New attribute value
     */
    public void setResourceType(ResourceType resourceType){
        this.resourceType = resourceType;
    }
}
