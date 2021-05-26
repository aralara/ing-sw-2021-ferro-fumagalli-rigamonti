package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.SceneNames;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import it.polimi.ingsw.server.model.storage.Shelf;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;

import java.util.ArrayList;
import java.util.List;

public class PlayerBoardController extends GenericController {

    private List<Shelf> shelves;
    private List<Resource> toDiscard;

    private List<ImageView> spaces;
    private ResourceType resToPlace;
    private boolean isResToPlaceAction=false, mainActionPlayed=false, warehouseIsDisabled=false;

    @FXML private Button confirm_button, restoreWarehouse_button, activateProductions_button, endTurn_button, activeLeaderCard_button, discardLeaderCard_button,
            rearrangeWarehouse_button, viewOpponents_button;
    @FXML private ImageView space1L1_imageView, space1L2_imageView, space1L3_imageView, space2L1_imageView,
            space2L2_imageView, space2L3_imageView, space3L1_imageView, space3L2_imageView, space3L3_imageView;
    @FXML private Label player_label, resToPlaceCoin_label, resToPlaceServant_label, resToPlaceShield_label, resToPlaceStone_label;
    @FXML private ImageView inkwell_imageVIew, coinToPlace_imageView, servantToPlace_imageView, shieldToPlace_imageView,
            stoneToPlace_imageView;
    @FXML private ImageView shelfResL1_1_imageView, shelfResL2_1_imageView, shelfResL2_2_imageView,
            shelfResL3_1_imageView, shelfResL3_2_imageView, shelfResL3_3_imageView, handLeader1_imageView, handLeader2_imageView,
            spaceLeader1_imageView, spaceLeader2_imageView;


    public void setIsResToPlace(boolean isResToPlaceAction){
        this.isResToPlaceAction=isResToPlaceAction;
    }

    public void setMainActionPlayed(boolean played){
        mainActionPlayed=played;
        endTurn_button.setDisable(!mainActionPlayed);
    }

    public void setWarehouseIsDisabled(boolean isDisabled){
        warehouseIsDisabled=isDisabled;
    }

    public void setPlayer_label(String player_label){
        this.player_label.setText(player_label);
        this.player_label.setVisible(true);
    }

    public void enableInkwell(){
        this.inkwell_imageVIew.setVisible(true);
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

    public void goToMarket() {
        getGUIApplication().setActiveScene(SceneNames.MARKET_BOARD);
    }

    public void goToDecks() {
        getGUIApplication().setActiveScene(SceneNames.DECKS_BOARD);
    }

    public void restoreWarehouse() {
        //fare
        if(toDiscard!=null)
            toDiscard.clear();
        shelves = getGUI().getWarehouseShelvesCopy();
        getGUI().updateResourcesToPlace();
        getGUI().updateWarehouse();
        confirm_button.setVisible(false);
        restoreWarehouse_button.setVisible(false);
    }

    public void activateProductions() {
    }

    public void endTurn() {
    }

    public void activateLeaderCard() {
    }

    public void discardLeaderCard() {
    }

    public void rearrangeWarehouse() {
        //TODO: svuota e reinserisci tutto
    }

    public void viewOpponents() {
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
        String label = getResToPlaceCoin_label().getText().substring(2);
        int labelQuantity = Integer.parseInt(label);
        if(labelQuantity>0) {
            handleDragDetectedToPlace(mouseEvent, coinToPlace_imageView);
            resToPlace=ResourceType.COIN;
        }
    }

    public void handleDragDetectedServantToPlace(MouseEvent mouseEvent) {
        String label = getResToPlaceServant_label().getText().substring(2);
        int labelQuantity = Integer.parseInt(label);
        if(labelQuantity>0) {
            handleDragDetectedToPlace(mouseEvent, servantToPlace_imageView);
            resToPlace=ResourceType.SERVANT;
        }
    }

    public void handleDragDetectedShieldToPlace(MouseEvent mouseEvent) {
        String label = getResToPlaceShield_label().getText().substring(2);
        int labelQuantity = Integer.parseInt(label);
        if(labelQuantity>0) {
            handleDragDetectedToPlace(mouseEvent, shieldToPlace_imageView);
            resToPlace=ResourceType.SHIELD;
        }
    }

    public void handleDragDetectedStoneToPlace(MouseEvent mouseEvent) {
        String label = getResToPlaceStone_label().getText().substring(2);
        int labelQuantity = Integer.parseInt(label);
        if(labelQuantity>0) {
            handleDragDetectedToPlace(mouseEvent, stoneToPlace_imageView);
            resToPlace=ResourceType.STONE;
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

    private void handleDragDroppedShelf(DragEvent dragEvent, ImageView imageView, int shelfLevel){
        if(addToWarehouse(resToPlace, shelfLevel)) {
            Image image = dragEvent.getDragboard().getImage();
            imageView.setImage(image);
            setQuantity(resToPlace, getQuantity(resToPlace) - 1);
            restoreWarehouse_button.setVisible(true);
        }
    }

    public void handleDragDroppedShelfResL1_1(DragEvent dragEvent) {
        handleDragDroppedShelf(dragEvent, shelfResL1_1_imageView, 1);
    }

    public void handleDragDroppedShelfResL2_1(DragEvent dragEvent) {
        handleDragDroppedShelf(dragEvent, shelfResL2_1_imageView, 2);
    }

    public void handleDragDroppedShelfResL2_2(DragEvent dragEvent) {
        handleDragDroppedShelf(dragEvent, shelfResL2_2_imageView, 2);
    }

    public void handleDragDroppedShelfResL3_1(DragEvent dragEvent) {
        handleDragDroppedShelf(dragEvent, shelfResL3_1_imageView, 3);
    }

    public void handleDragDroppedShelfResL3_2(DragEvent dragEvent) {
        handleDragDroppedShelf(dragEvent, shelfResL3_2_imageView, 3);
    }

    public void handleDragDroppedShelfResL3_3(DragEvent dragEvent) {
        handleDragDroppedShelf(dragEvent, shelfResL3_3_imageView, 3);
    }

    public void handleDragDroppedHole() {
        addToDiscardedResources(resToPlace);
        setQuantity(resToPlace, getQuantity(resToPlace) - 1);
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

    public int getQuantity(ResourceType resourceType) {
        PlayerBoardController pbc = (PlayerBoardController) getGUIApplication().getController(SceneNames.PLAYER_BOARD);
        if (resourceType == ResourceType.COIN) {
            return Integer.parseInt(pbc.getResToPlaceCoin_label().getText().substring(2));
        } else if (resourceType == ResourceType.SERVANT) {
            return Integer.parseInt(pbc.getResToPlaceServant_label().getText().substring(2));
        }else if (resourceType == ResourceType.SHIELD) {
            return Integer.parseInt(pbc.getResToPlaceShield_label().getText().substring(2));
        }else if (resourceType == ResourceType.STONE){
            return Integer.parseInt(pbc.getResToPlaceStone_label().getText().substring(2));
        }
        return 0;
    }

    public void setQuantity(ResourceType resourceType, int quantity){
        PlayerBoardController pbc = (PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD);

        if(resourceType == ResourceType.COIN) {
            pbc.getResToPlaceCoin_label().setText("x " + quantity);
        }else if (resourceType == ResourceType.SERVANT) {
            pbc.getResToPlaceServant_label().setText("x "+quantity);
        }else if (resourceType == ResourceType.SHIELD) {
            pbc.getResToPlaceShield_label().setText("x "+quantity);
        }else if (resourceType == ResourceType.STONE){
            pbc.getResToPlaceStone_label().setText("x "+quantity);
        }

        if(quantity>0)
            disableButtons();
        else if (quantity == 0)
            checkEnableButtons();
    }

    private void checkEnableButtons(){
        if(getQuantity(ResourceType.COIN)==0 && getQuantity(ResourceType.SERVANT)==0
                && getQuantity(ResourceType.SHIELD)==0 && getQuantity(ResourceType.STONE)==0){
            isResToPlaceAction=false;
            confirm_button.setVisible(true);
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

    public void confirm() {
        if(toDiscard==null)
            toDiscard = new ArrayList<>();
        getGUI().sendShelvesConfigurationMessage(shelves,toDiscard);
        enableButtons();
        if(toDiscard!=null)
            toDiscard.clear();
        if(shelves!=null)
            shelves.clear();
        restoreWarehouse_button.setVisible(false);
        confirm_button.setVisible(false);
    }

    public void setDevelopmentBSpaces(List<List<Integer>> idList) {
        //TODO: stub
    }

    public void setFaithBFaith(int faith) {
        //TODO: stub
    }

    public void setFaithBPope(boolean[] popeProgression) {
        //TODO: stub
    }

    public void setLeaderBHand(List<Integer> idList){
        handLeader1_imageView.setImage(null);
        handLeader2_imageView.setImage(null);
        for(Integer id : idList) {
            if (handLeader1_imageView.getImage() == null &&spaceLeader1_imageView.getImage() == null){
                handLeader1_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/cost_"
                        + id + ".png")));
            }
            else if(handLeader2_imageView.getImage() == null &&spaceLeader2_imageView.getImage() == null){
                handLeader2_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/cost_"
                        + id + ".png")));
            }
        }
    }

    public void setLeaderBBoard(List<Integer> idList){
        spaceLeader1_imageView.setImage(null);
        spaceLeader2_imageView.setImage(null);
        for(Integer id : idList) {
            if (spaceLeader1_imageView.getImage() == null && handLeader1_imageView.getImage() == null){
                spaceLeader1_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/"
                        + id + ".png")));
            }
            else if(spaceLeader2_imageView.getImage() == null && handLeader2_imageView.getImage() == null){
                spaceLeader2_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/"
                        + id + ".png")));
            }
        }
    }

    public void setStrongbox(List<Resource> resources) {
        //TODO: stub
    }

    public void setWarehouse(List<Shelf> shelves) { //TODO: aggiungere caricamento leader
        Image image;
        Shelf shelf;
        String resPath = "/imgs/res/", resType;
        for(int i=0; i<shelves.size(); i++){
            shelf = shelves.get(i);
            if(i<3){
                switch (shelf.getLevel()){
                    case(1):
                        if(shelf.getResourceType() != ResourceType.WILDCARD) {
                            resType = shelf.getResourceType().toString()+".png";
                            image = new Image(getClass().getResourceAsStream(resPath+resType));
                        }
                        else {
                            image = null;
                            shelfResL1_1_imageView.setImage(image);
                        }
                        break;
                    case(2):
                        if(shelf.getResourceType() != ResourceType.WILDCARD) {
                            resType = shelf.getResourceType().toString()+".png";
                            image = new Image(getClass().getResourceAsStream(resPath+resType));
                            if(shelf.getResources().getQuantity()==1){
                                shelfResL2_1_imageView.setImage(image);
                                shelfResL2_2_imageView.setImage(null);
                            }
                            else if(shelf.getResources().getQuantity()==2) {
                                shelfResL2_1_imageView.setImage(image);
                                shelfResL2_2_imageView.setImage(image);
                            }
                        }
                        else {
                            shelfResL2_1_imageView.setImage(null);
                            shelfResL2_2_imageView.setImage(null);
                        }
                        break;
                    case(3):
                        if(shelf.getResourceType() != ResourceType.WILDCARD) {
                            resType = shelf.getResourceType().toString()+".png";
                            image = new Image(getClass().getResourceAsStream(resPath+resType));
                            if(shelf.getResources().getQuantity()==1){
                                shelfResL3_1_imageView.setImage(image);
                                shelfResL3_2_imageView.setImage(null);
                                shelfResL3_3_imageView.setImage(null);
                            }
                            else if(shelf.getResources().getQuantity()==2) {
                                shelfResL3_1_imageView.setImage(image);
                                shelfResL3_2_imageView.setImage(image);
                                shelfResL3_3_imageView.setImage(null);
                            }
                            else if(shelf.getResources().getQuantity()==3) {
                                shelfResL3_1_imageView.setImage(image);
                                shelfResL3_2_imageView.setImage(image);
                                shelfResL3_3_imageView.setImage(image);
                            }
                        }
                        else {
                            shelfResL3_1_imageView.setImage(null);
                            shelfResL3_2_imageView.setImage(null);
                            shelfResL3_3_imageView.setImage(null);
                        }
                        break;
                    default: break;
                }
            }
            else if(i==3){

            }
            else if(i==4){

            }
        }
    }

    private void addToDiscardedResources(ResourceType resourceType){
        if(toDiscard==null)
            toDiscard = new ArrayList<>();
        toDiscard.add(new Resource(resourceType,1));
    }

    private boolean addToWarehouse(ResourceType resourceType, int level){ //3,4 x leader
        if(shelves==null || shelves.isEmpty())
            shelves = getGUI().getWarehouseShelvesCopy();
        //TODO: gestire
        return true;
    }
}
