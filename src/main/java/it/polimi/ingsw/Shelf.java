package it.polimi.ingsw;

import java.util.List;

public class Shelf {

    private ResourceType resourceType;
    private List<Resource> resources;
    private int level;


    public Shelf(){

    }


    public ResourceType getResourceType() {
        return null;
    }

    public void setResourceType(ResourceType resource){
        this.resourceType = resource;
    }

    public int getLevel() {
        return this.level;
    }

    public boolean checkAdd(List<Resource> resources){
        return false;
    }

    public boolean addResources(List<Resource> resources) {
        return false;
    }

    public List<Resource> getList() {
        return null;
    }

    public boolean takeResources(List<Resource> resources){
        return false;
    }

    public boolean checkAvailability(List<Resource> resources){
        return false;
    }
}
