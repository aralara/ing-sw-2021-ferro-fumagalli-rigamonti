package it.polimi.ingsw.server.model.storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Defines an interface to handle common parts of all storages
 */
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
            if(resource.getQuantity() > 0) {
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
        }
        return true;
    }

    /**
     * Checks if a list of resources can be modeled using wildcards from another one
     * @param model Model list
     * @param modeled List that needs to be checked
     * @param possibleWildcards Possible wildcards that blank resources can be turned into
     * @param allowEmptyWildcards Flag that determines if a wildcard in the model list can result into nothing inside the modeled list
     * @return Returns true if modeled can be obtained from model, false otherwise
     */
    static boolean checkListModeled(List<Resource> model, List<Resource> modeled,
                                           List<ResourceType> possibleWildcards, boolean allowEmptyWildcards) {
        List<Resource> c_model = new ArrayList<>();
        List<Resource> c_modeled = new ArrayList<>();
        model.forEach(r -> c_model.add(r.makeClone()));
        modeled.forEach(r -> c_modeled.add(r.makeClone()));
        Storage.aggregateResources(c_model);
        Storage.aggregateResources(c_modeled);
        try {
            // Modeled list shouldn't contain wildcards
            if(c_modeled.stream().anyMatch(r -> r.getResourceType() == ResourceType.WILDCARD))
                return false;
            // Subtracts all of the non-wildcard generated resources from the modeled list
            for(Resource r : c_model)
                if (r.getResourceType() != ResourceType.WILDCARD) {
                    Resource modeledResource = c_modeled.stream()
                            .filter(r2 -> r.getResourceType() == r2.getResourceType()).findFirst().orElseThrow();
                    if (r.getQuantity() <= modeledResource.getQuantity())
                        modeledResource.setQuantity(modeledResource.getQuantity() - r.getQuantity());
                    else
                        return false;
                }
            // Checks if the wildcards from the model list can generate the remaining resources in the modeled list
            for(Resource r : c_model)
                if (r.getResourceType() == ResourceType.WILDCARD)
                    while(r.getQuantity() > 0) {
                        Optional<Resource> modeledResourceOpt = c_modeled.stream()
                                .filter(r2 -> r2.getQuantity() > 0 && possibleWildcards
                                        .stream().anyMatch(rt -> rt == r2.getResourceType())).findFirst();

                        if(allowEmptyWildcards && modeledResourceOpt.isEmpty())
                            break;
                        else {
                            Resource modeledResource = modeledResourceOpt.orElseThrow();
                            modeledResource.setQuantity(modeledResource.getQuantity() - 1);
                            r.setQuantity(r.getQuantity() - 1);
                        }
                    }

            // Checks if the modeled list has any excess resources
            if(c_modeled.stream().anyMatch(r -> r.getQuantity() > 0))
                return false;
        } catch(Exception e) {
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
                    resources.get(i).add(resources.get(j));
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
     * Calculates a net cost given its possible discount
     * @param cost Gross cost as a list of resources
     * @param discounts Possible discounts as a list of resource types
     * @return Returns the net cost as a list of resources
     */
    static List<Resource> calculateDiscount(List<Resource> cost, List<ResourceType> discounts) {
        List<Resource> returnCost = cost;
        if (discounts.size() > 0) {
            final List<Resource> temp = new ArrayList<>();
            cost.forEach(r -> temp.add(r.makeClone()));
            discounts.forEach(rt -> {
                Optional<Resource> res = temp.stream().filter(r -> rt == r.getResourceType()).findFirst();
                if(res.isPresent()) {
                    if(res.get().getQuantity() == 0)
                        temp.remove(res.get());
                    else
                        res.get().setQuantity(res.get().getQuantity() - 1);
                }
            });
            returnCost = temp;
        }
        return returnCost;
    }

    /**
     * Checks if a list of resources is discarded correctly
     * @param resources resources to be placed
     * @param toDiscard resources to be discarded
     * @return Returns true if the discarded resource is correct
     */
    static boolean isDiscardedResCorrect(List<Resource> resources, List<Resource> toDiscard) {
        aggregateResources(resources);
        aggregateResources(toDiscard);
        return checkContainedResources(resources,toDiscard);
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
