package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.Shelf;
import it.polimi.ingsw.utils.messages.Message;

import java.util.List;

public class ShelvesConfigurationMessage implements Message {

    private List<Shelf> shelves;
    private List<Resource> extra;


    public ShelvesConfigurationMessage(List<Shelf> shelves, List<Resource> extra) {
        this.shelves = shelves;
        this.extra = extra;
    }


    public List<Shelf> getShelves() {
        return shelves;
    }

    public List<Resource> getExtra() {
        return extra;
    }
}
