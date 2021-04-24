package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.server.model.storage.RequestResources;
import it.polimi.ingsw.utils.messages.Message;

import java.util.List;

public class RequestResourcesMessage implements Message {

    private List<RequestResources> requestResources;


    public RequestResourcesMessage(List<RequestResources> requestResources) {
        this.requestResources = requestResources;
    }


    public List<RequestResources> getRequestResources() {
        return requestResources;
    }
}
