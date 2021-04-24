package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.utils.messages.Message;

public class SelectMarketMessage implements Message {

    private int row, column;


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
}
