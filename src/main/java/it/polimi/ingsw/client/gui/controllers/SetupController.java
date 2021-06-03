package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.SceneNames;
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
    @FXML private Button playOnline_button, playOffline_button, connect_button, confirm_button;
    @FXML private Label nickname_label, notifyPlayers_label;
    @FXML private ProgressBar loading_bar;

    /**
     * Gets the ipAddress_field attribute
     * @return Returns ipAddress_field
     */
    public TextField getIpAddress_field() {
        return ipAddress_field;
    }

    /**
     * Gets the portNumber_field attribute
     * @return Returns portNumber_field
     */
    public TextField getPortNumber_field() {
        return portNumber_field;
    }

    /**
     * Gets the nickname_field attribute
     * @return Returns nickname_field
     */
    public TextField getNickname_field() {
        return nickname_field;
    }

    /**
     * Gets the connecting_progressIndicator attribute
     * @return Returns connecting_progressIndicator
     */
    public ProgressIndicator getConnecting_progressIndicator() {
        return connecting_progressIndicator;
    }

    /**
     * Gets the waitingNickname_progressIndicator attribute
     * @return Returns waitingNickname_progressIndicator
     */
    public ProgressIndicator getWaitingNickname_progressIndicator() {
        return waitingNickname_progressIndicator;
    }

    /**
     * Gets the connect_button attribute
     * @return Returns connect_button
     */
    public Button getConnect_button() {
        return connect_button;
    }

    /**
     * Gets the confirm_button attribute
     * @return Returns confirm_button
     */
    public Button getConfirm_button() {
        return confirm_button;
    }

    /**
     * Gets the nickname_label attribute
     * @return Returns nickname_label
     */
    public Label getNickname_label() {
        return nickname_label;
    }

    /**
     * Shows the ConnectionMenu scene
     */
    public void playOnline() {
        playOnline_button.setVisible(false);
        playOffline_button.setVisible(false);
        loading_bar.setVisible(true);
        getGUIApplication().setActiveScene(SceneNames.CONNECTION_MENU);
    }

    /**
     * TODO: fare quando si sarà implementato il metodo
     */
    public void playOffline() {
        //TODO: implementare singlegame offline
        playOnline_button.setVisible(false);
        playOffline_button.setVisible(false);
        loading_bar.setVisible(true);
    }

    /**
     * Controls if port and ip address are correct and shows the NicknameMenu scene, otherwise shows an alert message
     */
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

    /**
     * Calls connect method if ENTER key is pressed
     * @param keyEvent
     */
    public void keyConnect(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            connect();
        }
    }

    /**
     * Sends a message to the server with the chosen nickname
     */
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

    /**
     * Calls sendNickname method if ENTER key is pressed
     * @param keyEvent
     */
    public void keyNickname(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            sendNickname();
        }
    }

    /**
     * Sets 1 as size of the lobby
     */
    public void choose1Players() {
        getGUI().setLobbySize(1);
    }

    /**
     * Sets 2 as size of the lobby
     */
    public void choose2Players() {
        getGUI().setLobbySize(2);
    }

    /**
     * Sets 3 as size of the lobby
     */
    public void choose3Players() {
        getGUI().setLobbySize(3);
    }

    /**
     * Sets 4 as size of the lobby
     */
    public void choose4Players() {
        getGUI().setLobbySize(4);
    }

    /**
     * Updates the label showing the nickname given by parameter
     * @param nickname Nickname to show
     */
    public void notifyNewPlayer(String nickname){
        String text = notifyPlayers_label.getText();
        notifyPlayers_label.setText(text + nickname + (nickname.length()>=16 ? "\n" : " ") + "has joined the game!\n");
        notifyPlayers_label.setVisible(true);
    }
}
