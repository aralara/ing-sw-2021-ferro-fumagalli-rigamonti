package it.polimi.ingsw.client.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ConnectionMenuController {

    Alert alert;

    @FXML
    TextField ipAddress_field;
    @FXML
    TextField portNumber_field;

    public ConnectionMenuController(){
        alert = new Alert(Alert.AlertType.NONE);
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons()
                .add(new Image(getClass().getResourceAsStream("/imgs/logo.png"))); //TODO: metodo x settare tutte le icon?
    }

    public void connect(ActionEvent actionEvent) {
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
            //TODO:cambia schermata
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setTitle("OK");
            alert.setHeaderText("Master of Renaissance starts");
            alert.showAndWait();
            System.exit(0);
        }
    }

    public void quit(ActionEvent actionEvent) {
        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setTitle("Quit");
        alert.setHeaderText("Thanks to have played Master of Renaissance!");
        alert.showAndWait();
        System.exit(0);
    }
}
