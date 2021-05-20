package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GraphicalGUI;
import it.polimi.ingsw.client.gui.SceneNames;
import it.polimi.ingsw.server.Server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class SetupController implements ControllerInterface {
    private GraphicalGUI graphicalGUI;
    private List<Label> notifyPlayer_labels;

    @FXML TextField ipAddress_field;
    @FXML TextField portNumber_field;
    @FXML TextField nickname_field;
    @FXML Label notifyPlayer1_label;
    @FXML Label notifyPlayer2_label;
    @FXML Label notifyPlayer3_label;

    @Override
    public void setGui(GraphicalGUI graphicalGUI) {
        this.graphicalGUI = graphicalGUI;
    }

    public void connect(ActionEvent actionEvent) {
        Alert alert = graphicalGUI.getAlert();
        alert.setAlertType(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        if(ipAddress_field.getText().equals("")){
            alert.setHeaderText("Missing field!");
            alert.setContentText("The IP address field is empty, please fill it");
            alert.showAndWait();
        }
        else if(portNumber_field.getText().equals("")){
            alert.setHeaderText("Missing field!");
            alert.setContentText("The port number field is empty, please fill it");
            alert.showAndWait();
        }
        else if(!ipAddress_field.getText().matches("[0-9.]*")){
            alert.setHeaderText("Invalid field!");
            alert.setContentText("The IP address field is invalid, please complete it correctly");
            alert.showAndWait();
        }
        else if(!portNumber_field.getText().matches("[0-9]*") || portNumber_field.getLength()>4){
            alert.setHeaderText("Invalid field!");
            alert.setContentText("The port number field is invalid, please complete it correctly");
            alert.showAndWait();
        }
        else{

            if(graphicalGUI.getGUI().getMessageHandler().connect(ipAddress_field.getText(),
                    Integer.parseInt(portNumber_field.getText()))){

                graphicalGUI.setActiveScene(SceneNames.NICKNAME_MENU);
            }
            else{
                alert.setHeaderText("Connection problem");
                alert.setContentText("Failed connecting to the server, please try again");
                alert.showAndWait();
            }
        }
    }

    public void quit(ActionEvent actionEvent) {
        Alert alert = graphicalGUI.getAlert();
        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setTitle("Quit");
        alert.setHeaderText("Thanks to have played Master of Renaissance!");
        alert.showAndWait();
        System.exit(0);
    }

    public void sendNickname(ActionEvent actionEvent) {
        Alert alert = graphicalGUI.getAlert();
        alert.setAlertType(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        if(nickname_field.getText().equals("")){
            alert.setHeaderText("Missing field!");
            alert.setContentText("The nickname field is empty, please fill it");
            alert.showAndWait();
        }
        if(nickname_field.getText().length() >10){ //TODO: altrimenti nella pagina di eventuale attesa non ci starà tutta la scritta
            alert.setHeaderText("Invalid field!");
            alert.setContentText("The nickname field is longer than 10 character,\nplease complete it correctly");
            alert.showAndWait();
        }
        else{
            //TODO: switchare in base a partita già esistente o meno
            graphicalGUI.setActiveScene(SceneNames.GAME_MODE_MENU);
            //setWaitingPlayerScene();
        }
    }

    public void startSingleGame(ActionEvent actionEvent) {
        graphicalGUI.setActiveScene(SceneNames.SINGLE_PLAYER_MENU);
    }

    public void startMultiGame(ActionEvent actionEvent) {
        graphicalGUI.setActiveScene(SceneNames.MULTI_PLAYER_MENU);
    }

    public void startOnlineGame(ActionEvent actionEvent) { //TODO: implementare singlegame online
        graphicalGUI.setActiveScene(SceneNames.PLAYER_BOARD);
    }

    public void startOfflineGame(ActionEvent actionEvent) { //TODO: implementare singlegame offline
        graphicalGUI.setActiveScene(SceneNames.PLAYER_BOARD);
    }

    public void chooseOneOpponent(ActionEvent actionEvent) { //TODO: settare un solo avversario (#giocatori = 2)
        setWaitingPlayerScene();
    }

    public void chooseTwoOpponents(ActionEvent actionEvent) { //TODO: settare due avversari (#giocatori = 3)
        setWaitingPlayerScene();
    }

    public void chooseThreeOpponents(ActionEvent actionEvent) { //TODO: settare tre avversari (#giocatori = 4)
        setWaitingPlayerScene();
    }

    private void setWaitingPlayerScene(){
        notifyPlayer_labels = new ArrayList<>();
        notifyPlayer_labels.add(notifyPlayer1_label);
        notifyPlayer_labels.add(notifyPlayer2_label);
        notifyPlayer_labels.add(notifyPlayer3_label);
        graphicalGUI.setActiveScene(SceneNames.MULTI_PLAYER_WAITING);
    }

    public void notifyNewPlayer(String nickname){ //TODO: chiamare quando si aggiunge un giocatore
        if(notifyPlayer1_label.getText().equals("")){
            notifyPlayer1_label.setText("The player " + nickname + " has joined the game!");
            notifyPlayer1_label.setVisible(true);
        }
        else if(notifyPlayer2_label.getText().equals("")){
            notifyPlayer2_label.setText("The player " + nickname + " has joined the game!");
            notifyPlayer2_label.setVisible(true);
        }
        else if(notifyPlayer3_label.getText().equals("")){
            notifyPlayer3_label.setText("The player " + nickname + " has joined the game!");
            notifyPlayer3_label.setVisible(true);
        }
    }

    public void setPlayerBoardScene(){ //TODO: da chiamare quando si è raggiunto il numero di player
        graphicalGUI.setActiveScene(SceneNames.PLAYER_BOARD);
    }
}
