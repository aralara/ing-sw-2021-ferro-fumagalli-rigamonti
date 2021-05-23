package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.SceneNames;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;

import java.util.ArrayList;
import java.util.List;

public class PlayerBoardController extends GenericController {

    private List<ImageView> spaces;

    @FXML private Button activateProductions_button;
    @FXML private ImageView space1L1_imageView, space1L2_imageView, space1L3_imageView, space2L1_imageView, space2L2_imageView,
            space2L3_imageView, space3L1_imageView, space3L2_imageView, space3L3_imageView; //TODO: disabilitare dopo dragDone

    public void goToMarket(ActionEvent actionEvent) {
        getGUIApplication().setActiveScene(SceneNames.MARKET_BOARD);
    }

    public void goToDecks(ActionEvent actionEvent) {
        getGUIApplication().setActiveScene(SceneNames.DECKS_BOARD);
    }

    public void activateProductions(ActionEvent actionEvent) {
    }

    public void endTurn(ActionEvent actionEvent) {
    }

    public void activateLeaderCard(ActionEvent actionEvent) {
    }

    public void discardLeaderCard(ActionEvent actionEvent) {
    }

    public void rearrangeWarehouse(ActionEvent actionEvent) {
    }

    public void viewOpponents(ActionEvent actionEvent) {
    }

    private void handleDragOver(DragEvent dragEvent) {
        if(dragEvent.getDragboard().hasImage())
            dragEvent.acceptTransferModes(TransferMode.MOVE);
    }

    private void handleDragDropped(DragEvent dragEvent, ImageView imageView) {
        Image image = dragEvent.getDragboard().getImage();
        imageView.setImage(image);
        //TODO:inviare messaggio
        disableSpaces();
        getGUIApplication().closeCardStage();
        //TODO: vedere se l'azione va a buon fine, altrimenti rimettere giusti i parametri
    }

    public void handleDragOver1L1(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragDropped1L1(DragEvent dragEvent) {
        handleDragDropped(dragEvent, space1L1_imageView);
    }

    public void handleDragOver1L2(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragDropped1L2(DragEvent dragEvent) {
        handleDragDropped(dragEvent, space1L2_imageView);
    }

    public void handleDragOver1L3(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragDropped1L3(DragEvent dragEvent) {
        handleDragDropped(dragEvent, space1L3_imageView);
    }

    public void handleDragOver2L1(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragDropped2L1(DragEvent dragEvent) {
        handleDragDropped(dragEvent, space2L1_imageView);
    }

    public void handleDragOver2L2(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragDropped2L2(DragEvent dragEvent) {
        handleDragDropped(dragEvent, space2L2_imageView);
    }

    public void handleDragOver2L3(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragDropped2L3(DragEvent dragEvent) {
        handleDragDropped(dragEvent, space2L3_imageView);
    }

    public void handleDragOver3L1(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragDropped3L1(DragEvent dragEvent) {
        handleDragDropped(dragEvent, space3L1_imageView);
    }

    public void handleDragOver3L2(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragDropped3L2(DragEvent dragEvent) {
        handleDragDropped(dragEvent, space3L2_imageView);
    }

    public void handleDragOver3L3(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragDropped3L3(DragEvent dragEvent) {
        handleDragDropped(dragEvent, space3L3_imageView);
    }


    public void enableActivateProductionsAction(){ //TODO: settare all'inizio di ogni (proprio) turno
        activateProductions_button.setDisable(false);
    }

    public void disableActivateProductionsAction(){
        activateProductions_button.setDisable(true);
    }

    public void disableSpaces(){
        fillSpacesList();
        for(ImageView imageView : spaces)
            imageView.setDisable(true);
    }

    public void enableSpaces(){ //TODO: migliorabile
        fillSpacesList();
        for(int i=0; i<3; i++){
            if(spaces.get(i).getImage()==null){
                spaces.get(i).setDisable(false);
                break;
            }
        }
        for(int i=3; i<6; i++){
            if(spaces.get(i).getImage()==null){
                spaces.get(i).setDisable(false);
                break;
            }
        }
        for(int i=6; i<9; i++){
            if(spaces.get(i).getImage()==null){
                spaces.get(i).setDisable(false);
                break;
            }
        }
    }

    private void fillSpacesList(){
        spaces = new ArrayList<>();
        spaces.add(space1L1_imageView);
        spaces.add(space1L2_imageView);
        spaces.add(space1L3_imageView);
        spaces.add(space2L1_imageView);
        spaces.add(space2L2_imageView);
        spaces.add(space2L3_imageView);
        spaces.add(space3L1_imageView);
        spaces.add(space3L2_imageView);
        spaces.add(space3L3_imageView);
    }
}
