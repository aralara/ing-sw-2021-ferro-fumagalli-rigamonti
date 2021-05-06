package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.ActionMessage;
import it.polimi.ingsw.utils.messages.server.ResourcesMarketMessage;

import java.util.List;

public class SelectMarketMessage implements ActionMessage {

    private final int row, column;


    public SelectMarketMessage(int row, int column) {
        this.row = row;
        this.column = column;
    }


    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public void doAction(VirtualView view, Controller controller) {
        List<Resource> resources = controller.getFromMarket(view.getNickname(), row, column);
        view.sendMessage(new ResourcesMarketMessage(resources));
    }
}
