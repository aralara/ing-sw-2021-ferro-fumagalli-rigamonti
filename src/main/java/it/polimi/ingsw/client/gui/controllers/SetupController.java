package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.SceneNames;
import it.polimi.ingsw.server.saves.SaveInteractions;
import it.polimi.ingsw.utils.messages.client.ConnectionMessage;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.List;

/**
 * Handles methods relative to fxml ConnectionMenu, GameModeMenu, Loading, MultiPlayerWaiting, NicknameMenu, Saves files
 */
public class SetupController extends GenericController {

    private boolean isLocal=false;

    @FXML private TextField ipAddress_field, portNumber_field, nickname_field;
    @FXML private ProgressIndicator connecting_progressIndicator, waitingNickname_progressIndicator;
    @FXML private Button playOnline_button, playOffline_button, connect_button, confirm_button,
            load_button, delete_button;
    @FXML private Label nickname_label, notifyPlayers_label;
    @FXML private ProgressBar loading_bar;
    @FXML private Pane connection_pane;
    @FXML private ComboBox<String> files_comboBox;

    /**
     * Sets the isLocal attribute
     * @param value New attribute value
     */
    public void setIsLocal(boolean value){
        isLocal = value;
    }

    /**
     * Gets the isLocal attribute
     * @return Returns isLocal
     */
    public boolean getIsLocal() {
        return isLocal;
    }

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
        ((SetupController)getGUIApplication().getController(SceneNames.NICKNAME_MENU)).setIsLocal(false);
        showLoadingBar(true);
        getGUIApplication().setActiveScene(SceneNames.CONNECTION_MENU);
    }

    /**
     * Sets the number of players and shows the NicknameMenu scene
     */
    public void playOffline() {
        ((SetupController)getGUIApplication().getController(SceneNames.NICKNAME_MENU)).setIsLocal(true);
        getGUI().setNumberOfPlayers(1);
        showLoadingBar(true);
        getGUIApplication().setActiveScene(SceneNames.NICKNAME_MENU);
    }

    /**
     * Controls if port and ip address are correct and shows the NicknameMenu scene, otherwise shows an alert message
     */
    public void connect() {
        if(ipAddress_field.getText().equals("")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Missing field!",
                    "The IP address field is empty, please fill it");
        }
        else if(portNumber_field.getText().equals("")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Missing field!",
                    "The port number field is empty, please fill it");
        }
        else if(!ipAddress_field.getText().matches("[0-9.]*")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid field!",
                    "The IP address field is invalid, please complete it correctly");
        }
        else if(!portNumber_field.getText().matches("[0-9]*") || portNumber_field.getLength()>4) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid field!",
                    "The port number field is invalid, please complete it correctly");
        }
        else {
            getGUIApplication().changeConnectionMenuStatus();
            if(getGUI().connect(ipAddress_field.getText(), Integer.parseInt(portNumber_field.getText()))) {
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
     * @param keyEvent Key event
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
            getGUI().setNickname(nickname_field.getText());
            ((PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD)).
                    setPlayer_label(nickname_field.getText());
            if(!isLocal) {
                getGUI().getMessageHandler().sendClientMessage(new ConnectionMessage(nickname_field.getText()));
            }
            else {
                getGUIApplication().setActiveScene(SceneNames.LOADING);
                try {
                    getGUIApplication().getGUI().localSetup();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Calls sendNickname method if ENTER key is pressed
     * @param keyEvent Key event
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

    /**
     * Shows loading bar according to the value given by parameter
     * @param toShow True to show the bar, false to show buttons
     */
    public void showLoadingBar(boolean toShow){
        playOnline_button.setVisible(!toShow);
        playOffline_button.setVisible(!toShow);
        loading_bar.setVisible(toShow);
    }

    /**
     * Avoids to load or delete file saves
     */
    public void skip() {
        getGUIApplication().closeSecondStage();
        load_button.setDisable(true);
        delete_button.setDisable(true);
        getGUI().sendSaveInteractionMessage(null, SaveInteractions.NO_ACTION);
    }

    /**
     * Calls sendSaveInteractionMessage GUI method to load the selected file save
     */
    public void load() {
        getGUIApplication().closeSecondStage();
        load_button.setDisable(true);
        delete_button.setDisable(true);
        getGUI().sendSaveInteractionMessage(files_comboBox.getValue(), SaveInteractions.OPEN_SAVE);
    }

    /**
     * Calls sendSaveInteractionMessage GUI method to delete the selected file save
     */
    public void delete() {
        getGUIApplication().closeSecondStage();
        load_button.setDisable(true);
        delete_button.setDisable(true);
        getGUI().sendSaveInteractionMessage(files_comboBox.getValue(), SaveInteractions.DELETE_SAVE);
    }

    /**
     * Fills files_comboBox with items which names are given by parameter
     * @param files List of items to load
     */
    public void fillComboBox(List<String> files){
        files_comboBox.setItems(FXCollections.observableArrayList(files));
        files_comboBox.setStyle("-fx-font: 16px \"Pristina\";");
    }

    /**
     * Enables buttons when an item is selected from the comboBox
     */
    public void fileSelected() {
        if(files_comboBox.getValue()!=null && !files_comboBox.getValue().equals("")){
            load_button.setDisable(false);
            delete_button.setDisable(false);
        }
        else {
            load_button.setDisable(true);
            delete_button.setDisable(true);
        }
    }
}
