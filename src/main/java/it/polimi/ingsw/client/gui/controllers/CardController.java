package it.polimi.ingsw.client.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;

public class CardController extends GenericController{

    @FXML private ImageView cardToPlace_imageView;

    public void handleDragDetected(MouseEvent mouseEvent) {
        Dragboard dragboard = cardToPlace_imageView.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putImage(cardToPlace_imageView.getImage());
        dragboard.setContent(clipboardContent);
        mouseEvent.consume();
    }

    public void setImage(Image image){
        cardToPlace_imageView.setImage(image);
    }
}
