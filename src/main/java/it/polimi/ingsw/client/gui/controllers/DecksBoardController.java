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
    }

    public void clickedY3() {
    }

    public void clickedP3() {
    }

    public void clickedG2() {
    }

    public void clickedB2() {
    }

    public void clickedY2() {
    }

    public void clickedP2() {
    }

    public void clickedG1() {
    }

    public void clickedB1() {
    }

    public void clickedY1() {
    }

    public void clickedP1() {
    }

    public void buyCard() {
    }
}
