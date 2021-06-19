package it.polimi.ingsw.client.structures;

import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.Strongbox;
import it.polimi.ingsw.utils.listeners.Listened;
import it.polimi.ingsw.utils.listeners.Listeners;

import java.io.Serializable;
import java.util.List;

/**
 * Handles strongbox client methods
 */
public class StrongboxView extends Listened implements Serializable {

    private List<Resource> resources;

    /**
     * StrongboxView constructor with parameters
     * @param strongbox Strongbox to set
     */
    public StrongboxView(Strongbox strongbox) {
        this.resources = strongbox.getList();
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
        if(hasListeners())
            fireUpdate(Listeners.BOARD_STRONGBOX.value(), resources);
    }
}
