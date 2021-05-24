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
    private boolean isResToPlaceAction=false, mainActionPlayed=false, warehouseIsDisabled=false;
    private int srcWarehousePosition;

    @FXML private Button restoreWarehouse_button, activateProductions_button, endTurn_button, activeLeaderCard_button, discardLeaderCard_button,
            rearrangeWarehouse_button, viewOpponents_button;
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

    public boolean getIsResToPlaceAction(){
        return isResToPlaceAction;
    }

    public void setIsResToPlace(boolean isResToPlaceAction){
        this.isResToPlaceAction=isResToPlaceAction;
    }

    public boolean getMainActionPlayed(){
        return mainActionPlayed;
    }

    public void setMainActionPlayed(boolean played){
        mainActionPlayed=played;
        if(mainActionPlayed)
            endTurn_button.setDisable(false);
        else endTurn_button.setDisable(true);
    }

    public boolean getWarehouseIsDisabled(){
        return warehouseIsDisabled;
    }

    public void setSrcWarehousePosition(int position){
        srcWarehousePosition=position;
    }

    public int getSrcWarehousePosition(){
        return srcWarehousePosition;
    }

    public void setWarehouseIsDisabled(boolean isDisabled){
        warehouseIsDisabled=isDisabled;
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

    public void restoreWarehouse(ActionEvent actionEvent) {
        //fare
        restoreWarehouse_button.setVisible(false);
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
        setMainActionPlayed(true);
        setWarehouseIsDisabled(false);
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

    private void handleDragOverShelf(DragEvent dragEvent){
        if(!warehouseIsDisabled)
            handleDragOver(dragEvent);
    }

    public void handleDragOverShelfResL1_1(DragEvent dragEvent) {
        handleDragOverShelf(dragEvent);
    }

    public void handleDragOverShelfResL2_1(DragEvent dragEvent) {
        handleDragOverShelf(dragEvent);
    }

    public void handleDragOverShelfResL2_2(DragEvent dragEvent) {
        handleDragOverShelf(dragEvent);
    }

    public void handleDragOverShelfResL3_1(DragEvent dragEvent) {
        handleDragOverShelf(dragEvent);
    }

    public void handleDragOverShelfResL3_2(DragEvent dragEvent) {
        handleDragOverShelf(dragEvent);
    }

    public void handleDragOverShelfResL3_3(DragEvent dragEvent) {
        handleDragOverShelf(dragEvent);
    }

    private void handleDragDroppedShelf(DragEvent dragEvent, ImageView imageView){
        if(resToPlace.equals("") || imageView.getImage()==null) {
            Image image = dragEvent.getDragboard().getImage();
            manageResToPlace(imageView.getImage());
            imageView.setImage(image);
            //TODO: mandare messaggio
        }
    }

    private void manageResToPlace(Image image){
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
            default:
                switchWarehouseResource(image);
                restoreWarehouse_button.setVisible(true);
        }
    }

    private void switchWarehouseResource(Image destImage){
        switch (srcWarehousePosition){
            case 11:
                shelfResL1_1_imageView.setImage(destImage);
                break;
            case 21:
                shelfResL2_1_imageView.setImage(destImage);
                break;
            case 22:
                shelfResL2_2_imageView.setImage(destImage);
                break;
            case 31:
                shelfResL3_1_imageView.setImage(destImage);
                break;
            case 32:
                shelfResL3_2_imageView.setImage(destImage);
                break;
            case 33:
                shelfResL3_3_imageView.setImage(destImage);
                break;
            default:break;
        }
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

    public void handleDragDetectedShelf(MouseEvent mouseEvent, ImageView imageView, int position) {
        Dragboard dragboard = imageView.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putImage(imageView.getImage());
        dragboard.setContent(clipboardContent);
        setSrcWarehousePosition(position);
        setResToPlace("");
        mouseEvent.consume();
    }

    public void handleDragDetectedShelfResL1_1(MouseEvent mouseEvent) {
        handleDragDetectedShelf(mouseEvent, shelfResL1_1_imageView, 11);
    }

    public void handleDragDetectedShelfResL2_1(MouseEvent mouseEvent) {
        handleDragDetectedShelf(mouseEvent, shelfResL2_1_imageView, 21);
    }

    public void handleDragDetectedShelfResL2_2(MouseEvent mouseEvent) {
        handleDragDetectedShelf(mouseEvent, shelfResL2_2_imageView, 22);
    }

    public void handleDragDetectedShelfResL3_1(MouseEvent mouseEvent) {
        handleDragDetectedShelf(mouseEvent, shelfResL3_1_imageView, 31);
    }

    public void handleDragDetectedShelfResL3_2(MouseEvent mouseEvent) {
        handleDragDetectedShelf(mouseEvent, shelfResL3_2_imageView, 32);
    }

    public void handleDragDetectedShelfResL3_3(MouseEvent mouseEvent) {
        handleDragDetectedShelf(mouseEvent, shelfResL3_3_imageView,33);
    }

    public void handleDragDroppedHole(DragEvent dragEvent) {
        manageResToPlace(null);
        //TODO: messaggio risorse da scartare
    }

    public void handleDragOverHole(DragEvent dragEvent) {
        if(isResToPlaceAction)
            handleDragOver(dragEvent);
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
        if(quantity>0)
            disableButtons();
        else if (quantity == 0)
            checkEnableButtons();
    }

    public int getServantQuantity(){
        PlayerBoardController pbc = (PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD);
        return Integer.parseInt(pbc.getResToPlaceServant_label().getText().substring(2));
    }

    public void setServantQuantity(int quantity){
        PlayerBoardController pbc = (PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD);
        pbc.getResToPlaceServant_label().setText("x "+quantity);
        if(quantity>0)
            disableButtons();
        else if (quantity == 0)
            checkEnableButtons();
    }

    public int getShieldQuantity(){
        PlayerBoardController pbc = (PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD);
        return Integer.parseInt(pbc.getResToPlaceShield_label().getText().substring(2));
    }

    public void setShieldQuantity(int quantity){
        PlayerBoardController pbc = (PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD);
        pbc.getResToPlaceShield_label().setText("x "+quantity);
        if(quantity>0)
            disableButtons();
        else if (quantity == 0)
            checkEnableButtons();
    }

    public int getStoneQuantity(){
        PlayerBoardController pbc = (PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD);
        return Integer.parseInt(pbc.getResToPlaceStone_label().getText().substring(2));
    }

    public void setStoneQuantity(int quantity){
        PlayerBoardController pbc = (PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD);
        pbc.getResToPlaceStone_label().setText("x "+quantity);
        if(quantity>0)
            disableButtons();
        else if (quantity == 0)
            checkEnableButtons();
    }

    private void checkEnableButtons(){
        if(getCoinQuantity()==0 && getServantQuantity()==0 && getShieldQuantity()==0 && getStoneQuantity()==0){
            isResToPlaceAction=false;
            enableButtons();
        }
    }

    public void disableButtons(){
        ((MarketBoardController)getGUIApplication().getController(SceneNames.MARKET_BOARD)).disableMarketAction();
        ((DecksBoardController)getGUIApplication().getController(SceneNames.DECKS_BOARD)).disableBuyCardAction();
        activateProductions_button.setDisable(true);
        endTurn_button.setDisable(true);
        activeLeaderCard_button.setDisable(true);
        discardLeaderCard_button.setDisable(true);
        rearrangeWarehouse_button.setDisable(true);
        viewOpponents_button.setDisable(true);
    }

    public void enableButtons(){
        if(!mainActionPlayed) {
            ((MarketBoardController) getGUIApplication().getController(SceneNames.MARKET_BOARD)).enableMarketAction();
            ((DecksBoardController) getGUIApplication().getController(SceneNames.DECKS_BOARD)).enableBuyCardAction();
            activateProductions_button.setDisable(false);
        }
        if(mainActionPlayed)
            endTurn_button.setDisable(false);
        activeLeaderCard_button.setDisable(false);
        discardLeaderCard_button.setDisable(false);
        rearrangeWarehouse_button.setDisable(false);
        viewOpponents_button.setDisable(false);
    }
}
