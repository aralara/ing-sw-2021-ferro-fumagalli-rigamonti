package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.SceneNames;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;

import java.util.ArrayList;
import java.util.List;

public class PlayerBoardController extends GenericController {

    private List<ImageView> spaces;
    private String resToPlace;

    @FXML private Button activateProductions_button;
    @FXML private ImageView space1L1_imageView, space1L2_imageView, space1L3_imageView, space2L1_imageView,
            space2L2_imageView, space2L3_imageView, space3L1_imageView, space3L2_imageView, space3L3_imageView;
    @FXML private Label resToPlaceCoin_label, resToPlaceServant_label, resToPlaceShield_label, resToPlaceStone_label;
    @FXML private ImageView coinToPlace_imageView, servantToPlace_imageView, shieldToPlace_imageView, stoneToPlace_imageView;
    @FXML private ImageView shelfResL1_1_imageView, shelfResL2_1_imageView, shelfResL2_2_imageView,
            shelfResL3_1_imageView, shelfResL3_2_imageView, shelfResL3_3_imageView;

    public String getResToPlace(){
        return resToPlace;
    }

    public void setResToPlace(String resToPlace){
        this.resToPlace=resToPlace;
    }

    public Label getResToPlaceCoin_label() {
        return resToPlaceCoin_label;
    }

    public Label getResToPlaceServant_label() {
        return resToPlaceServant_label;
    }

    public Label getResToPlaceShield_label() {
        return resToPlaceShield_label;
    }

    public Label getResToPlaceStone_label() {
        return resToPlaceStone_label;
    }

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

    public void handleDragOver(DragEvent dragEvent) {
        if(dragEvent.getDragboard().hasImage())
            dragEvent.acceptTransferModes(TransferMode.MOVE);
    }

    private void handleDragDroppedSpace(DragEvent dragEvent, ImageView imageView) {
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
        handleDragDroppedSpace(dragEvent, space1L1_imageView);
    }

    public void handleDragOver1L2(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragDropped1L2(DragEvent dragEvent) {
        handleDragDroppedSpace(dragEvent, space1L2_imageView);
    }

    public void handleDragOver1L3(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragDropped1L3(DragEvent dragEvent) {
        handleDragDroppedSpace(dragEvent, space1L3_imageView);
    }

    public void handleDragOver2L1(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragDropped2L1(DragEvent dragEvent) {
        handleDragDroppedSpace(dragEvent, space2L1_imageView);
    }

    public void handleDragOver2L2(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragDropped2L2(DragEvent dragEvent) {
        handleDragDroppedSpace(dragEvent, space2L2_imageView);
    }

    public void handleDragOver2L3(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragDropped2L3(DragEvent dragEvent) {
        handleDragDroppedSpace(dragEvent, space2L3_imageView);
    }

    public void handleDragOver3L1(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragDropped3L1(DragEvent dragEvent) {
        handleDragDroppedSpace(dragEvent, space3L1_imageView);
    }

    public void handleDragOver3L2(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragDropped3L2(DragEvent dragEvent) {
        handleDragDroppedSpace(dragEvent, space3L2_imageView);
    }

    public void handleDragOver3L3(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragDropped3L3(DragEvent dragEvent) {
        handleDragDroppedSpace(dragEvent, space3L3_imageView);
    }

    public void handleDragDetectedToPlace(MouseEvent mouseEvent, ImageView imageView) {
        Dragboard dragboard = imageView.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putImage(imageView.getImage());
        dragboard.setContent(clipboardContent);
        mouseEvent.consume();
    }

    public void handleDragDetectedCoinToPlace(MouseEvent mouseEvent) {
        PlayerBoardController pbc = ((PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD));
        String label = pbc.getResToPlaceCoin_label().getText().substring(2);
        int labelQuantity = Integer.parseInt(label);
        if(labelQuantity>0) {
            handleDragDetectedToPlace(mouseEvent, coinToPlace_imageView);
            ((PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD)).setResToPlace("coin");
        }
    }

    public void handleDragDetectedServantToPlace(MouseEvent mouseEvent) {
        PlayerBoardController pbc = ((PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD));
        String label = pbc.getResToPlaceServant_label().getText().substring(2);
        int labelQuantity = Integer.parseInt(label);
        if(labelQuantity>0) {
            handleDragDetectedToPlace(mouseEvent, servantToPlace_imageView);
            ((PlayerBoardController) getGUIApplication().getController(SceneNames.PLAYER_BOARD)).setResToPlace("servant");
        }
    }

    public void handleDragDetectedShieldToPlace(MouseEvent mouseEvent) {
        PlayerBoardController pbc = ((PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD));
        String label = pbc.getResToPlaceShield_label().getText().substring(2);
        int labelQuantity = Integer.parseInt(label);
        if(labelQuantity>0) {
            handleDragDetectedToPlace(mouseEvent, shieldToPlace_imageView);
            ((PlayerBoardController) getGUIApplication().getController(SceneNames.PLAYER_BOARD)).setResToPlace("shield");
        }
    }

    public void handleDragDetectedStoneToPlace(MouseEvent mouseEvent) {
        PlayerBoardController pbc = ((PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD));
        String label = pbc.getResToPlaceStone_label().getText().substring(2);
        int labelQuantity = Integer.parseInt(label);
        if(labelQuantity>0) {
            handleDragDetectedToPlace(mouseEvent, stoneToPlace_imageView);
            ((PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD)).setResToPlace("stone");
        }
    }

    public void handleDragOverShelfResL1_1(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragOverShelfResL2_1(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragOverShelfResL2_2(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragOverShelfResL3_1(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragOverShelfResL3_2(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragOverShelfResL3_3(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    private void handleDragDroppedShelf(DragEvent dragEvent, ImageView imageView){
        Image image = dragEvent.getDragboard().getImage();
        imageView.setImage(image);

        String resToPlace = ((PlayerBoardController)getGUIApplication()
                .getController(SceneNames.PLAYER_BOARD)).getResToPlace();
        switch (resToPlace){
            case "coin":
                setCoinQuantity(getCoinQuantity()-1);
                ((PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD)).setResToPlace("");
                break;
            case "servant":
                setServantQuantity(getServantQuantity()-1);
                ((PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD)).setResToPlace("");
                break;
            case "shield":
                setShieldQuantity(getShieldQuantity()-1);
                ((PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD)).setResToPlace("");
                break;
            case "stone":
                setStoneQuantity(getStoneQuantity()-1);
                ((PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD)).setResToPlace("");
                break;
            default: //TODO: scambio di shelf
        }
        //TODO: mandare messaggio
    }

    public void handleDragDroppedShelfResL1_1(DragEvent dragEvent) {
        handleDragDroppedShelf(dragEvent, shelfResL1_1_imageView);
    }

    public void handleDragDroppedShelfResL2_1(DragEvent dragEvent) {
        handleDragDroppedShelf(dragEvent, shelfResL2_1_imageView);
    }

    public void handleDragDroppedShelfResL2_2(DragEvent dragEvent) {
        handleDragDroppedShelf(dragEvent, shelfResL2_2_imageView);
    }

    public void handleDragDroppedShelfResL3_1(DragEvent dragEvent) {
        handleDragDroppedShelf(dragEvent, shelfResL3_1_imageView);
    }

    public void handleDragDroppedShelfResL3_2(DragEvent dragEvent) {
        handleDragDroppedShelf(dragEvent, shelfResL3_2_imageView);
    }

    public void handleDragDroppedShelfResL3_3(DragEvent dragEvent) {
        handleDragDroppedShelf(dragEvent, shelfResL3_3_imageView);
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

    public void enableActions(){
        //TODO: fare
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

    public int getCoinQuantity(){
        PlayerBoardController pbc = (PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD);
        return Integer.parseInt(pbc.getResToPlaceCoin_label().getText().substring(2));
    }

    public void setCoinQuantity(int quantity){
        PlayerBoardController pbc = (PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD);
        pbc.getResToPlaceCoin_label().setText("x "+quantity);
    }

    public int getServantQuantity(){
        PlayerBoardController pbc = (PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD);
        return Integer.parseInt(pbc.getResToPlaceServant_label().getText().substring(2));
    }

    public void setServantQuantity(int quantity){
        PlayerBoardController pbc = (PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD);
        pbc.getResToPlaceServant_label().setText("x "+quantity);
    }

    public int getShieldQuantity(){
        PlayerBoardController pbc = (PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD);
        return Integer.parseInt(pbc.getResToPlaceShield_label().getText().substring(2));
    }

    public void setShieldQuantity(int quantity){
        PlayerBoardController pbc = (PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD);
        pbc.getResToPlaceShield_label().setText("x "+quantity);
    }

    public int getStoneQuantity(){
        PlayerBoardController pbc = (PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD);
        return Integer.parseInt(pbc.getResToPlaceStone_label().getText().substring(2));
    }

    public void setStoneQuantity(int quantity){
        PlayerBoardController pbc = (PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD);
        pbc.getResToPlaceStone_label().setText("x "+quantity);
    }
}
