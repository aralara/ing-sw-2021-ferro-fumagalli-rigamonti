package it.polimi.ingsw.server.model.storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Production implements Serializable {

    private final List<Resource> consumed, produced;


    /**
     * Production default constructor (initializes the production to be a basic production)
     */
    public Production() {
        this.consumed = new ArrayList<>(List.of(new Resource(ResourceType.WILDCARD,2)));
        this.produced = new ArrayList<>(List.of(new Resource(ResourceType.WILDCARD,1)));
    }

    /**
     * Production constructor with parameters
     * @param consumed Consumed resources
     * @param produced Produced resources
     */
    public Production(List<Resource> consumed, List<Resource> produced) {
        this.consumed = consumed;
        this.produced = produced;
    }


    @Override
    public String toString() {
        StringBuilder toPrint = new StringBuilder("\tConsumed: ");
        boolean first = true;

        for (Resource resource : consumed) {
            toPrint.append((!first) ? "\t          " : "").append(" > ").append(resource.getQuantity())
                    .append(" ").append(resource.getResourceType()).append("\n");
            first = false;
        }
        toPrint.append("\tProduced: ");
        first = true;
        for (Resource resource : produced) {
            toPrint.append((!first) ? "\t          " : "").append(" > ").append(resource.getQuantity())
                    .append(" ").append(resource.getResourceType()).append("\n");
            first = false;
        }
        return toPrint.toString();
    }

    /**
     * Returns a cloned instance of the Production
     * @return Returns the cloned object
     */
    public Production makeClone() {
        List<Resource> consumedCopy;
        List<Resource> producedCopy;
        consumedCopy = consumed.stream().map(Resource::makeClone).collect(Collectors.toList());
        producedCopy = produced.stream().map(Resource::makeClone).collect(Collectors.toList());
        return new Production(consumedCopy,producedCopy);
    }

    /** //TODO: da fare
     * Compares two productions and declares if this production could have resolved into the other
     * @param p Production to compare
     * @return True if this production could have resolved into the other, false otherwise
     *//*
    public boolean equalsResolved(Production p) {
        List<Resource> consumed = this.makeClone().getConsumed().stream()
                .filter(r -> r.getResourceType() != ResourceType.WILDCARD).collect(Collectors.toList());
        int nWildConsumed = (int) this.makeClone().getConsumed().stream()
                .filter(r -> r.getResourceType() == ResourceType.WILDCARD).count();
        List<Resource> produced = this.makeClone().getProduced().stream()
                .filter(r -> r.getResourceType() != ResourceType.WILDCARD).collect(Collectors.toList());
        int nWildProduced = (int) this.makeClone().getProduced().stream()
                .filter(r -> r.getResourceType() == ResourceType.WILDCARD).count();
        List<Resource> pConsumed = p.makeClone().getConsumed();
        List<Resource> pProduced = p.makeClone().getProduced();
        Storage.aggregateResources(consumed);
        Storage.aggregateResources(produced);
        Storage.aggregateResources(pConsumed);
        Storage.aggregateResources(pProduced);
        List<Resource> plus = new ArrayList<>();
        for(Resource rp : pConsumed) {
            for(Resource r : consumed) {
                if(r.getResourceType() == rp.getResourceType()) {
                    if(r.getResourceType() != ResourceType.FAITH && r.getResourceType() != ResourceType.WILDCARD)
                        if()
                }
            }
        }
        return consumed.stream().allMatch(c -> pConsumed.stream().anyMatch(pc ->
                        pc.getResourceType() == c.getResourceType() && pc.getQuantity() == c.getQuantity())) &&
                produced.stream().allMatch(c -> pProduced.stream().anyMatch(pc ->
                        pc.getResourceType() == c.getResourceType() && pc.getQuantity() == c.getQuantity()));
    }*/

    /**
     * Returns a list of the consumed resources
     * @return Returns the resource list
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
