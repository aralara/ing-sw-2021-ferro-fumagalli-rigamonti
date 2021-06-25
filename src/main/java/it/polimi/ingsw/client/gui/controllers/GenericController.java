package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.gui.GUIApplication;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * Defines a class to handle common parts of all controllers
 */
public class GenericController {
    private GUIApplication guiApplication;

    /**
     * Shows alert with the specifics given by parameters
     * @param alertType Type of the alert
     * @param title Title of the alert
     * @param header Header of the alert
     * @param content Content of the alert
     */
    public void showAlert(Alert.AlertType alertType, String title, String header, String content){
        Alert alert = guiApplication.getAlert();
        if(alert.isShowing()) {
            Image icon = new Image(getClass().getResourceAsStream("/imgs/icon_inkwell.png"));
            alert = new Alert(Alert.AlertType.NONE);
            ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(icon);
        }
        alert.setAlertType(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    /**
     * Shows an alert with the specifics given by parameters when Lorenzo picks a card
     * @param title Title of the alert
     * @param header Header of the alert
     * @param content Content of the alert
     */
    public void showLorenzoAlert(String title, String header,
                                 String content, int cardID) {
        Alert alert = guiApplication.getLorenzoAlert();
        ImageView alertImage = new ImageView(
                new Image(getClass().getResourceAsStream("/imgs/lorenzo/" + cardID + ".png")));
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.setGraphic(alertImage);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    /**
     * Gets the gui attribute
     * @return Returns gui
     */
    public GUI getGUI(){
        return guiApplication.getGUI();
    }

    /**
     * Gets the scene attribute
     * @return Returns scene
     */
    public GUIApplication getGUIApplication(){
        return guiApplication;
    }

    /**
     * Sets the guiApplication attribute
     * @param guiApplication New attribute value
     */
    public void setGUIApplication(GUIApplication guiApplication) {
        this.guiApplication = guiApplication;
    }

}
