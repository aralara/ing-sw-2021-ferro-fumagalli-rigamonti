package it.polimi.ingsw.client.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class DecksBoardController extends GenericController {

    @FXML ImageView deckG3_imageView, deckB3_imageView, deckY3_imageView, deckP3_imageView,
            deckG2_imageView, deckB2_imageView, deckY2_imageView, deckP2_imageView,
            deckG1_imageView, deckB1_imageView, deckY1_imageView, deckP1_imageView, selectedDevCard_imageView;

    public void goBack() {
        selectedDevCard_imageView.setImage(null);
        getGUIApplication().closeSecondStage();
    }

    public void clickedG3() {
        selectedDevCard_imageView.setImage(deckG3_imageView.getImage());
    }

    public void clickedB3() {
        selectedDevCard_imageView.setImage(deckB3_imageView.getImage());
    }

    public void clickedY3() {
        selectedDevCard_imageView.setImage(deckY3_imageView.getImage());
    }

    public void clickedP3() {
        selectedDevCard_imageView.setImage(deckP3_imageView.getImage());
    }

    public void clickedG2() {
        selectedDevCard_imageView.setImage(deckG2_imageView.getImage());
    }

    public void clickedB2() {
        selectedDevCard_imageView.setImage(deckB2_imageView.getImage());
    }

    public void clickedY2() {
        selectedDevCard_imageView.setImage(deckY2_imageView.getImage());
    }

    public void clickedP2() {
        selectedDevCard_imageView.setImage(deckP2_imageView.getImage());
    }

    public void clickedG1() {
        selectedDevCard_imageView.setImage(deckG1_imageView.getImage());
    }

    public void clickedB1() {
        selectedDevCard_imageView.setImage(deckB1_imageView.getImage());
    }

    public void clickedY1() {
        selectedDevCard_imageView.setImage(deckY1_imageView.getImage());
    }

    public void clickedP1() {
        selectedDevCard_imageView.setImage(deckP1_imageView.getImage());
    }

    public void buyCard() {
    }
}
