package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.SceneNames;
import it.polimi.ingsw.server.model.storage.ResourceType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class FirstPhaseController extends GenericController {

    private List<Integer> leaderIndexesToDiscard;
    private boolean anotherRes=false;

    @FXML private ImageView leader1_imageView, leader2_imageView, leader3_imageView, leader4_imageView;
    @FXML private Button discard1_button, discard2_button, discard3_button, discard4_button;
    @FXML private Label chooseResources_label, coin_label, servant_label, shield_label, stone_label;

    public Label getChooseResources_label(){ //TODO: se ricevo 1/2 risorse cambio titolo di conseguenza
        return chooseResources_label;
    }

    public void setChooseResources_label(String title){
        this.chooseResources_label.setText(title);
    }

    private void discard(Button button, int index){
        button.setDisable(true);
        addLeaderToDiscard(index);
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

    private void takeResource(Label label, ResourceType resourceType){
        PlayerBoardController pbc = (PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD);

        int labelQuantity = pbc.getQuantity(resourceType);
        pbc.setQuantity(resourceType, ++labelQuantity);

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
        takeResource(coin_label,ResourceType.COIN);
    }

    public void takeServant() {
        takeResource(servant_label,ResourceType.SERVANT);
    }

    public void takeShield() {
        takeResource(shield_label,ResourceType.SHIELD);
    }

    public void takeStone() {
        takeResource(stone_label,ResourceType.STONE);
    }

    public void setLeaders(List<Integer> idLeaders){
        leader1_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/"
                + idLeaders.get(0)+".png")));
        leader2_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/"
                + idLeaders.get(1)+".png")));
        leader3_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/"
                + idLeaders.get(2)+".png")));
        leader4_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/"
                + idLeaders.get(3)+".png")));
    }

    public void enableLabels(){ //TODO: se deve scegliere 2 risorse
        coin_label.setVisible(true);
        servant_label.setVisible(true);
        shield_label.setVisible(true);
        stone_label.setVisible(true);
        anotherRes=true;
    }

    private void addLeaderToDiscard(int buttonPosition){
        if(leaderIndexesToDiscard == null)
            leaderIndexesToDiscard = new ArrayList<>();
        leaderIndexesToDiscard.add(buttonPosition-1);
        if(leaderIndexesToDiscard.size()==2)
            getGUI().sendLeaderCardDiscardMessage(leaderIndexesToDiscard);
    }
}
