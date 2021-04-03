package it.polimi.ingsw.model.storage;

import java.util.ArrayList;
import java.util.List;

public interface Storage {


    /**
     * Method invoked to merge multiple resource lists in a single list
     * @param lists Lists to be merged
     * @return Returns the created list
     */
    static List<Resource> mergeResourceList(List<List<Resource>> lists) {
        List<Resource> temp = new ArrayList<>();
        for (List<Resource> list : lists) {
            temp.addAll(list);
        }
        return temp;
    }

    /**
     * Checks if the container list contains all of the resources in the contained list
     * @param container Container resource list
     * @param contained Contained resource list
     * @return Returns true if all the resources are contained, false otherwise
     */
    static boolean checkContainedResources(List<Resource> container, List<Resource> contained) {

        return container.containsAll(contained);
    }

    /**
     * Aggregates the resources in the list that have the same resourceType and return the list
     * @param resources List to be aggregated
     * @return Returns the list updated
     */
    static List<Resource> aggregateResources(List<Resource> resources) {

        for(int i=0;i<resources.size();i++){
            for(int j=1;j<resources.size();j++){
                if((resources.get(i).getResourceType() == resources.get(j).getResourceType()) && i!=j){
                    resources.get(i).add(resources.get(j));
                    resources.remove(j);
                }
            }
        }
        return resources;
    }

    /**
     * Gets an aggregated list of all the resources contained in the storage
     * @return Returns the list of resources
     */
    List<Resource> getList();

    /**
     * Adds a list of resources to the storage
     * @param resources List of resources to be added
     * @return Returns true if the list is added correctly, false otherwise
     */
    boolean addResources(List<Resource> resources);

    /**
     * Removes a list of resources from the storage
     * @param resources List of resources to be removed
     * @return Returns true if the list is removed correctly, false otherwise
     */
    boolean removeResources(List<Resource> resources);
}
