package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GraphicalGUI;
import javafx.scene.control.Alert;

public class GenericController {
    private GraphicalGUI graphicalGUI;

    public void setGraphicalGUI(GraphicalGUI graphicalGUI) {
        this.graphicalGUI = graphicalGUI;
    }

    public void showAlert(Alert.AlertType alertType, String title, String header, String content){
        Alert alert = graphicalGUI.getAlert();
        alert.setAlertType(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public GraphicalGUI getGraphicalGUI(){
        return graphicalGUI;
    }
}
