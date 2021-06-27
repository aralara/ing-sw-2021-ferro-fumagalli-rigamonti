package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;
import it.polimi.ingsw.utils.messages.server.action.ResourcesMarketMessage;

import java.util.List;

/**
 * Client message that selects a marble position to take resources from the market
 */
public class SelectMarketMessage extends ClientActionMessage {

    private final int row, column;


    /**
     * Constructor for a SelectMarketMessage given the coordinates of the selection
     * @param row Row coordinate for the market selection
     * @param column Column coordinate for the market selection
     */
    public SelectMarketMessage(int row, int column) {
        this.row = row;
        this.column = column;
    }


    @Override
    public void doAction(VirtualView view) {    //TODO: sicurezza?
        Controller controller = view.getGameHandler().getController();
        List<Resource> resources = controller.getFromMarket(view.getNickname(), row, column);
        view.sendMessage(new ServerAckMessage(getUuid(), true));
        view.sendMessage(new ResourcesMarketMessage(resources));
    }

    @Override
    public void doACKResponseAction(ClientController client) { }

    @Override
    public void doNACKResponseAction(ClientController client) {
        client.ackNotification("Invalid row/column combination, unable to access market", true);
    }
}
