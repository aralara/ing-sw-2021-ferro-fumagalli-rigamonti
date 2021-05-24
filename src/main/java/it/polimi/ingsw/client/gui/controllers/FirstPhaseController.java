package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.SceneNames;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.model.storage.ResourceType;
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

    private void discard(Button button, int index){
        button.setDisable(true);
        getGUI().addLeaderToDiscard(index);
    }

    public void discard1() {
        discard(discard1_button, 1);
    }

    public void discard2() {
        discard(discard2_button, 2);
    }

    public void discard3() {
        discard(discard3_button, 3);
    }

    public void discard4() {
        discard(discard4_button, 4);
    }

    private void finishEqualizeAction(Label label, ResourceType resourceType){
        PlayerBoardController pbc = (PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD);
        int labelQuantity = pbc.getCoinQuantity(); //TODO: trasforma in getQuantity generico specificando il paramentro
        pbc.setCoinQuantity(++labelQuantity);

        /*int labelQuantity = pbc.getQuantity(resourceType);
        pbc.setQuantity(resourceType, ++labelQuantity);*/

        if(label.isVisible() && anotherRes){
            int quantity = label.getText().charAt(2)-48;
            label.setText("x "+(quantity+1));
            anotherRes=false;
        }
        else {
            pbc.setIsResToPlace(true);
            getGUIApplication().closePopUpStage();
        }
    }

    public void takeCoin() {
        PlayerBoardController pbc = (PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD);
        int labelQuantity = pbc.getCoinQuantity(); //TODO: trasforma in getQuantity generico specificando il paramentro
        pbc.setCoinQuantity(++labelQuantity);

        if(coin_label.isVisible() && anotherRes){
            int quantity = coin_label.getText().charAt(2)-48;
            coin_label.setText("x "+(quantity+1));
            anotherRes=false;
        }
        else {
            pbc.setIsResToPlace(true);
            getGUIApplication().closePopUpStage();
        }
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
        else {
            pbc.setIsResToPlace(true);
            getGUIApplication().closePopUpStage();
        }
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
        else {
            pbc.setIsResToPlace(true);
            getGUIApplication().closePopUpStage();
        }
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
        else {
            pbc.setIsResToPlace(true);
            getGUIApplication().closePopUpStage();
        }
    }

    public void setLeaders(List<Integer> leaders){
        leader1_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/"
                +leaders.get(0)+".png")));
        leader2_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/"
                +leaders.get(1)+".png")));
        leader3_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/"
                +leaders.get(2)+".png")));
        leader4_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/"
                +leaders.get(3)+".png")));
    }

    public void enableLabels(){ //TODO: se deve scegliere 2 risorse
        coin_label.setVisible(true);
        servant_label.setVisible(true);
        shield_label.setVisible(true);
        stone_label.setVisible(true);
        anotherRes=true;
    }
}
