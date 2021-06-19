package it.polimi.ingsw.server.model.storage;

import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.listeners.Listeners;
import it.polimi.ingsw.utils.listeners.server.PlayerListened;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles methods relative to the warehouse
 */
public class Warehouse extends PlayerListened implements Storage {

    private List<Shelf> shelves;


    /**
     * Warehouse default constructor (initializes a 3 shelf warehouse with incremental sizes)
     */
    public Warehouse() {
        shelves = new ArrayList<>();
        for(int i = 0; i < Constants.BASE_WAREHOUSE_SHELVES.value(); i++)
            shelves.add(new Shelf(i + 1));
    }

    /**
     * Warehouse constructor from a list of shelves
     */
    public Warehouse(List<Shelf> shelves) {
        this.shelves = new ArrayList<>();
        for(Shelf shelf : shelves)
            this.shelves.add(shelf.makeClone());
    }


    /**
     * Checks if a configuration of shelves is valid in order to be added to a warehouse
     * @param configuration List of shelves to be validated
     * @return Returns true if the configuration is valid, false otherwise
     */
    public static boolean validate(List<Shelf> configuration) {
        if (configuration.stream().filter(Shelf::isLeader).count() > 2)
            return false;
        if (configuration.stream().filter(t -> !t.isLeader()).count() > 3)
            return false;
        for (int i = 0; i < configuration.size() - 1; i++) {
            for (int j = 1; j < configuration.size(); j++) {
                if (!configuration.get(i).isLeader() && !configuration.get(j).isLeader() && i != j) {
                    if (!(configuration.get(i).getResourceType().equals(ResourceType.WILDCARD) ||
                            configuration.get(j).getResourceType().equals(ResourceType.WILDCARD)) &&
                            configuration.get(i).getResourceType() == configuration.get(j).getResourceType())
                        return false;
                    if (configuration.get(i).getLevel() == configuration.get(j).getLevel())
                        return false;
                }
            }
        }
        return true;
    }

    /**
     * Adds a shelf to the list of shelves
     * @param shelf Shelf that need to be added
     */
    public void addShelf(Shelf shelf) {
        shelves.add(shelf);
        fireUpdate(Listeners.BOARD_WAREHOUSE.value(), shelves);
    }

    /**
     * Updates the current warehouse configuration to a new list of shelves
     * @param configuration New list of shelves
     * @return Returns true if the configuration is updated correctly, false otherwise
     */
    public boolean changeConfiguration(List<Shelf> configuration) {
        if (validate(configuration)) {
            this.shelves = configuration;
            fireUpdate(Listeners.BOARD_WAREHOUSE.value(), shelves);
            return true;
        }
        return false;
    }

    @Override
    public boolean addResources(List<Resource> resources) {
        return false;
    }

    @Override
    public boolean removeResources(List<Resource> resources) {
        return true;
    }

    /**
     * Removes a list of resources from the shelves in the warehouse
     * @param resources List of resources to be removed
     * @param isLeader Indicates if the type of controlled shelves is leader or not
     * @return Returns true if the list is removed correctly, false otherwise
     */
    public boolean removeResources(List<Resource> resources, boolean isLeader) {
        Storage.aggregateResources(resources);
        if (Storage.checkContainedResources(this.getList(isLeader), resources)) {
            for (Shelf shelf : shelves) {
                if (shelf.isLeader() == isLeader) {
                    for (Resource resource : resources) {
                        if (shelf.getResources().getResourceType() == resource.getResourceType()) {
                            shelf.removeResources(resource);
                            if(shelf.getResources().getQuantity() == 0 && !shelf.isLeader()) {
                                shelf.setResourceType(ResourceType.WILDCARD);
                            }else if (shelf.getResources().getQuantity() == 0 && shelf.isLeader()){
                                shelf.getResources().setResourceType(shelf.getResourceType());
                            }
                        }
                    }
                }
            }
            fireUpdate(Listeners.BOARD_WAREHOUSE.value(), shelves);
            return true;
        }
        return false;
    }

    /**
     * Returns a list of resources containing the difference between the warehouse and a list of shelves
     * @param addedResources List of resources containing the new resources
     * @return Returns a list of resources, returns null if the list would contain negative values
     */
    public List<Resource> getAddedResources(List<Resource> addedResources) {
        List<Resource> originalResources = getList();
        List<Resource> difference = new ArrayList<>();

        if(!originalResources.stream().allMatch(or -> or.getQuantity() == 0 || addedResources.stream().anyMatch(ar ->
                ar.getResourceType() == or.getResourceType() && ar.getQuantity() >= or.getQuantity())))
            return null;

        for(Resource ar : addedResources) {
            boolean resolved = false;
            for(Resource or : originalResources) {
                if (ar.getResourceType() == or.getResourceType()) {
                    resolved = true;
                    int resDiff = ar.getQuantity() - or.getQuantity();
                    if(resDiff > 0) {
                        difference.add(new Resource(ar.getResourceType(), resDiff));
                        break;
                    }
                    else if(resDiff < 0)
                        return null;
                }
            }
            if(!resolved)   // If a new type of resource has been added, it's calculated separately
                difference.add(new Resource(ar.getResourceType(), ar.getQuantity()));
        }
        return difference;
    }

    @Override
    public List<Resource> getList() {
        List<Resource> tempList = new ArrayList<>();
        for (Shelf shelf : shelves)
            tempList = Storage.mergeResourceList(tempList,shelf.makeClone().getList());
        Storage.aggregateResources(tempList);
        return tempList;
    }

    /**
     * Gets an aggregated list of all the resources contained in the Warehouse that respects the parameter
     * @param isLeader Indicates if the type of controlled shelves is leader or not
     * @return Returns the list of resources
     */
    public List<Resource> getList(boolean isLeader) {
        List<Resource> tempList = new ArrayList<>();
        for (Shelf shelf : shelves) {
            if (shelf.isLeader() == isLeader)
                tempList = Storage.mergeResourceList(tempList,shelf.makeClone().getList());
        }
        Storage.aggregateResources(tempList);
        return tempList;
    }

    /**
     * Gets if the warehouse is empty
     * @return Returns true if the warehouse is empty, false otherwise
     */
    public boolean isEmpty() {
        return shelves.stream().allMatch(Shelf::isEmpty);
    }

    /**
     * Returns the cloned shelves
     * @return Returns a list of cloned shelves
     */
    public List<Shelf> getShelves() {
        List<Shelf> temp = new ArrayList<>();
        for (Shelf shelf : shelves)
            temp.add(shelf.makeClone());
        return temp;
    }
}

