package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GraphicalGUI;

public class DecksBoardController implements ControllerInterface {
    private GraphicalGUI graphicalGUI;

    @Override
    public void setGui(GraphicalGUI graphicalGUI) {
        this.graphicalGUI = graphicalGUI;
    }
}
