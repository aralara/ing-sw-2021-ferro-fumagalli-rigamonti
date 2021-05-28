package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.SceneNames;
import it.polimi.ingsw.exceptions.NotExistingNicknameException;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import it.polimi.ingsw.server.model.storage.Shelf;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;

import java.util.ArrayList;
import java.util.List;

public class PlayerBoardController extends GenericController {

    private List<Shelf> shelves;
    private List<Resource> toDiscard;
        
    private List<ImageView> spaces, faithSpaces;
    private ResourceType resToPlace;
    private boolean isResToPlaceAction=false, mainActionPlayed=false, warehouseIsDisabled=false;

    @FXML private Button confirm_button, restoreWarehouse_button, activateProductions_button, endTurn_button, activeLeaderCard_button, discardLeaderCard_button,
            rearrangeWarehouse_button, viewOpponents_button;
    @FXML private ImageView space1L1_imageView, space1L2_imageView, space1L3_imageView, space2L1_imageView,
            space2L2_imageView, space2L3_imageView, space3L1_imageView, space3L2_imageView, space3L3_imageView;
    @FXML private ImageView faithSpace0_imageView, faithSpace1_imageView, faithSpace2_imageView, faithSpace3_imageView, faithSpace4_imageView,
    faithSpace5_imageView, faithSpace6_imageView, faithSpace7_imageView ,faithSpace8_imageView, faithSpace9_imageView,
    faithSpace10_imageView, faithSpace11_imageView, faithSpace12_imageView, faithSpace13_imageView, faithSpace14_imageView,
    faithSpace15_imageView, faithSpace16_imageView, faithSpace17_imageView, faithSpace18_imageView, faithSpace19_imageView,
    faithSpace20_imageView, faithSpace21_imageView, faithSpace22_imageView, faithSpace23_imageView, faithSpace24_imageView;
    @FXML private Label player_label, resToPlaceCoin_label, resToPlaceServant_label, resToPlaceShield_label, resToPlaceStone_label;
    @FXML private ImageView inkwell_imageVIew, coinToPlace_imageView, servantToPlace_imageView, shieldToPlace_imageView,
            stoneToPlace_imageView;
    @FXML private ImageView shelfResL1_1_imageView, shelfResL2_1_imageView, shelfResL2_2_imageView,
            shelfResL3_1_imageView, shelfResL3_2_imageView, shelfResL3_3_imageView, handLeader1_imageView, handLeader2_imageView,
            spaceLeader1_imageView, spaceLeader2_imageView;
    @FXML private CheckBox basicProduction_checkBox, devSpace1_checkBox, devSpace2_checkBox, devSpace3_checkBox,
            leader1_checkBox, leader2_checkBox;


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

    public void goToMarket() {
        getGUIApplication().setActiveScene(SceneNames.MARKET_BOARD);
    }

    public void goToDecks() {
        getGUIApplication().setActiveScene(SceneNames.DECKS_BOARD);
    }

    public void restoreWarehouse() { //TODO: mettere a posto
        if(toDiscard!=null)
            toDiscard.clear();
        shelves = getGUI().getWarehouseShelvesCopy();
        resetResLabels();
        getGUI().updateResourcesToPlace();
        getGUI().updateWarehouse();
        confirm_button.setVisible(false);
        restoreWarehouse_button.setVisible(false);
        rearrangeWarehouse_button.setDisable(false);
        if(mainActionPlayed)
            endTurn_button.setDisable(false);
    }

    public void activateProductions() {
        //TODO: da fare
        ((MarketBoardController)getGUIApplication().getController(SceneNames.MARKET_BOARD)).disableMarketAction();
        ((DecksBoardController)getGUIApplication().getController(SceneNames.DECKS_BOARD)).disableBuyCardAction();
        disableActivateProductionsAction();
        disableActivateLeaderAction();
        disableDiscardLeaderAction();
        setWarehouseIsDisabled(true);
        //TODO: disabilitare checkBox leader e devSpace, drag strongbox, abilitare leader warehouse
    }

    public void endTurn() {
        disableButtons();
        mainActionPlayed=false;
        //TODO: disabilitare checkBox leader e devSpace, drag strongbox, abilitare leader warehouse
        getGUI().sendEndTurnMessage();
    }

    public void activateLeaderCard() {
        //TODO:da fare
    }

    public void discardLeaderCard() {
        //TODO:da fare
    }

    public void rearrangeWarehouse() {
        if(!warehouseIsEmpty()) {
            for (Shelf shelf : shelves) {
                addQuantity(shelf.getResources().getResourceType(), shelf.getResources().getQuantity());
                resetShelfImageView(shelf.getLevel());
                resetShelf(shelf);
            }
            rearrangeWarehouse_button.setDisable(true);
            restoreWarehouse_button.setVisible(true);
            confirm_button.setVisible(false);
        }
    }

    public void viewOpponents() {
        //TODO:da fare
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
        mainActionPlayed = true;
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
        String label = resToPlaceCoin_label.getText().substring(2);
        int labelQuantity = Integer.parseInt(label);
        if(labelQuantity>0) {
            handleDragDetectedToPlace(mouseEvent, coinToPlace_imageView);
            resToPlace=ResourceType.COIN;
        }
    }

    public void handleDragDetectedServantToPlace(MouseEvent mouseEvent) {
        String label = resToPlaceServant_label.getText().substring(2);
        int labelQuantity = Integer.parseInt(label);
        if(labelQuantity>0) {
            handleDragDetectedToPlace(mouseEvent, servantToPlace_imageView);
            resToPlace=ResourceType.SERVANT;
        }
    }

    public void handleDragDetectedShieldToPlace(MouseEvent mouseEvent) {
        String label = resToPlaceShield_label.getText().substring(2);
        int labelQuantity = Integer.parseInt(label);
        if(labelQuantity>0) {
            handleDragDetectedToPlace(mouseEvent, shieldToPlace_imageView);
            resToPlace=ResourceType.SHIELD;
        }
    }

    public void handleDragDetectedStoneToPlace(MouseEvent mouseEvent) {
        String label = resToPlaceStone_label.getText().substring(2);
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
        if(addToWarehouse(resToPlace, shelfLevel-1)) {
            Image image = dragEvent.getDragboard().getImage();
            imageView.setImage(image);
            setQuantity(resToPlace, getQuantity(resToPlace) - 1);
            restoreWarehouse_button.setVisible(true);
            rearrangeWarehouse_button.setDisable(true);
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
        restoreWarehouse_button.setVisible(true);
        setQuantity(resToPlace, getQuantity(resToPlace) - 1);
    }

    public void handleDragOverHole(DragEvent dragEvent) {
        if(isResToPlaceAction)
            handleDragOver(dragEvent);
    }

    public void disableActivateProductionsAction(){
        activateProductions_button.setDisable(true);
    }

    public void disableActivateLeaderAction(){
        activeLeaderCard_button.setDisable(true);
    }

    public void disableDiscardLeaderAction(){
        discardLeaderCard_button.setDisable(true);
    }

    private void disableSpaces(){
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

    private void fillFaithSpaces(){
        faithSpaces = new ArrayList<>();
        faithSpaces.add(faithSpace0_imageView);
        faithSpaces.add(faithSpace1_imageView);
        faithSpaces.add(faithSpace2_imageView);
        faithSpaces.add(faithSpace3_imageView);
        faithSpaces.add(faithSpace4_imageView);
        faithSpaces.add(faithSpace5_imageView);
        faithSpaces.add(faithSpace6_imageView);
        faithSpaces.add(faithSpace7_imageView);
        faithSpaces.add(faithSpace8_imageView);
        faithSpaces.add(faithSpace9_imageView);
        faithSpaces.add(faithSpace10_imageView);
        faithSpaces.add(faithSpace11_imageView);
        faithSpaces.add(faithSpace12_imageView);
        faithSpaces.add(faithSpace13_imageView);
        faithSpaces.add(faithSpace14_imageView);
        faithSpaces.add(faithSpace15_imageView);
        faithSpaces.add(faithSpace16_imageView);
        faithSpaces.add(faithSpace17_imageView);
        faithSpaces.add(faithSpace18_imageView);
        faithSpaces.add(faithSpace19_imageView);
        faithSpaces.add(faithSpace20_imageView);
        faithSpaces.add(faithSpace21_imageView);
        faithSpaces.add(faithSpace22_imageView);
        faithSpaces.add(faithSpace23_imageView);
        faithSpaces.add(faithSpace24_imageView);
    }

    public int getQuantity(ResourceType resourceType) {
        if (resourceType == ResourceType.COIN) {
            return Integer.parseInt(resToPlaceCoin_label.getText().substring(2));
        } else if (resourceType == ResourceType.SERVANT) {
            return Integer.parseInt(resToPlaceServant_label.getText().substring(2));
        }else if (resourceType == ResourceType.SHIELD) {
            return Integer.parseInt(resToPlaceShield_label.getText().substring(2));
        }else if (resourceType == ResourceType.STONE){
            return Integer.parseInt(resToPlaceStone_label.getText().substring(2));
        }
        return 0;
    }

    public void setQuantity(ResourceType resourceType, int quantity){
        if(resourceType == ResourceType.COIN) {
            resToPlaceCoin_label.setText("x " + quantity);
        }else if (resourceType == ResourceType.SERVANT) {
            resToPlaceServant_label.setText("x "+quantity);
        }else if (resourceType == ResourceType.SHIELD) {
            resToPlaceShield_label.setText("x "+quantity);
        }else if (resourceType == ResourceType.STONE){
            resToPlaceStone_label.setText("x "+quantity);
        }

        if(quantity>0)
            disableButtons();
        else if (quantity == 0)
            checkEnableButtons();
    }

    private void checkEnableButtons(){
        if(getQuantity(ResourceType.COIN)==0 && getQuantity(ResourceType.SERVANT)==0
                && getQuantity(ResourceType.SHIELD)==0 && getQuantity(ResourceType.STONE)==0){
            //isResToPlaceAction=false;
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
        //rearrangeWarehouse_button.setDisable(true);
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
        rearrangeWarehouse_button.setDisable(false);
        if(mainActionPlayed)
            endTurn_button.setDisable(false);
        isResToPlaceAction=false;
    }

    public void setDevelopmentBSpaces(List<List<Integer>> idList) {
        //TODO: stub
    }

    public void setFaithBFaith(int faith){
        resetFaith();
        Image cross = new Image(getClass().getResourceAsStream("/imgs/faith/cross_red.png"));
        int myFaith = 0;
        try {
            myFaith = getGUI().getLocalPlayerBoard().getFaithBoard().getFaith();
        }catch(NotExistingNicknameException e){
            e.printStackTrace();
        }
        if(myFaith > 24) myFaith = 24;
        faithSpaces.get(myFaith).setImage(cross);
    }

    private void resetFaith(){  //TODO: si può fare in modo migliore?
        fillFaithSpaces();
        for(ImageView imageView : faithSpaces)
            imageView.setImage(null);
    }

    public void setFaithBPope(boolean[] popeProgression) {
        //TODO: stub
    }

    public void setLeaderBHand(List<Integer> idList){  //TODO: mi arriva sia per le mie leader sia per quelle degli oppo, va bene
        if(idList.stream().noneMatch(x -> x == -1)) {
            handLeader1_imageView.setImage(null);
            handLeader2_imageView.setImage(null);
            for (Integer id : idList) {
                if (handLeader1_imageView.getImage() == null && spaceLeader1_imageView.getImage() == null) {
                    handLeader1_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/cost_"
                            + id + ".png")));
                } else if (handLeader2_imageView.getImage() == null && spaceLeader2_imageView.getImage() == null) {
                    handLeader2_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/cost_"
                            + id + ".png")));
                }
            }
        }
    }

    public void setLeaderBBoard(List<Integer> idList){  //TODO: mi arriva sia per le mie leader sia per quelle degli oppo, va bene?
        if(idList.stream().noneMatch(x -> x == -1)) {
            spaceLeader1_imageView.setImage(null);
            spaceLeader2_imageView.setImage(null);
            for(Integer id : idList) {
                if (spaceLeader1_imageView.getImage() == null && handLeader1_imageView.getImage() == null) {
                    spaceLeader1_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/"
                            + id + ".png")));
                } else if (spaceLeader2_imageView.getImage() == null && handLeader2_imageView.getImage() == null) {
                    spaceLeader2_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/"
                            + id + ".png")));
                }
            }
        }
    }

    public void setStrongbox(List<Resource> resources) {
        //TODO: stub
    }

    public void setWarehouse(List<Shelf> shelvesList) { //TODO: aggiungere caricamento leader
        shelves = getGUI().getWarehouseShelvesCopy();
        Image image;
        Shelf shelf;
        String resPath = "/imgs/res/", resType;
        for(int i = 0; i < shelves.size(); i++){
            shelf = shelves.get(i);
            if(i<3){
                switch (shelf.getLevel()){
                    case(1):
                        if(shelf.getResourceType() != ResourceType.WILDCARD) {
                            resType = shelf.getResourceType().toString()+".png";
                            shelfResL1_1_imageView.setImage(new Image(getClass().getResourceAsStream(resPath+resType)));
                        }
                        else
                            shelfResL1_1_imageView.setImage(null);
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
        if(checkFreeSpace()){
            if (shelves.get(level).getResourceType().equals(ResourceType.WILDCARD)) //empty shelf
                return emptyShelfManagement(shelves.get(level), resourceType);
            else if (shelves.get(level).getResourceType().equals(resourceType)) //shelf with the same resource type
                return sameResTypeShelfManagement(shelves.get(level), resourceType);
            else if (!shelves.get(level).isLeader()) //shelf with different resource type
                return differentResTypeShelfManagement(shelves.get(level), resourceType);
            else {
                showAlert(Alert.AlertType.ERROR, "Error", "Wrong slot",
                        "You can't place this resource here");
                return false;
            }
        }
        else {
            showAlert(Alert.AlertType.ERROR, "Error", "No more free slots",
                    "You can't place any resources anymore");
            return false;
        }
    }

    private boolean checkFreeSpace(){
        for (Shelf shelf : shelves) {
            if (shelf.getResourceType().equals(ResourceType.WILDCARD) ||
                    shelf.getLevel() > shelf.getResources().getQuantity()) {
                return true;
            }
        }
        return false;
    }

    private boolean emptyShelfManagement(Shelf selectedShelf, ResourceType resourceToPlace) {
        if(isResourceTypeUnique(shelves,resourceToPlace)) { //there are no shelves with the same resource type
            placeResource(selectedShelf, resourceToPlace);
            return true;
        }
        else {//there are shelves with the same resource type
            if(isShelfRearrangeable(shelves, resourceToPlace)) {
                Shelf otherShelf = getShelfWithSameResource(shelves, resourceToPlace);
                addQuantity(otherShelf.getResources().getResourceType(),otherShelf.getResources().getQuantity());
                resetShelfImageView(otherShelf.getLevel());
                resetShelf(otherShelf);
                placeResource(selectedShelf, resourceToPlace);
                return true;
            }
            return  false;
        }
    }

    private boolean sameResTypeShelfManagement(Shelf selectedShelf, ResourceType resourceToPlace){
        if (selectedShelf.getResources().getQuantity() <= selectedShelf.getLevel() - 1) { //shelf not completely full
            placeResource(selectedShelf, resourceToPlace);
            return true;
        }
        return false;
    }

    private boolean differentResTypeShelfManagement(Shelf selectedShelf, ResourceType resourceToPlace) {
        if (isResourceTypeUnique(shelves, resourceToPlace)) { //there are no shelves with the same resource type
            addQuantity(selectedShelf.getResources().getResourceType(),selectedShelf.getResources().getQuantity());
            resetShelfImageView(selectedShelf.getLevel());
            placeResource(selectedShelf, resourceToPlace);
            return true;
        }
        else {//there are shelves with the same resource type
            if (isShelfRearrangeable(shelves, resourceToPlace)) { //it's possible to rearrange shelves
                Shelf otherShelf = getShelfWithSameResource(shelves, resourceToPlace);
                addQuantity(otherShelf.getResources().getResourceType(),otherShelf.getResources().getQuantity());
                resetShelfImageView(otherShelf.getLevel());
                resetShelf(otherShelf);
                addQuantity(selectedShelf.getResources().getResourceType(),selectedShelf.getResources().getQuantity());
                resetShelfImageView(selectedShelf.getLevel());
                placeResource(selectedShelf, resourceToPlace);
                return true;
            }
            return false;
        }
    }

    private boolean isResourceTypeUnique(List<Shelf> shelves, ResourceType resourceType) {
        return shelves.stream().noneMatch(shelf -> !shelf.isLeader() && shelf.getResourceType().equals(resourceType));
    }

    private boolean isShelfRearrangeable(List<Shelf> shelves, ResourceType resourceType){
        Shelf shelfWithResources = getShelfWithSameResource(shelves, resourceType);
        int totalLeaderShelves = 2*(int)(shelves.stream().filter(shelf -> shelf.isLeader() && shelf.getResourceType()
                .equals(resourceType)).count());
        return shelfWithResources.getResources().getQuantity()+1 <= 3 + totalLeaderShelves;
    }

    private Shelf getShelfWithSameResource(List<Shelf> shelves, ResourceType resourceType){
        for(Shelf shelf : shelves){
            if(!shelf.isLeader() && shelf.getResourceType().equals(resourceType))
                return shelf;
        }
        return null; //TODO: brutto?
    }

    private void placeResource(Shelf shelf, ResourceType resourceType){
        if(shelf.getResourceType().equals(resourceType)){ //shelf with the same resource type
            shelf.getResources().setQuantity(shelf.getResources().getQuantity() + 1);
        }
        else { //empty shelf or with a different resource type
            shelf.setResourceType(resourceType);
            shelf.getResources().setResourceType(resourceType);
            shelf.getResources().setQuantity(1);
        }
    }

    private void resetShelf(Shelf shelf) {
        if(!shelf.isLeader()) {
            shelf.setResourceType(ResourceType.WILDCARD);
            shelf.getResources().setResourceType(ResourceType.WILDCARD);
        }
        shelf.getResources().setQuantity(0);
    }

    private void addQuantity(ResourceType resourceType, int quantity){
        setQuantity(resourceType, getQuantity(resourceType) + quantity);
    }

    private void resetShelfImageView(int level){ //TODO: manca leader
        switch (level){
            case(1):
                shelfResL1_1_imageView.setImage(null);
                break;
            case(2):
                shelfResL2_1_imageView.setImage(null);
                shelfResL2_2_imageView.setImage(null);
                break;
            case(3):
                shelfResL3_1_imageView.setImage(null);
                shelfResL3_2_imageView.setImage(null);
                shelfResL3_3_imageView.setImage(null);
                break;
            default: break;
        }
    }

    private boolean warehouseIsEmpty(){
        if(shelves==null || shelves.isEmpty())
            shelves=getGUI().getWarehouseShelvesCopy();
        for(Shelf shelf : shelves) {
            if (!shelf.isLeader() && shelf.getResourceType() != ResourceType.WILDCARD)
                return false;
            else if(shelf.isLeader() && shelf.getResources().getQuantity()>0)
                return false;
        }
        return true;
    }

    private void resetResLabels(){
        setQuantity(ResourceType.COIN,0);
        setQuantity(ResourceType.SERVANT,0);
        setQuantity(ResourceType.SHIELD,0);
        setQuantity(ResourceType.STONE,0);
    }
}
