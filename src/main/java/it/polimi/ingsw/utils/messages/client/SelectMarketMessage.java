package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;
import it.polimi.ingsw.utils.messages.server.action.ResourcesMarketMessage;

import java.util.List;

public class SelectMarketMessage extends ClientActionMessage {

    private final int row, column;


    public SelectMarketMessage(int row, int column) {
        this.row = row;
        this.column = column;
    }


    @Override
    public void doAction(VirtualView view) {
        Controller controller = view.getGameHandler().getController();
        List<Resource> resources = controller.getFromMarket(view.getNickname(), row, column);
        List<ResourceType> availableResources = controller.getPlayerBoard(view.getNickname()).getAbilityMarbles();
        view.sendMessage(new ServerAckMessage(getUuid(), true));    //TODO: controlli server side
        view.sendMessage(new ResourcesMarketMessage(resources, availableResources));
    }

    @Override
    public void doACKResponseAction(ClientController client) { }

    @Override
    public void doNACKResponseAction(ClientController client) {
        client.ackNotification("Invalid row/column combination, unable to access market", true);
    }
}
