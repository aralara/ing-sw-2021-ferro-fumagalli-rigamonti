package it.polimi.ingsw;

import java.util.List;

public interface Storage {

    static List<Resource> mergeResourceList(List<Resource>[] lists)
    {
        return null;
    }
    static List<Resource> checkContainedResources(List<Resource> container, List<Resource> contained)
    {
        return null;
    }
    List<Resource> getList();
    boolean addResources(List<Resource> resources);
    boolean takeResources(List<Resource> resources);
    boolean checkAvailability(List<Resource> resources);
}
