package it.polimi.ingsw;

import java.util.List;

public class RequestResources implements Storage{

    private List<Resource> requestedResources;
    private Class<Storage> storage;

    public RequestResources()
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
