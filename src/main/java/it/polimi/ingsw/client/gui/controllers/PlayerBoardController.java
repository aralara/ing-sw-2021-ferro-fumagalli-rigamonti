package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.SceneNames;
import it.polimi.ingsw.client.structures.PlayerBoardView;
import it.polimi.ingsw.exceptions.NotExistingNicknameException;
import it.polimi.ingsw.server.model.cards.ability.AbilityProduction;
import it.polimi.ingsw.server.model.cards.ability.AbilityWarehouse;
import it.polimi.ingsw.server.model.cards.card.Card;
import it.polimi.ingsw.server.model.cards.card.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.utils.messages.client.EndTurnMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private boolean isFirstLeaderProduction = false, isSecondLeaderProduction = false,
            isFirstLeaderShelf = false, isSecondLeaderShelf = false;

    @FXML private Label player_label,
            coinStrongbox_label, servantStrongbox_label, shieldStrongbox_label, stoneStrongbox_label,
            resToPlaceCoin_label, resToPlaceServant_label, resToPlaceShield_label, resToPlaceStone_label,
            otherActions_label, resToPlace_label, leaders_label;
    @FXML private Separator separator1, separator2, separator3;
    @FXML private Button confirm_button, restoreWarehouse_button, market_button, decks_button,
            activateProductions_button, endTurn_button, activateLeaderCard_button, discardLeaderCard_button,
            rearrangeWarehouse_button, viewOpponents_button, leftArrow_button, rightArrow_button, goBoard_button;
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

    /**
     * Sets the isPlayerTurn attribute and enable or disable buttons accordingly
     * @param isPlayerTurn New attribute value
     */
    public void setIsPlayerTurn(boolean isPlayerTurn){
        this.isPlayerTurn=isPlayerTurn;
        mainActionPlayed=false;
        if(isPlayerTurn) {
            if(!isResToPlaceAction)
                enableButtons();
        }
        else {
            disableButtons();
            viewOpponents_button.setDisable(false);
        }
    }

    /**
     * Sets the isResToPlaceAction attribute
     * @param isResToPlaceAction New attribute value
     */
    public void setIsResToPlace(boolean isResToPlaceAction){
        this.isResToPlaceAction=isResToPlaceAction;
    }

    /**
     * Sets the mainActionPlayed attribute
     * @param played New attribute value
     */
    public void setMainActionPlayed(boolean played){
        mainActionPlayed=played;
        enableButtons();
    }

    /**
     * Sets the warehouseIsDisabled attribute
     * @param isDisabled New attribute value
     */
    public void setWarehouseIsDisabled(boolean isDisabled){
        warehouseIsDisabled=isDisabled;
    }

    /**
     * Sets the player_label attribute
     * @param player_label New attribute value
     */
    public void setPlayer_label(String player_label){
        this.player_label.setText(player_label);
        this.player_label.setVisible(true);
    }

    /**
     * Shows the inkwell image on the playerBoard
     */
    public void enableInkwell(){
        this.inkwell_imageVIew.setVisible(true);
    }

    /**
     * Enables the button to view opponents on the playerBoard
     */
    public void enableOpponents(){
        this.viewOpponents_button.setDisable(false);
    }

    /**
     * Shows the market scene over the main stage
     */
    public void goToMarket() {
        getGUIApplication().setActiveScene(SceneNames.MARKET_BOARD);
    }

    /**
     * Shows the development decks scene over the main stage
     */
    public void goToDecks() {
        ((DecksBoardController)getGUIApplication().getController(SceneNames.DECKS_BOARD)).showDevelopmentDeck();
        getGUIApplication().setActiveScene(SceneNames.DECKS_BOARD);
    }

    /**
     * Restores resources in the warehouse and resets attributes
     */
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
            activateLeaderCard_button.setDisable(false);
            discardLeaderCard_button.setDisable(false);
        }
        if(!isResToPlaceAction)
            viewOpponents_button.setDisable(false);
    }

    /**
     * Activates the productions action
     */
    public void activateProductions() {
        if (checkSelectedCheckBoxes()) {
            disableButtons();
            setWarehouseIsDisabled(true);
            viewOpponents_button.setDisable(true);
            controlActivatedProductions();
            resetCheckBoxes();
        }
    }

    /**
     * Sends to the server a message to notify the end of the turn
     */
    public void endTurn() {
        disableButtons();
        mainActionPlayed=false;
        hideCheckBoxes();
        getGUI().getMessageHandler().sendClientMessage(new EndTurnMessage());
    }

    /**
     * Calls sendLeaderMessage method to activate leaders
     */
    public void activateLeaderCard() {
        sendLeaderMessage(true);
    }

    /**
     * Calls sendLeaderMessage method to discard leaders
     */
    public void discardLeaderCard() {
        sendLeaderMessage(false);
    }

    /**
     * Takes selected leaders and call sendLeaderMessage GUI's method
     * @param toActivate True to activate cards, false to discard them
     */
    private void sendLeaderMessage(boolean toActivate){
        List<Integer> leaders = new ArrayList<>();
        if(leader1_checkBox.isSelected())
            leaders.add(0);
        if(leader2_checkBox.isSelected())
            leaders.add(1);
        if(!leaders.isEmpty()) {
            leader1_checkBox.setSelected(false);
            leader2_checkBox.setSelected(false);
            getGUI().sendLeaderMessage(leaders, toActivate);
        }
    }

    /**
     * Empties warehouse's shelves, then all the resources need to be placed again on the shelves
     */
    public void rearrangeWarehouse() {
        if(!warehouseIsDisabled && !warehouseIsEmpty()) {
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

    /**
     * Shows opponents' boards
     */
    public void viewOpponents() {
        showButtons(false);
        if(getGUI().getLorenzoFaith()==-1){ //multiplayer
            viewOpponent(true);
        }
        else { //single player
            player_label.setText("Lorenzo il Magnifico");
            inkwell_imageVIew.setVisible(false);
            updateFaithBoard(getGUI().getLorenzoFaith(), true);
            updateFaithBPope(new boolean[]{false, false, false});
            resetWarehouse();
            resetStrongbox();
            resetDevelopmentBSpaces();
            resetLeaderBoard();
            leftArrow_button.setDisable(true);
            rightArrow_button.setDisable(true);
            leaders_label.setVisible(false);
            separator3.setVisible(false);
        }
        hideLeaderHand();
        hideCheckBoxes();
    }

    /**
     * Shows the opponent's board
     * @param next True to show next player, false to show previous player in the list of opponents
     */
    private void viewOpponent(boolean next){//false x precedente
        String currentPlayer = player_label.getText();
        leftArrow_button.setDisable(false);
        rightArrow_button.setDisable(false);
        PlayerBoardView opponentBoard;
        if(currentPlayer.equals(getGUI().getNickname())){
            leftArrow_button.setDisable(true);
            opponentBoard = getGUI().getOpponents().get(0);
        }
        else if(next){
            int currentPosition = getGUI().getOpponentIndex(currentPlayer);
            opponentBoard = getGUI().getOpponents().get(currentPosition+1);
        }
        else{
            int currentPosition = getGUI().getOpponentIndex(currentPlayer);
            opponentBoard = getGUI().getOpponents().get(currentPosition-1);
        }
        int opponentPosition = getGUI().getOpponentIndex(opponentBoard.getNickname());
        if(opponentPosition==getGUI().getOpponents().size()-1)
            rightArrow_button.setDisable(true);
        if(opponentPosition==0)
            leftArrow_button.setDisable(true);
        player_label.setText(opponentBoard.getNickname());
        inkwell_imageVIew.setVisible(opponentBoard.isInkwell());
        updateFaithBoard(opponentBoard.getFaithBoard().getFaith(), false);
        updateFaithBPope(opponentBoard.getFaithBoard().getPopeProgression());
        updateWarehouse(opponentBoard.getWarehouse().getShelves());
        updateStrongbox(opponentBoard.getStrongbox().getResources());
        updateDevelopmentBSpaces(opponentBoard.getDevelopmentBoard().getSpaces());
        updateOpponentLeaderBoard(opponentBoard);
        hideLeaderHand();
        hideCheckBoxes();
    }

    /**
     * Handles drag over events
     * @param dragEvent Drag event
     */
    public void handleDragOver(DragEvent dragEvent) {
        if(dragEvent.getDragboard().hasImage())
            dragEvent.acceptTransferModes(TransferMode.MOVE);
    }

    /**
     * Handles drag over event of the first development space, card level 1
     * @param dragEvent Drag event
     */
    public void handleDragOver1L1(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    /**
     * Handles drag over event of the first development space, card level 2
     * @param dragEvent Drag event
     */
    public void handleDragOver1L2(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    /**
     * Handles drag over event of the first development space, card level 3
     * @param dragEvent Drag event
     */
    public void handleDragOver1L3(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    /**
     * Handles drag over event of the second development space, card level 1
     * @param dragEvent Drag event
     */
    public void handleDragOver2L1(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    /**
     * Handles drag over event of the second development space, card level 2
     * @param dragEvent Drag event
     */
    public void handleDragOver2L2(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    /**
     * Handles drag over event of the second development space, card level 3
     * @param dragEvent Drag event
     */
    public void handleDragOver2L3(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    /**
     * Handles drag over event of the third development space, card level 1
     * @param dragEvent Drag event
     */
    public void handleDragOver3L1(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    /**
     * Handles drag over event of the third development space, card level 2
     * @param dragEvent Drag event
     */
    public void handleDragOver3L2(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    /**
     * Handles drag over event of the third development space, card level 3
     * @param dragEvent Drag event
     */
    public void handleDragOver3L3(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    /**
     * Handles drag dropped events of the development spaces
     * @param space The space where drop the card
     */
    private void handleDragDroppedSpace(int space) {
        DecksBoardController dbc = (DecksBoardController) getGUIApplication().getController(SceneNames.DECKS_BOARD);
        disableSpaces();
        getGUIApplication().closeCardStage();
        getGUI().sendCanBuyDevelopmentCardMessage(dbc.getSelectedCardColor(), dbc.getSelectedLevel(), space-1);
    }

    /**
     * Handles drag dropped events of the first development space, card level 1
     */
    public void handleDragDropped1L1() {
        handleDragDroppedSpace(1);
    }

    /**
     * Handles drag dropped events of the first development space, card level 2
     */
    public void handleDragDropped1L2() {
        handleDragDroppedSpace(1);
    }

    /**
     * Handles drag dropped events of the first development space, card level 3
     */
    public void handleDragDropped1L3() {
        handleDragDroppedSpace(1);
    }

    /**
     * Handles drag dropped events of the second development space, card level 1
     */
    public void handleDragDropped2L1() {
        handleDragDroppedSpace(2);
    }

    /**
     * Handles drag dropped events of the second development space, card level 2
     */
    public void handleDragDropped2L2() {
        handleDragDroppedSpace(2);
    }

    /**
     * Handles drag dropped events of the second development space, card level 3
     */
    public void handleDragDropped2L3() {
        handleDragDroppedSpace(2);
    }

    /**
     * Handles drag dropped events of the third development space, card level 1
     */
    public void handleDragDropped3L1() {
        handleDragDroppedSpace(3);
    }

    /**
     * Handles drag dropped events of the third development space, card level 2
     */
    public void handleDragDropped3L2() {
        handleDragDroppedSpace(3);
    }

    /**
     * Handles drag dropped events of the third development space, card level 3
     */
    public void handleDragDropped3L3() {
        handleDragDroppedSpace(3);
    }

    /**
     * Handles drag detected event of the resources to place
     * @param mouseEvent Mouse event
     * @param imageView ImageView to drag
     */
    public void handleDragDetectedToPlace(MouseEvent mouseEvent, ImageView imageView) {
        Dragboard dragboard = imageView.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putImage(imageView.getImage());
        dragboard.setContent(clipboardContent);
        mouseEvent.consume();
    }

    /**
     * Handles drag detected event of the coin to place
     * @param mouseEvent Mouse event
     */
    public void handleDragDetectedCoinToPlace(MouseEvent mouseEvent) {
        String label = resToPlaceCoin_label.getText().substring(2);
        int labelQuantity = Integer.parseInt(label);
        if(labelQuantity>0) {
            handleDragDetectedToPlace(mouseEvent, coinToPlace_imageView);
            resToPlace=ResourceType.COIN;
        }
    }

    /**
     * Handles drag detected event of the servant to place
     * @param mouseEvent Mouse event
     */
    public void handleDragDetectedServantToPlace(MouseEvent mouseEvent) {
        String label = resToPlaceServant_label.getText().substring(2);
        int labelQuantity = Integer.parseInt(label);
        if(labelQuantity>0) {
            handleDragDetectedToPlace(mouseEvent, servantToPlace_imageView);
            resToPlace=ResourceType.SERVANT;
        }
    }

    /**
     * Handles drag detected event of the shield to place
     * @param mouseEvent Mouse event
     */
    public void handleDragDetectedShieldToPlace(MouseEvent mouseEvent) {
        String label = resToPlaceShield_label.getText().substring(2);
        int labelQuantity = Integer.parseInt(label);
        if(labelQuantity>0) {
            handleDragDetectedToPlace(mouseEvent, shieldToPlace_imageView);
            resToPlace=ResourceType.SHIELD;
        }
    }

    /**
     * Handles drag detected event of the stone to place
     * @param mouseEvent Mouse event
     */
    public void handleDragDetectedStoneToPlace(MouseEvent mouseEvent) {
        String label = resToPlaceStone_label.getText().substring(2);
        int labelQuantity = Integer.parseInt(label);
        if(labelQuantity>0) {
            handleDragDetectedToPlace(mouseEvent, stoneToPlace_imageView);
            resToPlace=ResourceType.STONE;
        }
    }

    /**
     * Handles drag over events of the shelves
     * @param dragEvent Drag event
     */
    private void handleDragOverShelf(DragEvent dragEvent){
        if(!warehouseIsDisabled)
            handleDragOver(dragEvent);
    }

    /**
     * Handles drag over events of the first shelf
     * @param dragEvent Drag event
     */
    public void handleDragOverShelfResL1_1(DragEvent dragEvent) {
        handleDragOverShelf(dragEvent);
    }

    /**
     * Handles drag over events of the second shelf, slot 1
     * @param dragEvent Drag event
     */
    public void handleDragOverShelfResL2_1(DragEvent dragEvent) {
        handleDragOverShelf(dragEvent);
    }

    /**
     * Handles drag over events of the second shelf, slot 2
     * @param dragEvent Drag event
     */
    public void handleDragOverShelfResL2_2(DragEvent dragEvent) {
        handleDragOverShelf(dragEvent);
    }

    /**
     * Handles drag over events of the third shelf, slot 1
     * @param dragEvent Drag event
     */
    public void handleDragOverShelfResL3_1(DragEvent dragEvent) {
        handleDragOverShelf(dragEvent);
    }

    /**
     * Handles drag over events of the third shelf, slot 2
     * @param dragEvent Drag event
     */
    public void handleDragOverShelfResL3_2(DragEvent dragEvent) {
        handleDragOverShelf(dragEvent);
    }

    /**
     * Handles drag over events of the third shelf, slot 3
     * @param dragEvent Drag event
     */
    public void handleDragOverShelfResL3_3(DragEvent dragEvent) {
        handleDragOverShelf(dragEvent);
    }

    /**
     * Handles drag over events of the first leader's shelf, slot 1
     * @param dragEvent Drag event
     */
    public void handleDragOverShelfLeader1_1(DragEvent dragEvent) {
        if(isFirstLeaderShelf)
            handleDragOverShelf(dragEvent);
    }

    /**
     * Handles drag over events of the first leader's shelf, slot 2
     * @param dragEvent Drag event
     */
    public void handleDragOverShelfLeader1_2(DragEvent dragEvent) {
        if(isFirstLeaderShelf)
            handleDragOverShelf(dragEvent);
    }

    /**
     * Handles drag over events of the second leader's shelf, slot 1
     * @param dragEvent Drag event
     */
    public void handleDragOverShelfLeader2_1(DragEvent dragEvent) {
        if(isSecondLeaderShelf)
            handleDragOverShelf(dragEvent);
    }

    /**
     * Handles drag over events of the second leader's shelf, slot 2
     * @param dragEvent Drag event
     */
    public void handleDragOverShelfLeader2_2(DragEvent dragEvent) {
        if(isSecondLeaderShelf)
            handleDragOverShelf(dragEvent);
    }

    /**
     * Handles drag dropped events of the shelves
     * @param dragEvent Drag event
     * @param imageView ImageView to drop
     * @param shelfLevel Level of the shelf where drop the resource
     * @return Returns true if the resource has been placed, false otherwise
     */
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

    /**
     * Handles drag dropped events of the first shelf
     * @param dragEvent Drag event
     */
    public void handleDragDroppedShelfResL1_1(DragEvent dragEvent) {
        handleDragDroppedShelf(dragEvent, shelfResL1_1_imageView, 1);
    }

    /**
     * Handles drag dropped events of the second shelf, slot 1
     * @param dragEvent Drag event
     */
    public void handleDragDroppedShelfResL2_1(DragEvent dragEvent) {
        if(handleDragDroppedShelf(dragEvent, shelfResL2_1_imageView, 2)){
            setWhShelvesImages(2);
        }
    }

    /**
     * Handles drag dropped events of the second shelf, slot 2
     * @param dragEvent Drag event
     */
    public void handleDragDroppedShelfResL2_2(DragEvent dragEvent) {
        if(handleDragDroppedShelf(dragEvent, shelfResL2_2_imageView, 2))
            setWhShelvesImages(2);
    }

    /**
     * Handles drag dropped events of the third shelf, slot 1
     * @param dragEvent Drag event
     */
    public void handleDragDroppedShelfResL3_1(DragEvent dragEvent) {
        if(handleDragDroppedShelf(dragEvent, shelfResL3_1_imageView, 3))
            setWhShelvesImages(3);
    }

    /**
     * Handles drag dropped events of the third shelf, slot 2
     * @param dragEvent Drag event
     */
    public void handleDragDroppedShelfResL3_2(DragEvent dragEvent) {
        if(handleDragDroppedShelf(dragEvent, shelfResL3_2_imageView, 3))
            setWhShelvesImages(3);
    }

    /**
     * Handles drag dropped events of the third shelf, slot 3
     * @param dragEvent Drag event
     */
    public void handleDragDroppedShelfResL3_3(DragEvent dragEvent) {
        if(handleDragDroppedShelf(dragEvent, shelfResL3_3_imageView, 3))
            setWhShelvesImages(3);
    }

    /**
     * Handles drag dropped events of the first leader's shelf, slot 1
     * @param dragEvent Drag event
     */
    public void handleDragDroppedShelfLeader1_1(DragEvent dragEvent) {
        int position = 4;
        if(handleDragDroppedShelf(dragEvent, shelfLeader1_1_imageView, position))
            setWhLeadersImages(position, 1);
    }

    /**
     * Handles drag dropped events of the first leader's shelf, slot 2
     * @param dragEvent Drag event
     */
    public void handleDragDroppedShelfLeader1_2(DragEvent dragEvent) {
        int position = 4;
        if(handleDragDroppedShelf(dragEvent, shelfLeader1_2_imageView, position))
            setWhLeadersImages(position, 1);
    }

    /**
     * Handles drag dropped events of the second leader's shelf, slot 1
     * @param dragEvent Drag event
     */
    public void handleDragDroppedShelfLeader2_1(DragEvent dragEvent) {
        int position = (isFirstLeaderShelf) ? 5 : 4;
        if(handleDragDroppedShelf(dragEvent, shelfLeader2_1_imageView, position))
            setWhLeadersImages(position, 2);
    }

    /**
     * Handles drag dropped events of the second leader's shelf, slot 2
     * @param dragEvent Drag event
     */
    public void handleDragDroppedShelfLeader2_2(DragEvent dragEvent) {
        int position = (isFirstLeaderShelf) ? 5 : 4;
        if(handleDragDroppedShelf(dragEvent, shelfLeader2_2_imageView, position))
            setWhLeadersImages(position, 2);
    }

    /**
     * Handles drag dropped events of the resources to discard
     */
    public void handleDragDroppedHole() {
        addToDiscardedResources(resToPlace);
        restoreWarehouse_button.setVisible(true);
        setResToPlaceQuantity(resToPlace, getResToPlaceQuantity(resToPlace) - 1);
    }

    /**
     * Handles drag over events of the resources to discard
     * @param dragEvent Drag event
     */
    public void handleDragOverHole(DragEvent dragEvent) {
        if(isResToPlaceAction)
            handleDragOver(dragEvent);
    }

    /**
     * Disables activateProductions button
     */
    public void disableActivateProductionsAction(){
        activateProductions_button.setDisable(true);
    }

    /**
     * Disables activateLeaderCard button
     */
    public void disableActivateLeaderAction(){
        activateLeaderCard_button.setDisable(true);
    }

    /**
     * Disables discardLeaderCard button
     */
    public void disableDiscardLeaderAction(){
        discardLeaderCard_button.setDisable(true);
    }

    /**
     * Disable development board's spaces
     */
    private void disableSpaces(){
        fillSpacesList();
        for(ImageView imageView : spaces)
            imageView.setDisable(true);
    }

    /**
     * Enables for each space the first development board's card which image is null
     */
    public void enableSpaces(){
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

    /**
     * Loads development cards' imageViews in the development board's spaces list
     */
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

    /**
     * Loads faith spaces' imageViews in their list
     */
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

    /**
     * Gets the development card's imageView in the development board's space given by parameter
     * @param space ImageView's space
     * @param level ImageView's level
     * @return Returns the selected imageView
     */
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

    /**
     * Gets the quantity of the resource given by parameter
     * @param resourceType Resource's type
     * @return Returns resource's quantity
     */
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

    /**
     * Sets the quantity of the resource given by parameter
     * @param resourceType Resource's type
     * @param quantity Resource's quantity
     */
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
        viewOpponents_button.setDisable(true);
        if(quantity>0)
            disableButtons();
        else if (quantity == 0 &&
                getResToPlaceQuantity(ResourceType.COIN)==0 && getResToPlaceQuantity(ResourceType.SERVANT)==0
                && getResToPlaceQuantity(ResourceType.SHIELD)==0 && getResToPlaceQuantity(ResourceType.STONE)==0)
            confirm_button.setVisible(true);
    }

    /**
     * Disables buttons and hides checkboxes to prevent the player to click them
     */
    public void disableButtons(){
        ((MarketBoardController)getGUIApplication().getController(SceneNames.MARKET_BOARD)).disableMarketAction();
        ((DecksBoardController)getGUIApplication().getController(SceneNames.DECKS_BOARD)).disableBuyCardAction();
        activateProductions_button.setDisable(true);
        endTurn_button.setDisable(true);
        activateLeaderCard_button.setDisable(true);
        discardLeaderCard_button.setDisable(true);
        hideCheckBoxes();
    }

    /**
     * Enables buttons depending on whether the main actions has been played
     */
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
        activateLeaderCard_button.setDisable(false);
        discardLeaderCard_button.setDisable(false);
        rearrangeWarehouse_button.setDisable(false);
        viewOpponents_button.setDisable(false);
        showCheckBoxes();
    }

    /**
     * Confirms the actual shelves' configurations and calls sendShelvesConfigurationMessage GUI's method
     */
    public void confirm() {
        if(toDiscard==null)
            toDiscard = new ArrayList<>();
        if(shelves==null)
            shelves = getGUI().getWarehouseShelvesCopy();
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
        viewOpponents_button.setDisable(false);
    }

    /**
     * Updates development board's imageViews showing the cards given by parameter
     * @param devDecks List of development cards to show
     */
    private void updateDevelopmentBSpaces(List<Deck> devDecks) {
        int id, maxLevel = 3;
        for(int i = 0; i< devDecks.size(); i++){
            for(int level = 1; level <= maxLevel; level++) {
                int finalLevel = level;
                id = devDecks.get(i).getCards().stream().map(x -> (DevelopmentCard)x).
                        filter(x -> x.getLevel() == finalLevel).map(Card::getID).findFirst().orElse(-1);
                getDevSpace(i,level).setImage((id > -1) ? new Image(
                        getClass().getResourceAsStream("/imgs/devCards/" + id + ".png")) : null);
            }
        }
        showCheckBoxes();
    }

    /**
     * Sets to null all the development board's spaces' imageViews
     */
    private void resetDevelopmentBSpaces(){
        space1L1_imageView.setImage(null);
        space1L2_imageView.setImage(null);
        space1L3_imageView.setImage(null);
        space2L1_imageView.setImage(null);
        space2L2_imageView.setImage(null);
        space2L3_imageView.setImage(null);
        space3L1_imageView.setImage(null);
        space3L2_imageView.setImage(null);
        space3L3_imageView.setImage(null);
    }

    /**
     * Shows local player's development board's cards
     */
    public void showDevelopmentBSpaces() {
        updateDevelopmentBSpaces(getGUI().getDevelopmentBoard());
    }

    /**
     * Updates faith board's imageViews showing the position of the cross
     * @param faith Faith level
     * @param isLorenzo True if it's single game and the cross to show is black
     */
    private void updateFaithBoard(int faith, boolean isLorenzo){
        resetFaith();
        Image cross = new Image(getClass().getResourceAsStream(
                "/imgs/faith/cross_"+(isLorenzo?"black":"red")+".png"));
        if(faith > 24) faith = 24;
        faithSpaces.get(faith).setImage(cross);
    }

    /**
     * Shows local player's faith level
     */
    public void showFaithBoard(){
        try {
            updateFaithBoard(getGUI().getLocalPlayerBoard().getFaithBoard().getFaith(), false);
        }catch(NotExistingNicknameException e){
            e.printStackTrace();
        }
    }

    /**
     * Sets to null all the faith board's spaces' imageViews
     */
    private void resetFaith(){
        fillFaithSpaces();
        for(ImageView imageView : faithSpaces)
            imageView.setImage(null);
    }

    /**
     * Updates imageViews of the faith Pope progression, showing or not the value of the tiles
     * @param popeTiles List of tiles to show
     */
    private void updateFaithBPope(boolean [] popeTiles) {
        for (int i=0; i < popeTiles.length; i++) {
            if (i == 0) {
                popeFavorTile1_imageView.setImage(new Image(
                        (popeTiles[i]) ? getClass().getResourceAsStream("/imgs/faith/tile_2pointUp.png") :
                                getClass().getResourceAsStream("/imgs/faith/tile_2point.png")));
            }else if (i == 1){
                popeFavorTile2_imageView.setImage(new Image(
                        (popeTiles[i]) ? getClass().getResourceAsStream("/imgs/faith/tile_3pointUp.png") :
                                getClass().getResourceAsStream("/imgs/faith/tile_3point.png")));
            } else if (i == 2) {
                popeFavorTile3_imageView.setImage(new Image(
                        (popeTiles[i]) ? getClass().getResourceAsStream("/imgs/faith/tile_4pointUp.png") :
                                getClass().getResourceAsStream("/imgs/faith/tile_4point.png")));
            }
        }
    }

    /**
     * Shows local player's Pope progression tiles
     */
    public void showFaithBPope() {
        try {
            updateFaithBPope(getGUI().getLocalPlayerBoard().getFaithBoard().getPopeProgression());
        }catch(NotExistingNicknameException e){
            e.printStackTrace();
        }
    }

    /**
     * Sets to null all the leaderBoard's imageViews
     */
    private void resetLeaderBoard(){
        handLeader1_imageView.setImage(null);
        handLeader2_imageView.setImage(null);
        boardLeader1_imageView.setImage(null);
        boardLeader2_imageView.setImage(null);
        shelfLeader1_1_imageView.setImage(null);
        shelfLeader1_2_imageView.setImage(null);
        shelfLeader2_1_imageView.setImage(null);
        shelfLeader2_2_imageView.setImage(null);
    }

    /**
     * Updates imageViews of the leaders in the hand
     * @param leaderCards List of leaders to show
     */
    private void updateLeaderBHand(Deck leaderCards){
        handLeader1_imageView.setImage(null);
        handLeader2_imageView.setImage(null);
        for (Card card : leaderCards) {
            if (handLeader1_imageView.getImage() == null && boardLeader1_imageView.getImage() == null) {
                handLeader1_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/cost_"
                        + card.getID() + ".png")));
            } else {
                handLeader2_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/cost_"
                        + card.getID() + ".png")));
            }
        }
        showLeaderCheckBoxes();
    }

    /**
     * Sets imageViews of the leaders in the hand to be shown
     */
    public void setLeaderBHand(/*Deck leaderCards*/){ //TODO: controllare
        try {
            updateLeaderBHand(getGUI().getLocalPlayerBoard().getLeaderBoard().getHand());
        } catch (NotExistingNicknameException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets to null all the leaders' imageViews in the hand
     */
    private void hideLeaderHand(){
        handLeader1_imageView.setImage(null);
        handLeader2_imageView.setImage(null);
    }

    /**
     * Updates imageViews of the leaders on the board
     * @param leaderCards List of leaders to show
     */
    private void updateLeaderBBoard(Deck leaderCards){
        boardLeader1_imageView.setImage(null);
        boardLeader2_imageView.setImage(null);
        for(Card card : leaderCards) {
            if (boardLeader1_imageView.getImage() == null && handLeader1_imageView.getImage() == null) {
                boardLeader1_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/"
                        + card.getID() + ".png")));
                isFirstLeaderProduction = ((LeaderCard)card).getAbility() instanceof AbilityProduction;
                isFirstLeaderShelf = ((LeaderCard)card).getAbility() instanceof AbilityWarehouse;
                enableLeaderAbility();
            } else if (boardLeader2_imageView.getImage() == null && handLeader2_imageView.getImage() == null) {
                boardLeader2_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/"
                        + card.getID() + ".png")));
                isSecondLeaderProduction = ((LeaderCard)card).getAbility() instanceof AbilityProduction;
                isSecondLeaderShelf = ((LeaderCard)card).getAbility() instanceof AbilityWarehouse;
                enableLeaderAbility();
            }
        }
        showLeaderCheckBoxes();
    }

    /**
     * Sets imageViews of the leaders on the board to be shown
     */
    public void setLeaderBBoard(/*Deck leaderCards*/){
        try {
            updateLeaderBBoard(getGUI().getLocalPlayerBoard().getLeaderBoard().getBoard());
        } catch (NotExistingNicknameException e) {
            e.printStackTrace();
        }
    }

    /**
     * Enables leaders' productions or shelves if their abilities are active
     */
    private void enableLeaderAbility(){

        if(isFirstLeaderProduction) {
            shelfLeader1_1_imageView.setDisable(true);
            shelfLeader1_2_imageView.setDisable(true);
        }
        else if (isSecondLeaderProduction){
            shelfLeader2_1_imageView.setDisable(true);
            shelfLeader2_2_imageView.setDisable(true);
        }

        if(isFirstLeaderShelf) {
            shelfLeader1_1_imageView.setDisable(false);
            shelfLeader1_2_imageView.setDisable(false);
        }
        else if (isSecondLeaderShelf){
            shelfLeader2_1_imageView.setDisable(false);
            shelfLeader2_2_imageView.setDisable(false);
        }
    }

    /**
     * Updates labels of the strongbox
     * @param strongboxResources List of resources to update
     */
    private void updateStrongbox(List<Resource> strongboxResources) { //TODO: da controllare... perchè lo fa su un altro thread?
        resetStrongbox();
        for (Resource resource : strongboxResources){
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

    /**
     * Shows local player's strongbox
     */
    public void showStrongbox() {
        updateStrongbox(getGUI().getStrongboxResources());
    }

    /**
     * Sets to null all the strongbox's labels
     */
    private void resetStrongbox(){
        Platform.runLater(() -> coinStrongbox_label.setText("x 0"));
        Platform.runLater(() -> servantStrongbox_label.setText("x 0"));
        Platform.runLater(() -> stoneStrongbox_label.setText("x 0"));
        Platform.runLater(() -> shieldStrongbox_label.setText("x 0"));
    }

    /**
     * Updates imageViews of the warehouse
     * @param warehouseShelves List of shelves to update
     */
    private void updateWarehouse(List<Shelf> warehouseShelves) {
        shelves = warehouseShelves;
        Image image;
        String resPath = "/imgs/res/", resType;
        for(Shelf shelf : shelves){
            if(!shelf.isLeader()){ //basic
                switch (shelf.getLevel()){
                    case(1):
                        if(shelf.getResourceType() != ResourceType.WILDCARD) {
                            resType = shelf.getResourceType().toString()+".png";
                            shelfResL1_1_imageView.setImage(new Image(
                                    getClass().getResourceAsStream(resPath+resType)));
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
            else { //leader
                resType = shelf.getResourceType().toString()+".png";
                image = new Image(getClass().getResourceAsStream(resPath+resType));
                if(isFirstLeaderShelf && !isSecondLeaderShelf){
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
                else if(!isFirstLeaderShelf && isSecondLeaderShelf){
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
                } else{
                    if(getGUI().getLeaderBoard().size() == 2) {
                        if (shelf.getResources().getResourceType() == ((AbilityWarehouse) ((LeaderCard) getGUI().
                                getLeaderBoard().get(0)).getAbility()).getResourceType()) {
                            if (shelf.getResources().getQuantity() == 1) {
                                shelfLeader1_1_imageView.setImage(image);
                                shelfLeader1_2_imageView.setImage(null);
                            } else if (shelf.getResources().getQuantity() == 2) {
                                shelfLeader1_1_imageView.setImage(image);
                                shelfLeader1_2_imageView.setImage(image);
                            } else {
                                shelfLeader1_1_imageView.setImage(null);
                                shelfLeader1_2_imageView.setImage(null);
                            }
                        } else {
                            if (shelf.getResources().getQuantity() == 1) {
                                shelfLeader2_1_imageView.setImage(image);
                                shelfLeader2_2_imageView.setImage(null);
                            } else if (shelf.getResources().getQuantity() == 2) {
                                shelfLeader2_1_imageView.setImage(image);
                                shelfLeader2_2_imageView.setImage(image);
                            } else {
                                shelfLeader2_1_imageView.setImage(null);
                                shelfLeader2_2_imageView.setImage(null);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Sets to null all the warehouse's resources on each shelf
     */
    private void resetWarehouse(){
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

    /**
     * Shows local player's warehouse
     */
    public void showWarehouse() {
        updateWarehouse(getGUI().getWarehouseShelvesCopy());
    }

    /**
     * Sets the images of the warehouse's shelf given by parameter
     * @param level Shelf's level
     */
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

    /**
     * Sets the images of the leader's shelf given by parameters
     * @param index Index of the leader's shelf in the warehouse
     * @param leader Position of the leader in the board
     */
    private void setWhLeadersImages(int index, int leader){
        int resQuantity = shelves.get(index-1).getResources().getQuantity();
        Image image = new Image(getClass().getResourceAsStream("/imgs/res/" +
                shelves.get(index-1).getResources().getResourceType().toString().toLowerCase() + ".png"));
        switch (leader){
            case(1):
                shelfLeader1_1_imageView.setImage(null);
                shelfLeader1_2_imageView.setImage(null);
                if(resQuantity == 1){
                    shelfLeader1_1_imageView.setImage(image);
                    shelfLeader1_2_imageView.setImage(null);
                }
                else if(resQuantity == 2){
                    shelfLeader1_1_imageView.setImage(image);
                    shelfLeader1_2_imageView.setImage(image);
                }
                break;
            case (2):
                shelfLeader2_1_imageView.setImage(null);
                shelfLeader2_2_imageView.setImage(null);
                if(resQuantity == 1){
                    shelfLeader2_1_imageView.setImage(image);
                    shelfLeader2_2_imageView.setImage(null);
                }
                else if(resQuantity == 2) {
                    shelfLeader2_1_imageView.setImage(image);
                    shelfLeader2_2_imageView.setImage(image);
                }
                break;
            default:break;
        }
    }

    /**
     * Adds the resource given by parameter to discarded list
     * @param resourceType Resource's type
     */
    private void addToDiscardedResources(ResourceType resourceType){
        if(toDiscard==null)
            toDiscard = new ArrayList<>();
        toDiscard.add(new Resource(resourceType,1));
    }

    /**
     * Adds the resource given by parameter in the specified shelf
     * @param resourceType Resource's type
     * @param index Index of the shelf in the warehouse
     * @return Returns true if the resource has been added correctly, false otherwise
     */
    private boolean addToWarehouse(ResourceType resourceType, int index){
        if(shelves==null || shelves.isEmpty())
            shelves = getGUI().getWarehouseShelvesCopy();
        if(checkFreeSpace()){
            if (shelves.get(index).getResourceType().equals(ResourceType.WILDCARD)) //empty shelf
                return emptyShelfManagement(shelves.get(index), resourceType);
            else if (shelves.get(index).getResourceType().equals(resourceType)) //shelf with the same resource type
                return sameResTypeShelfManagement(shelves.get(index), resourceType);
            else if (!shelves.get(index).isLeader()) //shelf with different resource type
                return differentResTypeShelfManagement(shelves.get(index), resourceType);
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

    /**
     * Checks if there are free slots in the warehouse's shelves
     * @return Returns true if there are free slots, false otherwise
     */
    private boolean checkFreeSpace(){
        for (Shelf shelf : shelves) {
            if (shelf.getResourceType().equals(ResourceType.WILDCARD) ||
                    shelf.getLevel() > shelf.getResources().getQuantity()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Manages the placement of the specified resource on the empty shelf given by parameter
     * @param selectedShelf Shelf where to place the resource
     * @param resourceToPlace Resource's type
     * @return Returns true if the resource has been added correctly, false otherwise
     */
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

    /**
     * Manages the placement on the shelf with the same resource's type of the resource given by parameters
     * @param selectedShelf Shelf where to place the resource
     * @param resourceToPlace Resource's type
     * @return Returns true if the resource has been added correctly, false otherwise
     */
    private boolean sameResTypeShelfManagement(Shelf selectedShelf, ResourceType resourceToPlace){
        if (selectedShelf.getResources().getQuantity() <= selectedShelf.getLevel() - 1) { //shelf not completely full
            placeResource(selectedShelf, resourceToPlace);
            return true;
        }
        return false;
    }

    /**
     * Manages the placement on the shelf with different resource's type of the resource given by parameters
     * @param selectedShelf Shelf where to place the resource
     * @param resourceToPlace Resource's type
     * @return Returns true if the resource has been added correctly, false otherwise
     */
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

    /**
     * Checks if the resource's type given by parameter is unique among the warehouse's shelves
     * @param shelves List of shelves to control
     * @param resourceType Resource's type
     * @return Returns true if the type is unique, false otherwise
     */
    private boolean isResourceTypeUnique(List<Shelf> shelves, ResourceType resourceType) {
        return shelves.stream().noneMatch(shelf -> !shelf.isLeader() && shelf.getResourceType().equals(resourceType));
    }

    /**
     * Checks if the configuration of the resources which resource's type is given by parameter
     * is rearrangeable among the warehouse's shelves
     * @param shelves List of shelves to control
     * @param resourceType Resource's type
     * @return Returns true if it's rearrangeable, false otherwise
     */
    private boolean isShelfRearrangeable(List<Shelf> shelves, ResourceType resourceType){
        Shelf shelfWithResources = getShelfWithSameResource(shelves, resourceType);
        int totalLeaderShelves = 2*(int)(shelves.stream().filter(shelf -> shelf.isLeader() && shelf.getResourceType()
                .equals(resourceType)).count());
        return shelfWithResources.getResources().getQuantity()+1 <= 3 + totalLeaderShelves;
    }

    /**
     * Gets the shelf with the same resource's type of the resource given by parameter
     * @param shelves List of shelves where get the shelf from
     * @param resourceType Resourc's type
     * @return Returns the selected shelf, if present
     */
    private Shelf getShelfWithSameResource(List<Shelf> shelves, ResourceType resourceType){
        for(Shelf shelf : shelves){
            if(!shelf.isLeader() && shelf.getResourceType().equals(resourceType))
                return shelf;
        }
        return null;
    }

    /**
     * Places the resource on the shelf given by parameters
     * @param shelf Shelf where place the resource
     * @param resourceType Resource's type
     */
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

    /**
     * Empties the shelf given by parameter
     * @param shelf Shelf to empty
     */
    private void resetShelf(Shelf shelf) {
        if(!shelf.isLeader()) {
            shelf.setResourceType(ResourceType.WILDCARD);
            shelf.getResources().setResourceType(ResourceType.WILDCARD);
        }
        shelf.getResources().setQuantity(0);
    }

    /**
     * Adds quantity to place to the resource's label given by parameter
     * @param resourceType Resource's type to place
     * @param quantity Quantity amount to place
     */
    private void addQuantity(ResourceType resourceType, int quantity){
        setResToPlaceQuantity(resourceType, getResToPlaceQuantity(resourceType) + quantity);
    }

    /**
     * Sets to null al the imageViews of the shelf which level is given by parameter
     * @param level Level of the shelf to reset
     */
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

    /**
     * Sets to null all the shelves' imagesViews
     */
    private void resetShelvesImageView(){
        resetShelfImageView(1);
        resetShelfImageView(2);
        resetShelfImageView(3);
        shelfLeader1_1_imageView.setImage(null);
        shelfLeader1_2_imageView.setImage(null);
        shelfLeader2_1_imageView.setImage(null);
        shelfLeader2_2_imageView.setImage(null);
    }

    /**
     * Checks if there are no resources in the warehouse
     * @return Returns true if it's empty, false otherwise
     */
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

    /**
     * Resets to 0 all the labels to place resources
     */
    private void resetResLabels(){
        setResToPlaceQuantity(ResourceType.COIN,0);
        setResToPlaceQuantity(ResourceType.SERVANT,0);
        setResToPlaceQuantity(ResourceType.SHIELD,0);
        setResToPlaceQuantity(ResourceType.STONE,0);
    }

    /**
     * Shows all the checkBoxes if their cards are active
     */
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

    /**
     * Shows leaders' checkBoxes if their cards are active
     */
    private void showLeaderCheckBoxes(){
        leader1_checkBox.setVisible(handLeader1_imageView.getImage() != null && boardLeader1_imageView.getImage() == null ||
                isFirstLeaderProduction);
        leader2_checkBox.setVisible(handLeader2_imageView.getImage() != null && boardLeader2_imageView.getImage() == null ||
                isSecondLeaderProduction);
    }

    /**
     * Hides all the checkBoxes
     */
    public void hideCheckBoxes(){
        basicProduction_checkBox.setVisible(false);
        devSpace1_checkBox.setVisible(false);
        devSpace2_checkBox.setVisible(false);
        devSpace3_checkBox.setVisible(false);
        leader1_checkBox.setVisible(false);
        leader2_checkBox.setVisible(false);
    }

    /**
     * Unselects all the checkBoxes
     */
    private void resetCheckBoxes(){
        basicProduction_checkBox.setSelected(false);
        devSpace1_checkBox.setSelected(false);
        devSpace2_checkBox.setSelected(false);
        devSpace3_checkBox.setSelected(false);
        leader1_checkBox.setSelected(false);
        leader2_checkBox.setSelected(false);
    }

    /**
     * Checks if there are selected checkBoxes
     * @return Returns true if there are selected checkBoxes, false otherwise
     */
    private boolean checkSelectedCheckBoxes(){
        return basicProduction_checkBox.isSelected() || devSpace1_checkBox.isSelected() ||
                devSpace2_checkBox.isSelected() || devSpace3_checkBox.isSelected() ||
                isFirstLeaderProduction && leader1_checkBox.isSelected() ||
                isSecondLeaderProduction && leader2_checkBox.isSelected();
    }

    /**
     * Controls which productions have been activated
     */
    private void controlActivatedProductions(){
        List<Production> activatedProductions = new ArrayList<>();
        int specialProductionSize =  getGUI().getLeaderProductions().size();
        if (basicProduction_checkBox.isSelected())
            activatedProductions.add(getGUI().getBasicProduction());
        if (devSpace1_checkBox.isSelected())
            activatedProductions.add(((DevelopmentCard) getGUI().getDevelopmentBoard().get(0).get(0)).getProduction());
        if (devSpace2_checkBox.isSelected())
            activatedProductions.add(((DevelopmentCard) getGUI().getDevelopmentBoard().get(1).get(0)).getProduction());
        if (devSpace3_checkBox.isSelected())
            activatedProductions.add(((DevelopmentCard) getGUI().getDevelopmentBoard().get(2).get(0)).getProduction());
        if (isFirstLeaderProduction && leader1_checkBox.isSelected())
            activatedProductions.add(getGUI().getLeaderProductions().get(0));
        if (isSecondLeaderProduction && leader2_checkBox.isSelected())
            activatedProductions.add(getGUI().getLeaderProductions().get((specialProductionSize>1) ? 1 : 0));
        resolveWildcard(activatedProductions);
    }

    /**
     * Controls if there are productions' wildcards and calls method accordingly
     * Calls sendCanActivateProductionsMessage GUI's method if there are no wildcards, otherwise shows the popUp scene
     * @param productions
     */
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
                    setIsFirstPhase(false);
            ((WildcardResolverController) getGUIApplication().getController(SceneNames.RESOURCE_CHOICE_MENU)).
                    setIsMarbleAction(false, null);
            ((WildcardResolverController) getGUIApplication().getController(SceneNames.RESOURCE_CHOICE_MENU)).
                    resolveWildcard(consumedResolved, producedResolved, consumedWildcards, producedWildcards);
            getGUIApplication().setActiveScene(SceneNames.RESOURCE_CHOICE_MENU);
        }
        else getGUI().sendCanActivateProductionsMessage(productions);
    }

    /**
     * Shows or hide buttons on the playerBoard accordingly to the value given by parameter
     * @param isLocalBoard True if it's local playerBoard, false otherwise
     */
    private void showButtons(boolean isLocalBoard){
        activateProductions_button.setVisible(isLocalBoard);
        endTurn_button.setVisible(isLocalBoard);
        otherActions_label.setVisible(isLocalBoard);
        separator1.setVisible(isLocalBoard);
        viewOpponents_button.setVisible(isLocalBoard);
        rearrangeWarehouse_button.setVisible(isLocalBoard);
        market_button.setVisible(isLocalBoard);
        decks_button.setVisible(isLocalBoard);
        activateLeaderCard_button.setVisible(isLocalBoard);
        discardLeaderCard_button.setVisible(isLocalBoard);
        resToPlace_label.setVisible(isLocalBoard);
        separator2.setVisible(isLocalBoard);
        coinToPlace_imageView.setVisible(isLocalBoard);
        resToPlaceCoin_label.setVisible(isLocalBoard);
        servantToPlace_imageView.setVisible(isLocalBoard);
        resToPlaceServant_label.setVisible(isLocalBoard);
        shieldToPlace_imageView.setVisible(isLocalBoard);
        resToPlaceShield_label.setVisible(isLocalBoard);
        stoneToPlace_imageView.setVisible(isLocalBoard);
        resToPlaceStone_label.setVisible(isLocalBoard);
        leftArrow_button.setVisible(!isLocalBoard);
        rightArrow_button.setVisible(!isLocalBoard);
        goBoard_button.setVisible(!isLocalBoard);
        leaders_label.setVisible(true);
        separator3.setVisible(true);
    }

    /**
     * Shows previous opponent's board
     */
    public void showPrevPlayer() {
        viewOpponent(false);
    }

    /**
     * Shows next opponent's board
     */
    public void showNextPlayer() {
        viewOpponent(true);
    }

    /**
     * Goes back to local playerBoard restoring buttons
     */
    public void goBoard() {
        showButtons(true);
        leftArrow_button.setDisable(true);
        rightArrow_button.setDisable(true);
        player_label.setText(getGUI().getNickname());
        try {
            PlayerBoardView pbv = getGUI().getLocalPlayerBoard();
            inkwell_imageVIew.setVisible(pbv.isInkwell());
            updateFaithBoard(pbv.getFaithBoard().getFaith(), false);
            updateFaithBPope(pbv.getFaithBoard().getPopeProgression());
            updateStrongbox(pbv.getStrongbox().getResources());
            updateDevelopmentBSpaces(pbv.getDevelopmentBoard().getSpaces());
            resetLeaderBoard();
            updateLeaderBHand(pbv.getLeaderBoard().getHand());
            updateLeaderBBoard(pbv.getLeaderBoard().getBoard());
            updateWarehouse(pbv.getWarehouse().getShelves());
        } catch (NotExistingNicknameException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates leaders to show in the opponent's view
     * @param playerBoard Opponent's playerBoard to show
     */
    private void updateOpponentLeaderBoard(PlayerBoardView playerBoard){
        resetLeaderBoard();
        Deck leaderCards = playerBoard.getLeaderBoard().getBoard();
        if(leaderCards.size()>0) {
            boardLeader1_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/"
                    + leaderCards.get(0).getID() + ".png")));
            if(((LeaderCard)leaderCards.get(0)).getAbility() instanceof AbilityWarehouse)
                setOpponentLeaderShelf(((AbilityWarehouse)((LeaderCard)leaderCards.get(0)).getAbility()).
                        getResourceType(), playerBoard.getWarehouse().getShelves(), 1);
        }
        if(leaderCards.size()>1) {
            boardLeader2_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/leaderCards/"
                    + leaderCards.get(1).getID() + ".png")));
            if(((LeaderCard)leaderCards.get(0)).getAbility() instanceof AbilityWarehouse)
                setOpponentLeaderShelf(((AbilityWarehouse)((LeaderCard)leaderCards.get(0)).getAbility()).
                        getResourceType(), playerBoard.getWarehouse().getShelves(), 2);
        }
    }

    /**
     * Updates leader's shelves to show in the opponent's view
     * @param resourceType Resource's type to show
     * @param shelves List of opponent's warehouse's shelf
     * @param leaderPosition Position of the leader on the board
     */
    private void setOpponentLeaderShelf(ResourceType resourceType, List<Shelf> shelves, int leaderPosition){
        for(Shelf shelf : shelves){
            Image image = new Image(getClass().getResourceAsStream("/imgs/res/"+ resourceType + ".png"));
            if(shelf.isLeader() && shelf.getResourceType()==resourceType){
                if(leaderPosition==1) {
                    if (shelf.getResources().getQuantity() >= 1)
                        shelfLeader1_1_imageView.setImage(image);
                    if (shelf.getResources().getQuantity() >= 2)
                        shelfLeader1_2_imageView.setImage(image);
                }
                else {
                    if (shelf.getResources().getQuantity() >= 1)
                        shelfLeader2_1_imageView.setImage(image);
                    if (shelf.getResources().getQuantity() >= 2)
                        shelfLeader2_2_imageView.setImage(image);
                }
            }
        }
    }
}
