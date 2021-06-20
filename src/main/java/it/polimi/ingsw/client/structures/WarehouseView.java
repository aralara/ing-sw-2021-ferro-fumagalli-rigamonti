package it.polimi.ingsw.client.structures;

import it.polimi.ingsw.server.model.storage.Shelf;
import it.polimi.ingsw.server.model.storage.Warehouse;
import it.polimi.ingsw.utils.listeners.Listened;
import it.polimi.ingsw.utils.listeners.Listeners;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles warehouse client methods
 */
public class WarehouseView extends Listened implements Serializable {

    private List<Shelf> shelves;


    /**
     * WarehouseView constructor with parameters
     * @param warehouse Warehouse to set
     */
    public WarehouseView(Warehouse warehouse) {
        this.shelves = warehouse.getShelves();
    }

    /**
     * WarehouseView constructor with parameters
     * @param shelves Shelves to set
     */
    public WarehouseView(List<Shelf> shelves) { //TODO: sistemare chiamate a questo metodo nella CLI (preferibilmente toglierle)
        this.shelves = shelves;
    }


    /**
     * Gets if the warehouse is empty
     * @return Returns true if the warehouse is empty, false otherwise
     */
    public boolean isEmpty() {
        return shelves.stream().allMatch(Shelf::isEmpty);
    }

    /**
     * Gets a clone of the warehouse shelves
     * @return Returns a list of the cloned shelves
     */
    public List<Shelf> getShelvesClone() {
        return shelves.stream().map(Shelf::makeClone).collect(Collectors.toList());
    }

    /**
     * Gets the shelves attribute
     * @return Returns shelves
     */
    public List<Shelf> getShelves() {
        return shelves;
    }

    /**
     * Sets the shelves attribute
     * @param shelves New attribute value
     */
    public void setShelves(List<Shelf> shelves) {
        this.shelves = shelves;
        if(hasListeners())
            fireUpdate(Listeners.BOARD_WAREHOUSE.value(), shelves);
    }
}
