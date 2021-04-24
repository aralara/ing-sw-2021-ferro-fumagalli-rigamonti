package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.server.model.storage.Production;
import it.polimi.ingsw.utils.messages.Message;

import java.util.List;

public class ActivateProductionsMessage implements Message {

    private List<Production> productions;


    public ActivateProductionsMessage(List<Production> productions) {
        this.productions = productions;
    }


    public List<Production> getProductions() {
        return productions;
    }
}
