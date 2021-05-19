package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import javafx.event.ActionEvent;

public class NicknameMenuController implements ControllerInterface {
    private GUI gui;

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public void sendNickname(ActionEvent actionEvent) {
    }
}
