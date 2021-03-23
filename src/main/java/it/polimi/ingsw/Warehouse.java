package it.polimi.ingsw;

import java.util.List;

public class Warehouse implements Storage{

    private List<Resource> shelf1, shelf2, shelf3;


    public Warehouse()
    {

    }

    public Warehouse(List<Resource> shelf1, List<Resource> shelf2, List<Resource> shelf3) {
        this.shelf1 = shelf1;
        this.shelf2 = shelf2;
        this.shelf3 = shelf3;
    }

    public static boolean validate(Warehouse configuration)
    {
        return false;
    }

    public boolean changeConfiguration(Warehouse configuration)
    {
        return false;
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
