package it.polimi.ingsw.server.model.storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface Storage extends Serializable {

    /**
     * Method invoked to merge multiple resource lists in a single list
     * @param lists Lists to be merged
     * @return Returns the created list
     */
    @SafeVarargs
    static List<Resource> mergeResourceList(List<Resource> ... lists) {
        List<Resource> temp = new ArrayList<>();
        for (List<Resource> list : lists)
            temp.addAll(list);
        Storage.aggregateResources(temp);
        return temp;
    }

    /**
     * Checks if the container list contains all of the resources in the contained list
     * @param container Container resource list
     * @param contained Contained resource list
     * @return Returns true if all the resources are contained, false otherwise
     */
    static boolean checkContainedResources(List<Resource> container, List<Resource> contained) {
        for (Resource resource : contained) {
            boolean check = false;
            for (Resource value : container) {
                if (resource.getResourceType() == value.getResourceType() &&
                        value.getQuantity() >= resource.getQuantity()) {
                    check = true;
                    break;
                }
            }
            if (!check)
                return false;
        }
        return true;
    }

    /**
     * Aggregates the resources in the list that has the same resourceType and returns the list
     * @param resources List to be aggregated
     */
    static void aggregateResources(List<Resource> resources) {
        boolean[] toRemove = new boolean[resources.size()];
        Arrays.fill(toRemove, Boolean.FALSE);

        for (int i = 0; i < resources.size(); i++) {
            for (int j = 1; j < resources.size(); j++) {
                if ((resources.get(i).getResourceType() == resources.get(j).getResourceType()) &&
                        i != j && !toRemove[j] && !toRemove[i]) {
                    resources.get(i).add(resources.get(j)); //TODO: questa riga di codice dà problemi quando attiviamo 2 devCard che consumano lo stesso tipo di risorsa: se ho 1 e 2, la volta dopo avrò 3 e 2, poi 5 e 2, ecc
                    toRemove[j] = true;
                }
            }
        }
        for(int i = toRemove.length - 1;i > 0;i--) {
            if(toRemove[i])
                resources.remove(i);
        }
    }

    /**
     * Returns the total quantity of the resources contained in a list
     * @param resources List to be evaluated
     * @return Returns amount of resources
     */
    static int getTotalQuantity(List<Resource> resources) {
        int temp = 0;
        for (Resource resource : resources)
            temp += resource.getQuantity();
        return temp;
    }

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

    /**
     * Gets an aggregated list of all the resources contained in the storage
     * @return Returns the list of resources
     */
    List<Resource> getList();
}
