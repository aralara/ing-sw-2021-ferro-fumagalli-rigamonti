package it.polimi.ingsw;

import java.util.List;

public class Strongbox implements Storage{

    List<Resource> resources;

    public Strongbox()
    {

    }

    public List<Resource> getList()
    {
        return null;
    }

    public boolean addResources(List<Resource> resources)
    {
        return false;
    }

    public boolean takeResources(List<Resource> resources)
    {
        return false;
    }

    public boolean checkAvailability(List<Resource> resources)
    {
        return false;
    }
}
