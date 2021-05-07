package it.polimi.ingsw.client.structures;

import it.polimi.ingsw.server.model.storage.Resource;

import java.util.ArrayList;
import java.util.List;

public class StrongboxView {

    private List<Resource> resources;


    public StrongboxView() {
        this.resources = new ArrayList<>();
    }


    /**
     * Gets the resources attribute
     * @return Returns resources
     */
    public List<Resource> getResources() {
        return resources;
    }

    /**
     * Sets the resources attribute
     * @param resources New attribute value
     */
    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }
}
