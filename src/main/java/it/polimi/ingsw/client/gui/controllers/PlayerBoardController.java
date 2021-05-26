package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.SceneNames;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import it.polimi.ingsw.server.model.storage.Shelf;
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

    private List<Shelf> shelves;
    private List<Resource> toDiscard;

    private List<ImageView> spaces, faithSpaces;
    private String resToPlace;
    private boolean isResToPlaceAction=false, mainActionPlayed=false, warehouseIsDisabled=false;
    private int srcWarehousePosition;

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
        endTurn_button.setDisable(!mainActionPlayed);
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

    public Label getPlayer_label(){return player_label;}

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
                setQuantity(ResourceType.COIN,getQuantity(ResourceType.COIN)-1);
                ((PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD)).setResToPlace("");
                break;
            case "servant":
                setQuantity(ResourceType.SERVANT,getQuantity(ResourceType.SERVANT)-1);
                ((PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD)).setResToPlace("");
                break;
            case "shield":
                setQuantity(ResourceType.SHIELD,getQuantity(ResourceType.SHIELD)-1);
                ((PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD)).setResToPlace("");
                break;
            case "stone":
                setQuantity(ResourceType.STONE,getQuantity(ResourceType.STONE)-1);
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
        faithSpaces.add( faithSpace14_imageView);
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
        if(getQuantity(ResourceType.COIN)==0 && getQuantity(ResourceType.SERVANT)==0 && getQuantity(ResourceType.SHIELD)==0
                && getQuantity(ResourceType.STONE)==0){
            isResToPlaceAction=false;
            //enableButtons();  dove lo sposto?
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

    public void confirm(ActionEvent actionEvent) {
        getGUI().sendShelvesConfigurationMessage(shelves,toDiscard);
        //TODO: serve tenere memorizzato prima dell'ack o rimanda tutto il server?
        toDiscard.clear();
        shelves.clear();
    }

    public void setDevelopmentBSpaces(List<List<Integer>> idList) {
        //TODO: stub
    }

    public void setFaithBFaith(int faith) {
        fillFaithSpaces();
        resetFaith();
        Image cross = new Image(getClass().getResourceAsStream("/imgs/faith/cross_red.png"));
        if(faith > 24) faith = 24;
        faithSpaces.get(faith).setImage(cross);
    }

    private void resetFaith(){  //TODO: si può fare in modo migliore?
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

    public void setWarehouse(List<Shelf> shelves) {
        //TODO: stub
    }

    private void addToDiscardedResources(ResourceType resourceType){
        if(toDiscard==null)
            toDiscard = new ArrayList<>();
        toDiscard.add(new Resource(resourceType,1));
    }

    private void addToWarehouse(ResourceType resourceType, int level){ //3,4 x leader
        if(shelves==null || shelves.isEmpty())
            shelves = getGUI().getWarehouseShelvesCopy();
        //TODO: gestire
    }
}
