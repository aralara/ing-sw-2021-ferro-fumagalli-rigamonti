package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.SceneNames;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class ConnectionMenuController implements ControllerInterface {
    private GUI gui;

    @FXML TextField ipAddress_field;
    @FXML TextField portNumber_field;

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public void connect(ActionEvent actionEvent) {
        Alert alert = gui.getAlert();
        alert.setAlertType(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        if(ipAddress_field.getText().equals("")){ //TODO:aggiungere controlli sensati
            alert.setHeaderText("Missing field!");
            alert.setContentText("The IP address field is empty, please fill it");
            alert.showAndWait();
        }
        else if(portNumber_field.getText().equals("")){
            alert.setHeaderText("Missing field!");
            alert.setContentText("The port number field is empty, please fill it");
            alert.showAndWait();
        }
        else if(!ipAddress_field.getText().matches("[0-9.]*")){
            alert.setHeaderText("Invalid field!");
            alert.setContentText("The IP address field is invalid, please complete it correctly");
            alert.showAndWait();
        }
        else if(!portNumber_field.getText().matches("[0-9]*")){
            alert.setHeaderText("Invalid field!");
            alert.setContentText("The port number field is invalid, please complete it correctly");
            alert.showAndWait();
        }
        else{
            gui.setActiveScene(SceneNames.NICKNAME_MENU);
        }
    }

    public void quit(ActionEvent actionEvent) {
        Alert alert = gui.getAlert();
        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setTitle("Quit");
        alert.setHeaderText("Thanks to have played Master of Renaissance!");
        alert.showAndWait();
        System.exit(0);
    }
}
