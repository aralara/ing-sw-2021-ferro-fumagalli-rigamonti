package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.GUIApplication;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class GenericController {
    private GUIApplication guiApplication;

    public void showAlert(Alert.AlertType alertType, String title, String header, String content){
        Alert alert = guiApplication.getAlert();
        if(alert.isShowing()) {
            Image image = new Image(getClass().getResourceAsStream("/imgs/icon_inkwell.png"));
            alert = new Alert(Alert.AlertType.NONE);
            ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(image);
        }
        alert.setAlertType(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
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
