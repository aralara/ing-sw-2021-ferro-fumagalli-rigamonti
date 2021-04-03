package it.polimi.ingsw.model.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Warehouse implements Storage{

    private List<Shelf> shelves;


    public Warehouse() {
        shelves = new ArrayList<>();
    }


    public Warehouse(List<Shelf> shelves) {
        this.shelves = shelves;
    }


    /**
     * Checks if a configuration of shelves is valid in order to be added to a warehouse
     * @param configuration List of shelves to be validated
     * @return Returns true if the configuration is valid, false otherwise
     */
    public static boolean validate(List<Shelf> configuration) {

        if (configuration.stream().filter(Shelf::getIsLeader).count() > 2) {
            return false;
        }
        if(configuration.stream().filter(t -> !t.getIsLeader()).count() > 3) {
            return false;
        }
        for(int i=0;i<configuration.size()-1;i++) {
            for(int j=1;j<configuration.size();j++) {
                if(!configuration.get(i).getIsLeader() && !configuration.get(j).getIsLeader() && i !=j) {
                    if(configuration.get(i).getResourceType() == configuration.get(j).getResourceType()) {
                        return false;
                    }
                    if(configuration.get(i).getLevel() == configuration.get(j).getLevel()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Adds a shelf to the list of shelves
     * @param shelf Shelf that need to be added
     */
    public void addShelf(Shelf shelf){
        shelves.add(shelf);
    }

    /**
     * Updates the current warehouse configuration to a new list of shelves
     * @param configuration New list of shelves
     * @return Returns true if the configuration is updated correctly, false otherwise
     */
    public boolean changeConfiguration(List<Shelf> configuration) {
        if(validate(configuration))
        {
            this.shelves = configuration;
            return true;
        }
        return false;
    }

    @Override
    public List<Resource> getList() {
        List<List<Resource>> temp = new ArrayList<>();
        for (Shelf shelf : shelves) {
            temp.add(shelf.getList());
        }
        return Storage.aggregateResources(Storage.mergeResourceList(temp));
    }

    @Override
    public boolean addResources(List<Resource> resources) {
        return false;
    }


    @Override
    public boolean removeResources(List<Resource> resources) {
        if(Storage.checkContainedResources(this.getList(), resources)) {
            for (Shelf shelf : shelves) {
                if(!shelf.getIsLeader()) {
                    shelf.removeResources(resources.stream().filter(k -> k.getResourceType() ==
                            shelf.getResourceType()).collect(Collectors.toList()));
                }
            }
        }
        return false;
    }
}
