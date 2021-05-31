package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.SceneNames;
import it.polimi.ingsw.utils.messages.client.ConnectionMessage;
import javafx.fxml.FXML;;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class SetupController extends GenericController {

    @FXML private TextField ipAddress_field, portNumber_field, nickname_field;
    @FXML private ProgressIndicator connecting_progressIndicator, waitingNickname_progressIndicator;
    @FXML private Button playOnline_button, playOffline_button, connect_button, quit_button, confirm_button;
    @FXML private Label nickname_label, notifyPlayers_label;
    @FXML private ProgressBar loading_bar;

    public TextField getIpAddress_field() {
        return ipAddress_field;
    }

    public TextField getPortNumber_field() {
        return portNumber_field;
    }

    public TextField getNickname_field() {
        return nickname_field;
    }

    public ProgressIndicator getConnecting_progressIndicator() {
        return connecting_progressIndicator;
    }

    public ProgressIndicator getWaitingNickname_progressIndicator() {
        return waitingNickname_progressIndicator;
    }

    public Button getConnect_button() {
        return connect_button;
    }

    public Button getQuit_button() {
        return quit_button;
    }

    public Button getConfirm_button() {
        return confirm_button;
    }

    public Label getNickname_label() {
        return nickname_label;
    }

    public void playOnline() {
        playOnline_button.setVisible(false);
        playOffline_button.setVisible(false);
        loading_bar.setVisible(true);
        getGUIApplication().setActiveScene(SceneNames.CONNECTION_MENU);
    }

    public void playOffline() {
        //TODO: implementare singlegame offline
        playOnline_button.setVisible(false);
        playOffline_button.setVisible(false);
        loading_bar.setVisible(true);
    }

    public void connect() {  //TODO: decidere se lasciare ip e porta vuota (molto probabilmente sì)
        /*if(ipAddress_field.getText().equals("")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Missing field!",
                    "The IP address field is empty, please fill it");
        }
        else if(portNumber_field.getText().equals("")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Missing field!",
                    "The port number field is empty, please fill it");
        }*/
        if(!ipAddress_field.getText().matches("[0-9.]*")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid field!",
                    "The IP address field is invalid, please complete it correctly");
        }
        else if(!portNumber_field.getText().matches("[0-9]*") || portNumber_field.getLength()>4) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid field!",
                    "The port number field is invalid, please complete it correctly");
        }
        else {
            getGUIApplication().changeConnectionMenuStatus();
            //if(getGUI().connect(ipAddress_field.getText(), Integer.parseInt(portNumber_field.getText()))) {
            if(getGUI().connect(ipAddress_field.getText(), 1919)) {
                getGUIApplication().setActiveScene(SceneNames.NICKNAME_MENU);
            }
            else {
                showAlert(Alert.AlertType.ERROR, "Error", "Connection problem",
                        "Failed connecting to the server, please try again");
                getGUIApplication().changeConnectionMenuStatus();
            }
        }
    }

    public void keyConnect(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            connect();
        }
    }

    public void quit() {
        showAlert(Alert.AlertType.INFORMATION, "Quit",
                "Thanks to have played Master of Renaissance!", "");
        System.exit(0);
    }

    public void sendNickname() {

        if(nickname_field.getText().equals("")){
            showAlert(Alert.AlertType.ERROR, "Error", "Missing field!",
                    "The nickname field is empty, please fill it");
        }
        else if(nickname_field.getText().length() >20){
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid field!",
                    "The nickname field is longer than 20 character,\nplease complete it correctly");
        }
        else{
            getGUIApplication().changeNicknameMenuStatus();
            getGUIApplication().getGUI().sendNickname(nickname_field.getText());
        }
    }

    public void keyNickname(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            sendNickname();
        }
    }

    public void choose1Players() {
        getGUI().setLobbySize(1);
    }

    public void choose2Players() {
        getGUI().setLobbySize(2);
    }

    public void choose3Players() {
        getGUI().setLobbySize(3);
    }

    public void choose4Players() {
        getGUI().setLobbySize(4);
    }

    public void notifyNewPlayer(String nickname){
        String text = notifyPlayers_label.getText();
        notifyPlayers_label.setText(text + nickname + (nickname.length()>=16 ? "\n" : " ") + "has joined the game!\n");
        notifyPlayers_label.setVisible(true);
    }
}
