package it.polimi.ingsw;

import java.util.List;

public class Warehouse implements Storage{

    private List<Shelf> shelves;


    public Warehouse()
    {

    }

    public Warehouse(List<Shelf> shelves) {

    }

    public static boolean validate(List<Shelf> configuration)
    {
        return false;
    }

    public boolean changeConfiguration(List<Shelf> configuration)
    {
        return false;
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
