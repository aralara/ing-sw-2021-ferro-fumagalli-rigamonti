package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.server.model.storage.Shelf;
import it.polimi.ingsw.utils.messages.Message;

import java.util.List;

public class ShelfConfiugrationMessage implements Message {

    private List<Shelf> shelves;


    public ShelfConfiugrationMessage(List<Shelf> shelves) {
        this.shelves = shelves;
    }


    public List<Shelf> getShelves() {
        return shelves;
    }
}
