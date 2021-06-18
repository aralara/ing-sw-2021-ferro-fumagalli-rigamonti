package it.polimi.ingsw.client.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;

/**
 * Handles methods relative to fxml Card file
 */
public class CardController extends GenericController{

    @FXML private ImageView cardToPlace_imageView;

    /**
     * Handles drag detected event of the card
     * @param mouseEvent Mouse event
     */
    public void handleDragDetected(MouseEvent mouseEvent) {
        Dragboard dragboard = cardToPlace_imageView.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putImage(cardToPlace_imageView.getImage());
        dragboard.setContent(clipboardContent);
        mouseEvent.consume();
    }

    /**
     * Sets cardToPlace's imageView
     * @param image Image to be set
     */
    public void setImage(Image image){
        cardToPlace_imageView.setImage(image);
    }
}
