package it.polimi.ingsw.storage;

import java.util.List;

public class Warehouse implements Storage{

    private List<Shelf> shelves;


    public Warehouse() {

    }


    public Warehouse(List<Shelf> shelves) {

    }

    /**
     * Checks if a configuration of shelves is valid in order to be added to a warehouse
     * @param configuration List of shelves to be validated
     * @return Returns true if the configuration is valid, false otherwise
     */
    public static boolean validate(List<Shelf> configuration) {
        return false;
    }

    /**
     * Updates the current warehouse configuration to a new list of shelves
     * @param configuration New list of shelves
     * @return Returns true if the configuration is updated correctly, false otherwise
     */
    public boolean changeConfiguration(List<Shelf> configuration) {
        return false;
    }

    @Override
    public List<Resource> getList() {
        return null;
    }

    @Override
    public boolean addResources(List<Resource> resources) {
        return false;
    }

    @Override
    public boolean removeResources(List<Resource> resources) {
        return false;
    }

}
