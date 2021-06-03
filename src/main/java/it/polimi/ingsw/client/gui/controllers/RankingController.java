package it.polimi.ingsw.client.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class RankingController extends GenericController {

    @FXML private Label names_label, scores_label, pt_label;

    /**
     * Sets the content of names_label
     * @param text Text to show in the names_label
     */
    public void setNames_label(String text){
        if(names_label==null)
            names_label = new Label("");
        String previousText = names_label.getText();
        names_label.setText(previousText+text+"\n");
    }

    /**
     * Sets the content of scores_label
     * @param text Text to show in the scores_label
     */
    public void setScores_label(String text){
        if(scores_label==null)
            scores_label = new Label("");
        if(pt_label==null)
            pt_label = new Label("");
        String previousText = scores_label.getText();
        scores_label.setText(previousText+text+"\n");
        String previousPt = pt_label.getText();
        pt_label.setText(previousPt+"pt\n");
    }

    /**
     * Quits the game closing the program
     */
    public void quit() {
        showAlert(Alert.AlertType.INFORMATION, "Quit",
                "Thanks to have played Master of Renaissance!", "");
        System.exit(0);
    }
}
