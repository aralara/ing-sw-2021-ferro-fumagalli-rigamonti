package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.SceneNames;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public class FirstPhaseController extends GenericController {

    private boolean anotherRes=false;

    @FXML private ImageView leader1_imageView, leader2_imageView, leader3_imageView, leader4_imageView;
    @FXML private Button discard1_button, discard2_button, discard3_button, discard4_button;
    @FXML private Label chooseResources_label, coin_label, servant_label, shield_label, stone_label;

    public Label getChooseResources_label(){ //TODO: se ricevo 1/2 risorse cambio titolo di conseguenza
        return chooseResources_label;
    }

    public void discard1() {
        discard1_button.setDisable(true);
        if (getGUI().addLeaderToDiscard(1)) {
            getGUIApplication().closePopUpStage();
            getGUIApplication().setActiveScene(SceneNames.RESOURCE_CHOICE_MENU); //TODO: da aprire solo se richiesto (controllo da spostare)
        }
    }

    public void discard2() {
        discard2_button.setDisable(true);
        if(getGUI().addLeaderToDiscard(2)) {
            getGUIApplication().closePopUpStage();
            getGUIApplication().setActiveScene(SceneNames.RESOURCE_CHOICE_MENU); //TODO: da aprire solo se richiesto (controllo da spostare)
        }
    }

    public void discard3() {
        discard3_button.setDisable(true);
        if(getGUI().addLeaderToDiscard(3)) {
            getGUIApplication().closePopUpStage();
            getGUIApplication().setActiveScene(SceneNames.RESOURCE_CHOICE_MENU); //TODO: da aprire solo se richiesto (controllo da spostare)
        }
    }

    public void discard4() {
        discard4_button.setDisable(true);
        if(getGUI().addLeaderToDiscard(4)) {
            getGUIApplication().closePopUpStage();
            getGUIApplication().setActiveScene(SceneNames.RESOURCE_CHOICE_MENU); //TODO: da aprire solo se richiesto (controllo da spostare)
        }
    }

    public void takeCoin() {
        PlayerBoardController pbc = (PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD);
        int labelQuantity = pbc.getCoinQuantity();
        pbc.setCoinQuantity(++labelQuantity);

        if(coin_label.isVisible() && anotherRes){
            int quantity = coin_label.getText().charAt(2)-48;
            coin_label.setText("x "+(quantity+1));
            anotherRes=false;
        }
        else getGUIApplication().closePopUpStage();
    }

    public void takeServant() {
        PlayerBoardController pbc = (PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD);
        int labelQuantity = pbc.getServantQuantity();
        pbc.setServantQuantity(++labelQuantity);

        if(servant_label.isVisible() && anotherRes){
            int quantity = servant_label.getText().charAt(2)-48;
            servant_label.setText("x "+(quantity+1));
            anotherRes=false;
        }
        else getGUIApplication().closePopUpStage();
    }

    public void takeShield() {
        PlayerBoardController pbc = (PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD);
        int labelQuantity = pbc.getShieldQuantity();
        pbc.setShieldQuantity(++labelQuantity);

        if(shield_label.isVisible() && anotherRes){
            int quantity = shield_label.getText().charAt(2)-48;
            shield_label.setText("x "+(quantity+1));
            anotherRes=false;
        }
        else getGUIApplication().closePopUpStage();
    }

    public void takeStone() {
        PlayerBoardController pbc = (PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD);
        int labelQuantity = pbc.getStoneQuantity();
        pbc.setStoneQuantity(++labelQuantity);

        if(stone_label.isVisible() && anotherRes){
            int quantity = stone_label.getText().charAt(2)-48;
            stone_label.setText("x "+(quantity+1));
            anotherRes=false;
        }
        else getGUIApplication().closePopUpStage();
    }

    public void setLeaders(List<LeaderCard> leaders){
        leader1_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/"
                +leaders.get(0).getID()+".png")));
        leader2_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/"
                +leaders.get(1).getID()+".png")));
        leader3_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/"
                +leaders.get(2).getID()+".png")));
        leader4_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/"
                +leaders.get(3).getID()+".png")));
    }

    public void enableLabels(){ //TODO: se deve scegliere 2 risorse
        coin_label.setVisible(true);
        servant_label.setVisible(true);
        shield_label.setVisible(true);
        stone_label.setVisible(true);
        anotherRes=true;
    }
}
