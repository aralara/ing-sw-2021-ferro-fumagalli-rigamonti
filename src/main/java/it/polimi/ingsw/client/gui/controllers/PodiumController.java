package it.polimi.ingsw.client.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class PodiumController extends GenericController {

    @FXML private Label place1, place2, place3, place4;

    public void setPlace1(String text){
        place1.setText(text);
    }

    public void setPlace2(String text){
        place2.setText(text);
    }

    public void setPlace3(String text){
        place3.setText(text);
    }

    public void setPlace4(String text){
        place4.setText(text);
    }

    public void quit() {
        showAlert(Alert.AlertType.INFORMATION, "Quit",
                "Thanks to have played Master of Renaissance!", "");
        System.exit(0);
    }
}
