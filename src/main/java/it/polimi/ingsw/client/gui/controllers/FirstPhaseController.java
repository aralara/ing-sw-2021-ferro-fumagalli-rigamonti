package it.polimi.ingsw.client.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class FirstPhaseController extends GenericController {

    private List<Integer> leaderIndexesToDiscard;

    @FXML private ImageView leader1_imageView, leader2_imageView, leader3_imageView, leader4_imageView;
    @FXML private Button discard1_button, discard2_button, discard3_button, discard4_button;

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

    private void addLeaderToDiscard(int buttonPosition){
        if(leaderIndexesToDiscard == null)
            leaderIndexesToDiscard = new ArrayList<>();
        leaderIndexesToDiscard.add(buttonPosition-1);
        if(leaderIndexesToDiscard.size()==2)
            getGUI().sendLeaderCardDiscardMessage(leaderIndexesToDiscard);
    }
}
