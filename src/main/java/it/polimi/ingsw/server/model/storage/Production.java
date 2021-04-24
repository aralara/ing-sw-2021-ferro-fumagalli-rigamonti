package it.polimi.ingsw.server.model.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Production {

    List<Resource> consumed, produced;


    public Production() {
        this.consumed = new ArrayList<>(List.of(new Resource(ResourceType.WILDCARD,2)));
        this.produced = new ArrayList<>(List.of(new Resource(ResourceType.WILDCARD,1)));
    }

    public Production(List<Resource> consumed, List<Resource> produced) {
        this.consumed = consumed;
        this.produced = produced;
    }


    /**
     * Method to return a cloned instance for a Production
     * @return Returns Production cloned object
     */
    public Production makeClone() {
        List<Resource> consumedCopy;
        List<Resource> producedCopy;
        consumedCopy = consumed.stream().map(Resource::makeClone).collect(Collectors.toList());
        producedCopy = produced.stream().map(Resource::makeClone).collect(Collectors.toList());
        return new Production(consumedCopy,producedCopy);
    }

    /**
     * Returns a list of the consumed resources
     * @return Return the resource list
     */
    public List<Resource> getConsumed() {

        return this.consumed;
    }

    /**
     * Return a list of the produced resources
     * @return Return the resource list
     */
    public List<Resource> getProduced() {

        return this.produced;
    }
}
