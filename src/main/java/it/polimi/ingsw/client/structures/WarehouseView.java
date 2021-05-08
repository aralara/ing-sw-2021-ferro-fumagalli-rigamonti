package it.polimi.ingsw.client.structures;

import it.polimi.ingsw.server.model.storage.Shelf;

import java.util.ArrayList;
import java.util.List;

public class WarehouseView {

    private List<Shelf> shelves;


    public WarehouseView() {
        this.shelves = new ArrayList<>();
    }

    public WarehouseView(List<Shelf> shelves) {
        this.shelves = shelves;
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
    }
}