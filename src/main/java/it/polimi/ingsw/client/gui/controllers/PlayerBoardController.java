package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.SceneNames;
import it.polimi.ingsw.exceptions.NotExistingNicknameException;
import it.polimi.ingsw.server.model.cards.card.Card;
import it.polimi.ingsw.server.model.cards.card.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.server.model.storage.*;
import javafx.application.Platform;
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
import java.util.stream.Collectors;

public class PlayerBoardController extends GenericController {

    private List<Shelf> shelves;
    private List<Resource> toDiscard;
    private boolean isPlayerTurn=false;
        
    private List<ImageView> spaces, faithSpaces;
    private ResourceType resToPlace;
    private boolean isResToPlaceAction=false, mainActionPlayed=false, warehouseIsDisabled=false;
    private ResourceType leader1WarehouseType=null, leader2WarehouseType=null,
            leader1ProductionConsumedType=null, leader2ProductionConsumedType=null; //TODO: brutto?

    @FXML private Label player_label,
            coinStrongbox_label, servantStrongbox_label, shieldStrongbox_label, stoneStrongbox_label,
            resToPlaceCoin_label, resToPlaceServant_label, resToPlaceShield_label, resToPlaceStone_label;
    @FXML private Button confirm_button, restoreWarehouse_button, activateProductions_button, endTurn_button,
            activeLeaderCard_button, discardLeaderCard_button, rearrangeWarehouse_button, viewOpponents_button;
    @FXML private CheckBox basicProduction_checkBox, devSpace1_checkBox, devSpace2_checkBox, devSpace3_checkBox,
            leader1_checkBox, leader2_checkBox;
    @FXML private ImageView inkwell_imageVIew,
            faithSpace0_imageView, faithSpace1_imageView, faithSpace2_imageView, faithSpace3_imageView,
            faithSpace4_imageView, faithSpace5_imageView, faithSpace6_imageView, faithSpace7_imageView,
            faithSpace8_imageView, faithSpace9_imageView, faithSpace10_imageView, faithSpace11_imageView,
            faithSpace12_imageView, faithSpace13_imageView, faithSpace14_imageView, faithSpace15_imageView,
            faithSpace16_imageView, faithSpace17_imageView, faithSpace18_imageView, faithSpace19_imageView,
            faithSpace20_imageView, faithSpace21_imageView, faithSpace22_imageView, faithSpace23_imageView,
            faithSpace24_imageView,
            shelfResL1_1_imageView, shelfResL2_1_imageView, shelfResL2_2_imageView,
            shelfResL3_1_imageView, shelfResL3_2_imageView, shelfResL3_3_imageView,
            space1L1_imageView, space1L2_imageView, space1L3_imageView, space2L1_imageView, space2L2_imageView,
            space2L3_imageView, space3L1_imageView, space3L2_imageView, space3L3_imageView,
            handLeader1_imageView, handLeader2_imageView, boardLeader1_imageView, boardLeader2_imageView,
            shelfLeader1_1_imageView, shelfLeader1_2_imageView, shelfLeader2_1_imageView, shelfLeader2_2_imageView,
            coinToPlace_imageView, servantToPlace_imageView, shieldToPlace_imageView, stoneToPlace_imageView,
            popeFavorTile1_imageView, popeFavorTile2_imageView, popeFavorTile3_imageView;

    public void setIsPlayerTurn(boolean isPlayerTurn){
        this.isPlayerTurn=isPlayerTurn;
        mainActionPlayed=false;
        if(isPlayerTurn) {
            enableButtons();
        }
        else {
            disableButtons();
        }
    }

    public void setIsResToPlace(boolean isResToPlaceAction){
        this.isResToPlaceAction=isResToPlaceAction;
    }

    public void setMainActionPlayed(boolean played){ //TODO: aggiornare tutti gli elementi visti, per sicurezza, e aggiornare tutti i bottoni
        mainActionPlayed=played;
        enableButtons();
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

    public void enableOpponents(){
        this.viewOpponents_button.setDisable(false);
    }

    public void goToMarket() {
        getGUIApplication().setActiveScene(SceneNames.MARKET_BOARD);
    }

    public void goToDecks() {
        ((DecksBoardController)getGUIApplication().getController(SceneNames.DECKS_BOARD)).showDevelopmentDeck();
        getGUIApplication().setActiveScene(SceneNames.DECKS_BOARD);
    }

    public void restoreWarehouse() {
        if(toDiscard!=null)
            toDiscard.clear();
        shelves = getGUI().getWarehouseShelvesCopy();
        resetResLabels();
        getGUI().updateResourcesToPlace();
        getGUI().updateWarehouse();
        confirm_button.setVisible(false);
        restoreWarehouse_button.setVisible(false);
        rearrangeWarehouse_button.setDisable(false);
        if(mainActionPlayed){
            showCheckBoxes();
            endTurn_button.setDisable(false);
        }
    }

    public void activateProductions() {
        if (checkSelectedCheckBoxes()) {
            disableButtons();
            setWarehouseIsDisabled(true);
            viewOpponents_button.setDisable(true);
            getActivatedProductions();
            resetCheckBoxes();
        }
    }

    public void endTurn() {
        disableButtons();
        mainActionPlayed=false;
        hideCheckBoxes();
        //TODO: disabilitare cose
        getGUI().sendEndTurnMessage();
    }

    public void activateLeaderCard() {
        sendLeaderMessage(true);
    }

    public void discardLeaderCard() {
        sendLeaderMessage(false);
    }

    private void sendLeaderMessage(boolean toActivate){
        List<Integer> leaders = new ArrayList<>();
        if(leader1_checkBox.isSelected())
            leaders.add(0);
        if(leader2_checkBox.isSelected())
            leaders.add(1);
        if(!leaders.isEmpty()) {
            leader1_checkBox.setSelected(false);
            leader1_checkBox.setVisible(false);
            leader2_checkBox.setSelected(false);
            leader2_checkBox.setVisible(false);
            getGUI().sendLeaderMessage(leaders, toActivate);
        }
    }

    public void rearrangeWarehouse() {
        if(!warehouseIsEmpty()) {
            for (Shelf shelf : shelves) {
                addQuantity(shelf.getResources().getResourceType(), shelf.getResources().getQuantity());
                resetShelf(shelf);
            }
            resetShelvesImageView();
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

    public void handleDragOver1L1(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragOver1L2(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragOver1L3(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragOver2L1(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragOver2L2(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragOver2L3(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragOver3L1(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragOver3L2(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragOver3L3(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    private void handleDragDroppedSpace(DragEvent dragEvent, ImageView imageView, int space) {
        DecksBoardController dbc = (DecksBoardController) getGUIApplication().getController(SceneNames.DECKS_BOARD);
        /*Image image = dragEvent.getDragboard().getImage();
        imageView.setImage(image);*/
        disableSpaces();
        getGUIApplication().closeCardStage();
        getGUI().sendBuyDevelopmentCardMessage(dbc.getSelectedCardColor(), dbc.getSelectedLevel(), space-1);
    }

    public void handleDragDropped1L1(DragEvent dragEvent) {
        handleDragDroppedSpace(dragEvent, space1L1_imageView, 1);
    }

    public void handleDragDropped1L2(DragEvent dragEvent) {
        handleDragDroppedSpace(dragEvent, space1L2_imageView, 1);
    }

    public void handleDragDropped1L3(DragEvent dragEvent) {
        handleDragDroppedSpace(dragEvent, space1L3_imageView, 1);
    }

    public void handleDragDropped2L1(DragEvent dragEvent) {
        handleDragDroppedSpace(dragEvent, space2L1_imageView, 2);
    }

    public void handleDragDropped2L2(DragEvent dragEvent) {
        handleDragDroppedSpace(dragEvent, space2L2_imageView, 2);
    }

    public void handleDragDropped2L3(DragEvent dragEvent) {
        handleDragDroppedSpace(dragEvent, space2L3_imageView, 2);
    }

    public void handleDragDropped3L1(DragEvent dragEvent) {
        handleDragDroppedSpace(dragEvent, space3L1_imageView, 3);
    }

    public void handleDragDropped3L2(DragEvent dragEvent) {
        handleDragDroppedSpace(dragEvent, space3L2_imageView, 3);
    }

    public void handleDragDropped3L3(DragEvent dragEvent) {
        handleDragDroppedSpace(dragEvent, space3L3_imageView, 3);
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

    public void handleDragOverShelfLeader1_1(DragEvent dragEvent) {
        if(leader1WarehouseType!=null)
            handleDragOverShelf(dragEvent);
    }

    public void handleDragOverShelfLeader1_2(DragEvent dragEvent) {
        if(leader1WarehouseType!=null)
            handleDragOverShelf(dragEvent);
    }

    public void handleDragOverShelfLeader2_1(DragEvent dragEvent) {
        if(leader2WarehouseType!=null)
            handleDragOverShelf(dragEvent);
    }

    public void handleDragOverShelfLeader2_2(DragEvent dragEvent) {
        if(leader2WarehouseType!=null)
            handleDragOverShelf(dragEvent);
    }

    private boolean handleDragDroppedShelf(DragEvent dragEvent, ImageView imageView, int shelfLevel){
        if(addToWarehouse(resToPlace, shelfLevel-1)) {
            Image image = dragEvent.getDragboard().getImage();
            imageView.setImage(image);
            setResToPlaceQuantity(resToPlace, getResToPlaceQuantity(resToPlace) - 1);
            restoreWarehouse_button.setVisible(true);
            rearrangeWarehouse_button.setDisable(true);
            return true;
        }
        return false;
    }

    public void handleDragDroppedShelfResL1_1(DragEvent dragEvent) {
        handleDragDroppedShelf(dragEvent, shelfResL1_1_imageView, 1);
    }

    public void handleDragDroppedShelfResL2_1(DragEvent dragEvent) {
        if(handleDragDroppedShelf(dragEvent, shelfResL2_1_imageView, 2)){
            setWhShelvesImages(2);
        }
    }

    public void handleDragDroppedShelfResL2_2(DragEvent dragEvent) {
        if(handleDragDroppedShelf(dragEvent, shelfResL2_2_imageView, 2))
            setWhShelvesImages(2);
    }

    public void handleDragDroppedShelfResL3_1(DragEvent dragEvent) {
        if(handleDragDroppedShelf(dragEvent, shelfResL3_1_imageView, 3))
            setWhShelvesImages(3);
    }

    public void handleDragDroppedShelfResL3_2(DragEvent dragEvent) {
        if(handleDragDroppedShelf(dragEvent, shelfResL3_2_imageView, 3))
            setWhShelvesImages(3);
    }

    public void handleDragDroppedShelfResL3_3(DragEvent dragEvent) {
        if(handleDragDroppedShelf(dragEvent, shelfResL3_3_imageView, 3))
            setWhShelvesImages(3);
    }

    public void handleDragDroppedShelfLeader1_1(DragEvent dragEvent) { //TODO: da testare
        int position = getGUI().getLeaderShelfPosition(leader1WarehouseType)+1;
        if(handleDragDroppedShelf(dragEvent, shelfLeader1_1_imageView, position))
            setWhLeadersImages(position, 1);
    }

    public void handleDragDroppedShelfLeader1_2(DragEvent dragEvent) { //TODO: da testare
        int position = getGUI().getLeaderShelfPosition(leader1WarehouseType)+1;
        if(handleDragDroppedShelf(dragEvent, shelfLeader1_2_imageView, position))
            setWhLeadersImages(position, 1);
    }

    public void handleDragDroppedShelfLeader2_1(DragEvent dragEvent) { //TODO: da testare
        int position = getGUI().getLeaderShelfPosition(leader2WarehouseType)+1;
        if(handleDragDroppedShelf(dragEvent, shelfLeader2_1_imageView, position))
            setWhLeadersImages(position, 2);
    }

    public void handleDragDroppedShelfLeader2_2(DragEvent dragEvent) { //TODO: da testare
        int position = getGUI().getLeaderShelfPosition(leader2WarehouseType)+1;
        if(handleDragDroppedShelf(dragEvent, shelfLeader2_2_imageView, position))
            setWhLeadersImages(position, 2);
    }

    public void handleDragDroppedHole() {
        addToDiscardedResources(resToPlace);
        restoreWarehouse_button.setVisible(true);
        setResToPlaceQuantity(resToPlace, getResToPlaceQuantity(resToPlace) - 1);
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

    public void enableSpaces(){ //TODO: migliorabile?
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

    private ImageView getDevSpace(int space, int level) {
        if (space == 0) {
            if (level == 1)
                return space1L1_imageView;
            if (level == 2)
                return space1L2_imageView;
            if (level == 3)
                return space1L3_imageView;
        } else if (space == 1) {
            if (level == 1)
                return space2L1_imageView;
            if (level == 2)
                return space2L2_imageView;
            if (level == 3)
                return space2L3_imageView;
        }else if (space == 2) {
            if (level == 1)
                return space3L1_imageView;
            if (level == 2)
                return space3L2_imageView;
            if (level == 3)
                return space3L3_imageView;
        }
        return null;
    }

    public int getResToPlaceQuantity(ResourceType resourceType) {
        if (resourceType == ResourceType.COIN) {
            return Integer.parseInt(resToPlaceCoin_label.getText().substring(2));
        } else if (resourceType == ResourceType.SERVANT) {
            return Integer.parseInt(resToPlaceServant_label.getText().substring(2));
        }else if (resourceType == ResourceType.SHIELD) {
            return Integer.parseInt(resToPlaceShield_label.getText().substring(2));
        }else if (resourceType == ResourceType.STONE){
            return Integer.parseInt(resToPlaceStone_label.getText().substring(2));
        }
        return -1;
    }

    public void setResToPlaceQuantity(ResourceType resourceType, int quantity){
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

    public int getStrongboxQuantity(ResourceType resourceType) {
        if (resourceType == ResourceType.COIN) {
            return Integer.parseInt(coinStrongbox_label.getText().substring(2));
        } else if (resourceType == ResourceType.SERVANT) {
            return Integer.parseInt(servantStrongbox_label.getText().substring(2));
        }else if (resourceType == ResourceType.SHIELD) {
            return Integer.parseInt(shieldStrongbox_label.getText().substring(2));
        }else if (resourceType == ResourceType.STONE){
            return Integer.parseInt(stoneStrongbox_label.getText().substring(2));
        }
        return 0;
    }

    public void setStrongboxQuantity(ResourceType resourceType, int quantity){
        if(resourceType == ResourceType.COIN) {
            coinStrongbox_label.setText("x " + quantity);
        }else if (resourceType == ResourceType.SERVANT) {
            servantStrongbox_label.setText("x "+quantity);
        }else if (resourceType == ResourceType.SHIELD) {
            shieldStrongbox_label.setText("x "+quantity);
        }else if (resourceType == ResourceType.STONE){
            stoneStrongbox_label.setText("x "+quantity);
        }
    }

    private void checkEnableButtons(){
        if(getResToPlaceQuantity(ResourceType.COIN)==0 && getResToPlaceQuantity(ResourceType.SERVANT)==0
                && getResToPlaceQuantity(ResourceType.SHIELD)==0 && getResToPlaceQuantity(ResourceType.STONE)==0){
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
        hideCheckBoxes();
    }

    public void enableButtons(){
        if(!mainActionPlayed) {
            ((MarketBoardController) getGUIApplication().getController(SceneNames.MARKET_BOARD)).enableMarketAction();
            ((DecksBoardController) getGUIApplication().getController(SceneNames.DECKS_BOARD)).enableBuyCardAction();
            activateProductions_button.setDisable(false);
            endTurn_button.setDisable(true);
        }
        if(mainActionPlayed) {
            ((MarketBoardController) getGUIApplication().getController(SceneNames.MARKET_BOARD)).disableMarketAction();
            ((DecksBoardController) getGUIApplication().getController(SceneNames.DECKS_BOARD)).disableBuyCardAction();
            activateProductions_button.setDisable(true);
            endTurn_button.setDisable(false);
        }
        activeLeaderCard_button.setDisable(false);
        discardLeaderCard_button.setDisable(false);
        rearrangeWarehouse_button.setDisable(false);
        viewOpponents_button.setDisable(false);
        showCheckBoxes();
    }

    public void confirm() {
        if(toDiscard==null)
            toDiscard = new ArrayList<>();
        getGUI().sendShelvesConfigurationMessage(shelves,toDiscard);
        if(isPlayerTurn)
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

    public void showDevelopmentBSpaces() {
        int id, maxLevel = 3;
        List<Deck> tempDevDeck = getGUI().getDevelopmentBoard();
        for(int i = 0; i< tempDevDeck.size(); i++){
            for(int level = 1; level <= maxLevel; level++) {
                int finalLevel = level;
                id = tempDevDeck.get(i).getCards().stream().map(x -> (DevelopmentCard)x).filter(x -> x.getLevel() == finalLevel).
                        map(Card::getID).findFirst().orElse(-1);
                getDevSpace(i,level).setImage((id > -1) ? new Image(
                        getClass().getResourceAsStream("/imgs/devCards/" + id + ".png")) : null);
            }
        }
        showCheckBoxes();
    }

    public void showFaithBoard(){
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

    private void resetFaith(){
        fillFaithSpaces();
        for(ImageView imageView : faithSpaces)
            imageView.setImage(null);
    }

    public void showFaithBPope() {
        try {
            boolean [] popeCopy = getGUI().getLocalPlayerBoard().getFaithBoard().getPopeProgression();
            for (int i=0; i < popeCopy.length; i++) {
                if (i == 0) {
                    popeFavorTile1_imageView.setImage(new Image(
                            (popeCopy[i]) ? getClass().getResourceAsStream("/imgs/faith/tile_2pointUp.png") :
                                    getClass().getResourceAsStream("/imgs/faith/tile_2point.png")));
                }else if (i == 1){
                    popeFavorTile2_imageView.setImage(new Image(
                            (popeCopy[i]) ? getClass().getResourceAsStream("/imgs/faith/tile_3pointUp.png") :
                                    getClass().getResourceAsStream("/imgs/faith/tile_3point.png")));
                } else if (i == 2) {
                    popeFavorTile3_imageView.setImage(new Image(
                            (popeCopy[i]) ? getClass().getResourceAsStream("/imgs/faith/tile_4pointUp.png") :
                                    getClass().getResourceAsStream("/imgs/faith/tile_4point.png")));
                }
            }

        }catch(NotExistingNicknameException e){
            e.printStackTrace();
        }
    }

    public void setLeaderBHand(List<Integer> idList){  //TODO: mi arriva sia per le mie leader sia per quelle degli oppo, va bene
        if(idList.stream().noneMatch(x -> x == -1)) {
            handLeader1_imageView.setImage(null);
            handLeader2_imageView.setImage(null);
            for (Integer id : idList) {
                if (handLeader1_imageView.getImage() == null && boardLeader1_imageView.getImage() == null) {
                    handLeader1_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/cost_"
                            + id + ".png")));
                } else if (handLeader2_imageView.getImage() == null && boardLeader2_imageView.getImage() == null) {
                    handLeader2_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/cost_"
                            + id + ".png")));
                }
            }
        }
        showLeaderCheckBoxes();
    }

    public void setLeaderBBoard(List<Integer> idList){  //TODO: mi arriva sia per le mie leader sia per quelle degli oppo, va bene?
        if(idList.stream().noneMatch(x -> x == -1)) {
            boardLeader1_imageView.setImage(null);
            boardLeader2_imageView.setImage(null);
            for(Integer id : idList) {
                if (boardLeader1_imageView.getImage() == null && handLeader1_imageView.getImage() == null) {
                    boardLeader1_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/"
                            + id + ".png")));
                    enableLeaderAbility(id, 1);
                } else if (boardLeader2_imageView.getImage() == null && handLeader2_imageView.getImage() == null) {
                    boardLeader2_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/"
                            + id + ".png")));
                    enableLeaderAbility(id, 2);
                }
            }
        }
        showLeaderCheckBoxes();
    }

    private void enableLeaderAbility(int id, int space){ //TODO: prendere risorse e info dalla carta, non le diamo cosi per scontato
        ResourceType resourceType;
        if(id==5) resourceType=ResourceType.STONE;
        else if(id==6) resourceType=ResourceType.SERVANT;
        else if(id==7) resourceType=ResourceType.SHIELD;
        else if(id==8) resourceType=ResourceType.COIN;
        else return;
        if(space==1) {
            leader1WarehouseType = resourceType;
            shelfLeader1_1_imageView.setDisable(false);
            shelfLeader1_2_imageView.setDisable(false);
        }
        else {
            leader2WarehouseType = resourceType;
            shelfLeader2_1_imageView.setDisable(false);
            shelfLeader2_2_imageView.setDisable(false);
        }
        //aggiungere controllo se sono delle produzioni
        /*esempio: if(id è una produzione && space==1) leader1ProductionConsumedType!=null=resourceType*/
    }

    public void showStrongbox() { //TODO: da controllare... perchè lo fa su un altro thread?
        resetStrongbox();
        for (Resource resource : getGUI().getStrongboxResources()){
            if(resource.getResourceType() == ResourceType.COIN){
                Platform.runLater(() -> coinStrongbox_label.setText("x " + resource.getQuantity()));
            } else if(resource.getResourceType() == ResourceType.SERVANT){
                Platform.runLater(() -> servantStrongbox_label.setText("x " + resource.getQuantity()));
            }else if(resource.getResourceType() == ResourceType.STONE){
                Platform.runLater(() -> stoneStrongbox_label.setText("x " + resource.getQuantity()));
            }else if(resource.getResourceType() == ResourceType.SHIELD){
                Platform.runLater(() -> shieldStrongbox_label.setText("x " + resource.getQuantity()));
            }
        }
    }

    private void resetStrongbox(){
        Platform.runLater(() -> coinStrongbox_label.setText("x 0"));
        Platform.runLater(() -> servantStrongbox_label.setText("x 0"));
        Platform.runLater(() -> stoneStrongbox_label.setText("x 0"));
        Platform.runLater(() -> shieldStrongbox_label.setText("x 0"));
    }

    public void showWarehouse() {
        shelves = getGUI().getWarehouseShelvesCopy();
        Image image;
        Shelf shelf;
        String resPath = "/imgs/res/", resType;
        for(int i = 0; i < shelves.size(); i++){
            shelf = shelves.get(i);
            if(i<3){ //basic
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
            else { //leader //TODO: da testare
                resType = shelf.getResourceType().toString()+".png";
                image = new Image(getClass().getResourceAsStream(resPath+resType));
                if(shelf.getResourceType()==leader1WarehouseType){
                    if(shelf.getResources().getQuantity()==1){
                        shelfLeader1_1_imageView.setImage(image);
                        shelfLeader1_2_imageView.setImage(null);
                    }
                    else if(shelf.getResources().getQuantity()==2) {
                        shelfLeader1_1_imageView.setImage(image);
                        shelfLeader1_2_imageView.setImage(image);
                    }
                    else {
                        shelfLeader1_1_imageView.setImage(null);
                        shelfLeader1_2_imageView.setImage(null);
                    }
                }
                else if(shelf.getResourceType()==leader2WarehouseType){
                    if(shelf.getResources().getQuantity()==1){
                        shelfLeader2_1_imageView.setImage(image);
                        shelfLeader2_2_imageView.setImage(null);
                    }
                    else if(shelf.getResources().getQuantity()==2) {
                        shelfLeader2_1_imageView.setImage(image);
                        shelfLeader2_2_imageView.setImage(image);
                    }
                    else {
                        shelfLeader2_1_imageView.setImage(null);
                        shelfLeader2_2_imageView.setImage(null);
                    }
                }
            }
        }
    }

    private void setWhShelvesImages(int level){
        int resQuantity = shelves.get(level-1).getResources().getQuantity();
        switch (level){
            case(2):
                if(resQuantity>0) {
                    Image image;
                    if (shelfResL2_1_imageView.getImage() != null)
                        image = shelfResL2_1_imageView.getImage();
                    else
                        image = shelfResL2_2_imageView.getImage();
                    if(resQuantity==1){
                        shelfResL2_1_imageView.setImage(image);
                        shelfResL2_2_imageView.setImage(null);
                    }
                    else {
                        shelfResL2_1_imageView.setImage(image);
                        shelfResL2_2_imageView.setImage(image);
                    }
                }
                break;
            case (3):
                if(resQuantity>0) {
                    Image image;
                    if (shelfResL3_1_imageView.getImage() != null)
                        image = shelfResL3_1_imageView.getImage();
                    else if (shelfResL3_2_imageView.getImage() != null)
                        image = shelfResL3_2_imageView.getImage();
                    else
                        image = shelfResL3_3_imageView.getImage();
                    if(resQuantity==1){
                        shelfResL3_1_imageView.setImage(image);
                        shelfResL3_2_imageView.setImage(null);
                        shelfResL3_3_imageView.setImage(null);
                    }
                    else if(resQuantity==2){
                        shelfResL3_1_imageView.setImage(image);
                        shelfResL3_2_imageView.setImage(image);
                        shelfResL3_3_imageView.setImage(null);
                    }
                    else {
                        shelfResL3_1_imageView.setImage(image);
                        shelfResL3_2_imageView.setImage(image);
                        shelfResL3_3_imageView.setImage(image);
                    }
                }
                break;
            default:break;
        }
    }

    private void setWhLeadersImages(int level, int leader){
        int resQuantity = shelves.get(level-1).getResources().getQuantity();
        switch (leader){
            case(1):
                if(resQuantity>0) {
                    Image image;
                    if (shelfLeader1_1_imageView.getImage() != null)
                        image = shelfLeader1_1_imageView.getImage();
                    else
                        image = shelfLeader1_2_imageView.getImage();
                    if(resQuantity==1){
                        shelfLeader1_1_imageView.setImage(image);
                        shelfLeader1_2_imageView.setImage(null);
                    }
                    else {
                        shelfLeader1_1_imageView.setImage(image);
                        shelfLeader1_2_imageView.setImage(image);
                    }
                }
                break;
            case (2):
                if(resQuantity>0) {
                    Image image;
                    if (shelfLeader2_1_imageView.getImage() != null)
                        image = shelfLeader2_1_imageView.getImage();
                    else
                        image = shelfLeader2_2_imageView.getImage();
                    if(resQuantity==1){
                        shelfLeader2_1_imageView.setImage(image);
                        shelfLeader2_2_imageView.setImage(null);
                    }
                    else {
                        shelfLeader2_1_imageView.setImage(image);
                        shelfLeader2_2_imageView.setImage(image);
                    }
                }
                break;
            default:break;
        }
    }

    private void addToDiscardedResources(ResourceType resourceType){
        if(toDiscard==null)
            toDiscard = new ArrayList<>();
        toDiscard.add(new Resource(resourceType,1));
    }

    private boolean addToWarehouse(ResourceType resourceType, int position){
        if(shelves==null || shelves.isEmpty())
            shelves = getGUI().getWarehouseShelvesCopy();
        if(checkFreeSpace()){
            if (shelves.get(position).getResourceType().equals(ResourceType.WILDCARD)) //empty shelf
                return emptyShelfManagement(shelves.get(position), resourceType);
            else if (shelves.get(position).getResourceType().equals(resourceType)) //shelf with the same resource type
                return sameResTypeShelfManagement(shelves.get(position), resourceType);
            else if (!shelves.get(position).isLeader()) //shelf with different resource type
                return differentResTypeShelfManagement(shelves.get(position), resourceType);
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
        setResToPlaceQuantity(resourceType, getResToPlaceQuantity(resourceType) + quantity);
    }

    private void resetShelfImageView(int level){
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

    private void resetShelvesImageView(){
        shelfResL1_1_imageView.setImage(null);
        shelfResL2_1_imageView.setImage(null);
        shelfResL2_2_imageView.setImage(null);
        shelfResL3_1_imageView.setImage(null);
        shelfResL3_2_imageView.setImage(null);
        shelfResL3_3_imageView.setImage(null);
        shelfLeader1_1_imageView.setImage(null);
        shelfLeader1_2_imageView.setImage(null);
        shelfLeader2_1_imageView.setImage(null);
        shelfLeader2_2_imageView.setImage(null);
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
        setResToPlaceQuantity(ResourceType.COIN,0);
        setResToPlaceQuantity(ResourceType.SERVANT,0);
        setResToPlaceQuantity(ResourceType.SHIELD,0);
        setResToPlaceQuantity(ResourceType.STONE,0);
    }

    /*private boolean isResourceQuantityGreaterThan(int max){
        int total = 0;
        if(shelves==null || shelves.isEmpty())
            shelves=getGUI().getWarehouseShelvesCopy();
        for(Shelf shelf : shelves)
            total += shelf.getResources().getQuantity();
        total += getStrongboxQuantity(ResourceType.COIN);
        total += getStrongboxQuantity(ResourceType.SERVANT);
        total += getStrongboxQuantity(ResourceType.SHIELD);
        total += getStrongboxQuantity(ResourceType.STONE);
        return total > max;
    }*/

    public void showCheckBoxes(){
        basicProduction_checkBox.setVisible(true);
        if(space1L1_imageView.getImage()!=null)
            devSpace1_checkBox.setVisible(true);
        if(space2L1_imageView.getImage()!=null)
            devSpace2_checkBox.setVisible(true);
        if(space3L1_imageView.getImage()!=null)
            devSpace3_checkBox.setVisible(true);
        showLeaderCheckBoxes();
    }

    private void showLeaderCheckBoxes(){
        if(handLeader1_imageView.getImage()!=null && boardLeader1_imageView.getImage()==null ||
                leader1ProductionConsumedType!=null)
            leader1_checkBox.setVisible(true);
        if(handLeader2_imageView.getImage()!=null && boardLeader2_imageView.getImage()==null ||
                leader2ProductionConsumedType!=null)
            leader2_checkBox.setVisible(true);
    }

    public void hideCheckBoxes(){
        basicProduction_checkBox.setVisible(false);
        devSpace1_checkBox.setVisible(false);
        devSpace2_checkBox.setVisible(false);
        devSpace3_checkBox.setVisible(false);
        leader1_checkBox.setVisible(false);
        leader2_checkBox.setVisible(false);
    }

    private void resetCheckBoxes(){
        basicProduction_checkBox.setSelected(false);
        devSpace1_checkBox.setSelected(false);
        devSpace2_checkBox.setSelected(false);
        devSpace3_checkBox.setSelected(false);
        leader1_checkBox.setSelected(false);
        leader2_checkBox.setSelected(false);
    }

    private boolean checkSelectedCheckBoxes(){
        return basicProduction_checkBox.isSelected() || devSpace1_checkBox.isSelected() ||
                devSpace2_checkBox.isSelected() || devSpace3_checkBox.isSelected() ||
                leader1ProductionConsumedType!=null && leader1_checkBox.isSelected() ||
                leader2ProductionConsumedType!=null && leader2_checkBox.isSelected(); //TODO: leaders da controllare
    }

    private void getActivatedProductions(){ //TODO: vedere se migliorabile
        List<Production> activatedProductions = new ArrayList<>();
        if(basicProduction_checkBox.isSelected())
            activatedProductions.add(getGUI().getBasicProduction());
        if(devSpace1_checkBox.isSelected())
            activatedProductions.add(((DevelopmentCard)getGUI().getDevelopmentBoard().get(0).get(0)).getProduction());
        if(devSpace2_checkBox.isSelected())
            activatedProductions.add(((DevelopmentCard)getGUI().getDevelopmentBoard().get(1).get(0)).getProduction());
        if(devSpace3_checkBox.isSelected())
            activatedProductions.add(((DevelopmentCard)getGUI().getDevelopmentBoard().get(2).get(0)).getProduction());
        if(leader1ProductionConsumedType!=null && leader1_checkBox.isSelected())
            activatedProductions.add(getGUI().getLeaderProduction(getGUI().
                    getLeaderProductionPosition(leader1ProductionConsumedType)));
        if(leader2ProductionConsumedType!=null && leader2_checkBox.isSelected())
            activatedProductions.add(getGUI().getLeaderProduction(getGUI().
                    getLeaderProductionPosition(leader2ProductionConsumedType)));
        resolveWildcard(activatedProductions);
    }

    private void resolveWildcard(List<Production> productions){
        List<Resource> consumedResolved = new ArrayList<>(), producedResolved = new ArrayList<>(),
                consumedWildcards = new ArrayList<>(), producedWildcards = new ArrayList<>();
        for(Production production : productions) {
            consumedResolved.addAll(production.getConsumed().stream()
                    .filter((r -> r.getResourceType() != ResourceType.WILDCARD)).collect(Collectors.toList()));
            producedResolved.addAll(production.getProduced().stream()
                    .filter((r -> r.getResourceType() != ResourceType.WILDCARD)).collect(Collectors.toList()));
            consumedWildcards.addAll(production.getConsumed().stream()
                    .filter((r -> r.getResourceType() == ResourceType.WILDCARD)).collect(Collectors.toList()));
            producedWildcards.addAll(production.getProduced().stream()
                    .filter((r -> r.getResourceType() == ResourceType.WILDCARD)).collect(Collectors.toList()));
        }
        if(!consumedWildcards.isEmpty() || !producedWildcards.isEmpty()) {
            Storage.aggregateResources(consumedResolved);
            Storage.aggregateResources(producedResolved);
            Storage.aggregateResources(consumedWildcards);
            Storage.aggregateResources(producedWildcards);
            ((WildcardResolverController) getGUIApplication().getController(SceneNames.RESOURCE_CHOICE_MENU)).
                    resolveWildcard(consumedResolved, producedResolved, consumedWildcards, producedWildcards);
            getGUIApplication().setActiveScene(SceneNames.RESOURCE_CHOICE_MENU);
        }
        else getGUI().sendCanActivateProductionsMessage(productions);
    }
}
