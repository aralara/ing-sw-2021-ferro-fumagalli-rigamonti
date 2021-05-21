package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GraphicalGUI;
import it.polimi.ingsw.client.gui.SceneNames;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class SetupController extends GenericController {

    private List<Label> notifyPlayer_labels;

    @FXML TextField ipAddress_field;
    @FXML TextField portNumber_field;
    @FXML TextField nickname_field;
    @FXML Label notifyPlayer1_label;
    @FXML Label notifyPlayer2_label;
    @FXML Label notifyPlayer3_label;

    public void connect(ActionEvent actionEvent) {
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
            if(GraphicalGUI.getGUI().connect(ipAddress_field.getText(), Integer.parseInt(portNumber_field.getText()))) {
                getGraphicalGUI().setActiveScene(SceneNames.NICKNAME_MENU);
            }
            else {
                showAlert(Alert.AlertType.ERROR, "Error", "Connection problem",
                        "Failed connecting to the server, please try again");
            }
        }
    }

    public void quit(ActionEvent actionEvent) {
        showAlert(Alert.AlertType.INFORMATION, "Quit",
                "Thanks to have played Master of Renaissance!", "");
        System.exit(0);
    }

    public void sendNickname(ActionEvent actionEvent) {

        if(nickname_field.getText().equals("")){
            showAlert(Alert.AlertType.ERROR, "Error", "Missing field!",
                    "The nickname field is empty, please fill it");
        }
        else if(nickname_field.getText().length() >20){
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid field!",
                    "The nickname field is longer than 20 character,\nplease complete it correctly");
        }
        else{
            //GraphicalGUI.getGUI().getMessageHandler().sendMessage(new ConnectionMessage(nickname_field.getText())); TODO: lancia eccezione
            //TODO: switchare in base a partita già esistente o meno
            getGraphicalGUI().setActiveScene(SceneNames.GAME_MODE_MENU);
            //setWaitingPlayerScene();
        }
    }

    public void startSingleGame(ActionEvent actionEvent) {
        getGraphicalGUI().setActiveScene(SceneNames.SINGLE_PLAYER_MENU);
    }

    public void startMultiGame(ActionEvent actionEvent) {
        getGraphicalGUI().setActiveScene(SceneNames.MULTI_PLAYER_MENU);
    }

    public void startOnlineGame(ActionEvent actionEvent) { //TODO: implementare singlegame online
        getGraphicalGUI().setActiveScene(SceneNames.PLAYER_BOARD);
    }

    public void startOfflineGame(ActionEvent actionEvent) { //TODO: implementare singlegame offline
        getGraphicalGUI().setActiveScene(SceneNames.PLAYER_BOARD);
    }

    public void choose2Players(ActionEvent actionEvent) { //TODO: settare un solo avversario (#giocatori = 2)
        setWaitingPlayerScene();
    }

    public void choose3Players(ActionEvent actionEvent) { //TODO: settare due avversari (#giocatori = 3)
        setWaitingPlayerScene();
    }

    public void choose4Players(ActionEvent actionEvent) { //TODO: settare tre avversari (#giocatori = 4)
        setWaitingPlayerScene();
    }

    private void setWaitingPlayerScene(){
        notifyPlayer_labels = new ArrayList<>();
        notifyPlayer_labels.add(notifyPlayer1_label);
        notifyPlayer_labels.add(notifyPlayer2_label);
        notifyPlayer_labels.add(notifyPlayer3_label);
        getGraphicalGUI().setActiveScene(SceneNames.MULTI_PLAYER_WAITING);
    }

    public void notifyNewPlayer(String nickname){ //TODO: chiamare quando si aggiunge un giocatore
        notifyPlayer_labels = new ArrayList<>(0); //TODO: :(
        notifyPlayer_labels.add(notifyPlayer1_label);
        notifyPlayer_labels.add(notifyPlayer2_label);
        notifyPlayer_labels.add(notifyPlayer3_label);
        for(Label label : notifyPlayer_labels){
            if(label.getText().equals("")) {
                label.setText("The player " + nickname + " has joined the game!");
                label.setVisible(true);
                break;
            }
        }
    }

    public void temp(){ //TODO: da spostare nel metodo appropriato che verrà richiamato all'arrivo di un messaggio di notifica nuovo giocatore
        GenericController controller = getGraphicalGUI().getController(SceneNames.MULTI_PLAYER_WAITING);
        ((SetupController)controller).notifyNewPlayer("lara");
    }

    public void setPlayerBoardScene(){ //TODO: da chiamare quando si è raggiunto il numero di player
        getGraphicalGUI().setActiveScene(SceneNames.PLAYER_BOARD);
    }
}
