package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.GUIApplication;
import javafx.scene.control.Alert;

public class GenericController {
    private GUIApplication guiApplication;

    public void showAlert(Alert.AlertType alertType, String title, String header, String content){
        Alert alert = guiApplication.getAlert();
        alert.setAlertType(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public GUI getGUI(){
        return guiApplication.getGUI();
    }

    public GUIApplication getGUIApplication(){
        return guiApplication;
    }

    public void setGUIApplication(GUIApplication guiApplication) {
        this.guiApplication = guiApplication;
    }

}
