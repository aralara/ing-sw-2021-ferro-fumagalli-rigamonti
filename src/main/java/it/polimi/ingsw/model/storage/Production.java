package it.polimi.ingsw.model.storage;

import java.util.ArrayList;
import java.util.List;

public class Production {

    List<Resource> consumed, produced;


    public Production(List<Resource> consumed, List<Resource> produced) {
        this.consumed = consumed;
        this.produced = produced;
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
