package it.polimi.ingsw;

import java.util.List;

public class RequestResources implements Storage{

    private List<Resource> requestedResources;
    private Class<Storage> storage;

    public RequestResources()
    {

    }

    @Override
    public List<Resource> getList()
    {
        return null;
    }

    @Override
    public boolean addResources(List<Resource> resources)
    {
        return false;
    }

    @Override
    public boolean takeResources(List<Resource> resources)
    {
        return false;
    }

    @Override
    public boolean checkAvailability(List<Resource> resources)
    {
        return false;
    }
}
