package it.polimi.ingsw.storage;

import java.util.List;

public class Strongbox implements Storage{

    List<Resource> resources;


    public Strongbox() {

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
