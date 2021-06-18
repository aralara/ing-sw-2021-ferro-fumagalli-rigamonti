package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.SceneNames;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

/**
 * Handles methods relative to fxml Ranking file
 */
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
                "Thank you for having played Master of Renaissance!", "");
        System.exit(0);
    }

    /**
     * Goes back to the first scene to start a new game
     */
    public void startNewGame() {
        SetupController sc =((SetupController)getGUIApplication().getController(SceneNames.LOADING));
        getGUI().setNumberOfPlayers(1);
        sc.showLoadingBar(false);
        getGUIApplication().changeConnectionMenuStatus();
        ((SetupController)getGUIApplication().getController(SceneNames.CONNECTION_MENU)).
                getIpAddress_field().setText("");
        ((SetupController)getGUIApplication().getController(SceneNames.CONNECTION_MENU)).
                getPortNumber_field().setText("");
        ((SetupController)getGUIApplication().getController(SceneNames.CONNECTION_MENU)).
                getConnection_pane().requestFocus();
        getGUIApplication().changeNicknameMenuStatus();
        ((SetupController)getGUIApplication().getController(SceneNames.NICKNAME_MENU)).
                getNickname_field().setText("");
        ((SetupController)getGUIApplication().getController(SceneNames.NICKNAME_MENU)).
                getNickname_label().requestFocus();
        getGUIApplication().setActiveScene(SceneNames.LOADING); //TODO: ripristinare socket
    }
}
