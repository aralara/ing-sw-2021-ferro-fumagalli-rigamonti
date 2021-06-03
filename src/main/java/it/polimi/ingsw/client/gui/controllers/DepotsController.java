package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.SceneNames;
import it.polimi.ingsw.server.model.cards.card.DevelopmentCard;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.utils.messages.client.RequestResourcesDevMessage;
import it.polimi.ingsw.utils.messages.client.RequestResourcesProdMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;

import java.util.ArrayList;
import java.util.List;

public class DepotsController extends GenericController {

    private List<Shelf> shelves;
    private List<Resource> resourcesToTake, strongbox, warehouse, leaders;
    private ResourceType resToTakeType;
    private StorageType storageType;
    private int totalResourcesToTake, totalTakenResources, action;

    private DevelopmentCard devCardToBuy;
    private int spaceToPlace;
    private List<Production> productionsToActivate;

    @FXML private Label coinToTake_label, servantToTake_label, shieldToTake_label, stoneToTake_label;
    @FXML private Button restore_button, confirm_button;
    @FXML private ImageView whShelf1_1_imageView, whShelf2_1_imageView, whShelf2_2_imageView, whShelf3_1_imageView,
            whShelf3_2_imageView,whShelf3_3_imageView, leaderShelf1_1_imageView, leaderShelf1_2_imageView,
            leaderShelf2_1_imageView, leaderShelf2_2_imageView, strongboxCoin_imageView, strongboxServant_imageView,
            strongboxShield_imageView, strongboxStone_imageView;

    /**
     * Restores all the components of the class
     */
    public void restore() {
        setResourcesLabels(resourcesToTake);
    }

    /**
     * Confirms and sends a message to the server according to the value of the action attribute
     * If action is 1, sends a message with the resources to buy a card;
     * if action is 2, sends a message with the resources to activate productions
     */
    public void confirm() {
        List<RequestResources> requestResources = new ArrayList<>();
        requestResources.add(new RequestResources(strongbox, StorageType.STRONGBOX));
        requestResources.add(new RequestResources(warehouse, StorageType.WAREHOUSE));
        requestResources.add(new RequestResources(leaders, StorageType.LEADER));
        ((PlayerBoardController) getGUIApplication().getController(SceneNames.PLAYER_BOARD)).setWarehouseIsDisabled(false);
        getGUIApplication().closeSecondStage();
        if(action == 1)
            getGUI().getMessageHandler().sendClientMessage(
                    new RequestResourcesDevMessage(devCardToBuy, spaceToPlace, requestResources));
        else if (action == 2)
            getGUI().getMessageHandler().sendClientMessage(
                    new RequestResourcesProdMessage(productionsToActivate, requestResources));
    }

    /**
     * Handles drag dropped events of the depots
     */
    public void handleDragDropped() {
        if(totalTakenResources<totalResourcesToTake && isToTake(resToTakeType)) {
            incrementQuantity(resToTakeType);
            totalTakenResources++;
            if (storageType == StorageType.STRONGBOX) {
                strongbox.add(new Resource(resToTakeType, 1));
            }
            if (storageType == StorageType.WAREHOUSE) {
                warehouse.add(new Resource(resToTakeType, 1));
            }
            if (storageType == StorageType.LEADER) {
                leaders.add(new Resource(resToTakeType, 1));
            }
            restore_button.setVisible(true);
        }
        if(totalTakenResources==totalResourcesToTake)
            confirm_button.setVisible(true);
    }

    /**
     * Handles drag over events of the depots
     * @param dragEvent Drag event
     */
    public void handleDragOver(DragEvent dragEvent) {
        if(dragEvent.getDragboard().hasImage())
            dragEvent.acceptTransferModes(TransferMode.MOVE);
    }

    /**
     * * Handles drag detected events of the depots
     * @param mouseEvent Mouse event
     * @param imageView ImageView to drag
     * @param storageType Storage's type of the dragged resource
     * @param resourceType Resource's type of the dragged resource
     */
    private void handleDragDetected(MouseEvent mouseEvent, ImageView imageView,
                                    StorageType storageType, ResourceType resourceType) {
        Dragboard dragboard = imageView.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putImage(imageView.getImage());
        dragboard.setContent(clipboardContent);
        this.storageType = storageType;
        this.resToTakeType = resourceType;
        mouseEvent.consume();
    }

    /**
     * Handles drag detected event of the first warehouse's shelf
     * @param mouseEvent Mouse event
     */
    public void handleDragDetectedWhShelf1_1(MouseEvent mouseEvent) {
        if (shelves.get(0).getResourceType()!=ResourceType.WILDCARD)
            handleDragDetected(mouseEvent, whShelf1_1_imageView, StorageType.WAREHOUSE, shelves.get(0).getResourceType());
    }

    /**
     * Handles drag detected event of the second warehouse's shelf, first slot
     * @param mouseEvent Mouse event
     */
    public void handleDragDetectedWhShelf2_1(MouseEvent mouseEvent) {
        if (shelves.get(1).getResourceType()!=ResourceType.WILDCARD)
            handleDragDetected(mouseEvent, whShelf2_1_imageView, StorageType.WAREHOUSE, shelves.get(1).getResourceType());
    }

    /**
     * Handles drag detected event of the second warehouse's shelf, second slot
     * @param mouseEvent Mouse event
     */
    public void handleDragDetectedWhShelf2_2(MouseEvent mouseEvent) {
        if (shelves.get(1).getResourceType()!=ResourceType.WILDCARD)
            handleDragDetected(mouseEvent, whShelf2_2_imageView, StorageType.WAREHOUSE, shelves.get(1).getResourceType());
    }

    /**
     * Handles drag detected event of the third warehouse's shelf, first slot
     * @param mouseEvent Mouse event
     */
    public void handleDragDetectedWhShelf3_1(MouseEvent mouseEvent) {
        if (shelves.get(2).getResourceType()!=ResourceType.WILDCARD)
            handleDragDetected(mouseEvent, whShelf3_1_imageView, StorageType.WAREHOUSE, shelves.get(2).getResourceType());
    }

    /**
     * Handles drag detected event of the third warehouse's shelf, second slot
     * @param mouseEvent Mouse event
     */
    public void handleDragDetectedWhShelf3_2(MouseEvent mouseEvent) {
        if (shelves.get(2).getResourceType()!=ResourceType.WILDCARD)
            handleDragDetected(mouseEvent, whShelf3_2_imageView, StorageType.WAREHOUSE, shelves.get(2).getResourceType());
    }

    /**
     * Handles drag detected event of the third warehouse's shelf, third slot
     * @param mouseEvent Mouse event
     */
    public void handleDragDetectedWhShelf3_3(MouseEvent mouseEvent) {
        if (shelves.get(2).getResourceType()!=ResourceType.WILDCARD)
            handleDragDetected(mouseEvent, whShelf3_3_imageView, StorageType.WAREHOUSE, shelves.get(2).getResourceType());
    }

    /**
     * Handles drag detected event of the first leader's shelf, first slot
     * @param mouseEvent Mouse event
     */
    public void handleDragDetectedLeaderShelf1_1(MouseEvent mouseEvent) {
        if (shelves.size()>=4 && shelves.get(3).getResources().getQuantity()>0)
            handleDragDetected(mouseEvent, leaderShelf1_1_imageView, StorageType.LEADER, shelves.get(3).getResourceType());
    }

    /**
     * Handles drag detected event of the first leader's shelf, second slot
     * @param mouseEvent Mouse event
     */
    public void handleDragDetectedLeaderShelf1_2(MouseEvent mouseEvent) {
        if (shelves.size()>=4 && shelves.get(3).getResources().getQuantity()>0)
            handleDragDetected(mouseEvent, leaderShelf1_2_imageView, StorageType.LEADER, shelves.get(3).getResourceType());
    }

    /**
     * Handles drag detected event of the second leader's shelf, first slot
     * @param mouseEvent Mouse event
     */
    public void handleDragDetectedLeaderShelf2_1(MouseEvent mouseEvent) {
        if (shelves.size()>=5 && shelves.get(4).getResources().getQuantity()>0)
            handleDragDetected(mouseEvent, leaderShelf2_1_imageView, StorageType.LEADER, shelves.get(4).getResourceType());
    }

    /**
     * Handles drag detected event of the second leader's shelf, second slot
     * @param mouseEvent Mouse event
     */
    public void handleDragDetectedLeaderShelf2_2(MouseEvent mouseEvent) {
        if (shelves.size()>=5 && shelves.get(4).getResources().getQuantity()>0)
            handleDragDetected(mouseEvent, leaderShelf2_2_imageView, StorageType.LEADER, shelves.get(4).getResourceType());
    }

    /**
     * Handles drag detected event of the coin in the strongbox
     * @param mouseEvent Mouse event
     */
    public void handleDragDetectedStrongboxCoin(MouseEvent mouseEvent) {
        handleDragDetected(mouseEvent, strongboxCoin_imageView, StorageType.STRONGBOX, ResourceType.COIN);
    }

    /**
     * Handles drag detected event of the servant in the strongbox
     * @param mouseEvent Mouse event
     */
    public void handleDragDetectedStrongboxServant(MouseEvent mouseEvent) {
        handleDragDetected(mouseEvent, strongboxServant_imageView, StorageType.STRONGBOX, ResourceType.SERVANT);
    }

    /**
     * Handles drag detected event of the shield in the strongbox
     * @param mouseEvent Mouse event
     */
    public void handleDragDetectedStrongboxShield(MouseEvent mouseEvent) {
        handleDragDetected(mouseEvent, strongboxShield_imageView, StorageType.STRONGBOX, ResourceType.SHIELD);
    }

    /**
     * Handles drag detected event of the stone in the strongbox
     * @param mouseEvent Mouse event
     */
    public void handleDragDetectedStrongboxStone(MouseEvent mouseEvent) {
        handleDragDetected(mouseEvent, strongboxStone_imageView, StorageType.STRONGBOX, ResourceType.STONE);
    }

    /**
     * Sets labels and initializes attributes
     * @param resources List of resources to ask
     */
    public void setResourcesLabels(List<Resource> resources){
        resetLabels();
        resourcesToTake = new ArrayList<>(resources);
        Storage.aggregateResources(resourcesToTake);
        setTotalResourcesToTake();
        for(Resource resource : resourcesToTake)
            setResourcesQuantityToTake(resource.getResourceType(), resource.getQuantity());
        setWarehouse();
        strongbox=new ArrayList<>();
        warehouse=new ArrayList<>();
        leaders=new ArrayList<>();
        restore_button.setVisible(false);
        confirm_button.setVisible(false);
        totalTakenResources=0;
    }

    /**
     * Resets labels to 0 resources required
     */
    private void resetLabels(){
        coinToTake_label.setText("0/0");
        servantToTake_label.setText("0/0");
        shieldToTake_label.setText("0/0");
        stoneToTake_label.setText("0/0");
    }

    /**
     * Sets labels with the total amounts of the specified resource
     * @param resourceType Resource's type
     * @param quantity Resource's quantity
     */
    private void setResourcesQuantityToTake(ResourceType resourceType, int quantity){
        if(resourceType == ResourceType.COIN) {
            coinToTake_label.setText("0/" + quantity);
        }else if (resourceType == ResourceType.SERVANT) {
            servantToTake_label.setText("0/"+quantity);
        }else if (resourceType == ResourceType.SHIELD) {
            shieldToTake_label.setText("0/"+quantity);
        }else if (resourceType == ResourceType.STONE){
            stoneToTake_label.setText("0/"+quantity);
        }
    }

    /**
     * Increments by one the the quantity of the resource given by parameter
     * @param resourceType Resource's type
     */
    private void incrementQuantity(ResourceType resourceType){
        setResourceQuantity(resourceType, getResourceQuantity(resourceType) + 1);
    }

    /**
     * Gets the quantity of the resource given by parameter
     * @param resourceType Resource's type
     * @return Returns resource's quantity
     */
    public int getResourceQuantity(ResourceType resourceType) {
        if (resourceType == ResourceType.COIN) {
            int index = coinToTake_label.getText().indexOf("/");
            return Integer.parseInt(coinToTake_label.getText().substring(0,index));
        } else if (resourceType == ResourceType.SERVANT) {
            int index = servantToTake_label.getText().indexOf("/");
            return Integer.parseInt(servantToTake_label.getText().substring(0,index));
        }else if (resourceType == ResourceType.SHIELD) {
            int index = shieldToTake_label.getText().indexOf("/");
            return Integer.parseInt(shieldToTake_label.getText().substring(0,index));
        }else if (resourceType == ResourceType.STONE){
            int index = stoneToTake_label.getText().indexOf("/");
            return Integer.parseInt(stoneToTake_label.getText().substring(0,index));
        }
        return -1;
    }

    /**
     * Sets the quantity of the resource given by parameter
     * @param resourceType Resource's type
     * @param quantity Resource's quantity
     */
    public void setResourceQuantity(ResourceType resourceType, int quantity) {
        if (resourceType == ResourceType.COIN) {
            int index = coinToTake_label.getText().indexOf("/");
            String total = coinToTake_label.getText().substring(index);
            coinToTake_label.setText(quantity + total);
        } else if (resourceType == ResourceType.SERVANT) {
            int index = servantToTake_label.getText().indexOf("/");
            String total = servantToTake_label.getText().substring(index);
            servantToTake_label.setText(quantity + total);
        }else if (resourceType == ResourceType.SHIELD) {
            int index = shieldToTake_label.getText().indexOf("/");
            String total = shieldToTake_label.getText().substring(index);
            shieldToTake_label.setText(quantity + total);
        }else if (resourceType == ResourceType.STONE){
            int index = stoneToTake_label.getText().indexOf("/");
            String total = stoneToTake_label.getText().substring(index);
            stoneToTake_label.setText(quantity + total);
        }
    }

    /**
     * Sets warehouse's shelves' imageViews
     */
    private void setWarehouse(){
        shelves = getGUI().getWarehouseShelvesCopy();
        Image image;
        Shelf shelf;
        String resPath = "/imgs/res/", resType;
        for(int i = 0; i < shelves.size(); i++){
            shelf = shelves.get(i);
            switch (i){
                case(0):
                    if(shelf.getResourceType() != ResourceType.WILDCARD) {
                        resType = shelf.getResourceType().toString()+".png";
                        whShelf1_1_imageView.setImage(new Image(getClass().getResourceAsStream(resPath+resType)));
                    }
                    else
                        whShelf1_1_imageView.setImage(null);
                    break;
                case(1):
                    if(shelf.getResourceType() != ResourceType.WILDCARD) {
                        resType = shelf.getResourceType().toString()+".png";
                        image = new Image(getClass().getResourceAsStream(resPath+resType));
                        if(shelf.getResources().getQuantity()==1){
                            whShelf2_1_imageView.setImage(image);
                            whShelf2_2_imageView.setImage(null);
                        }
                        else if(shelf.getResources().getQuantity()==2) {
                            whShelf2_1_imageView.setImage(image);
                            whShelf2_2_imageView.setImage(image);
                        }
                    }
                    else {
                        whShelf2_1_imageView.setImage(null);
                        whShelf2_2_imageView.setImage(null);
                    }
                    break;
                case(2):
                    if(shelf.getResourceType() != ResourceType.WILDCARD) {
                        resType = shelf.getResourceType().toString()+".png";
                        image = new Image(getClass().getResourceAsStream(resPath+resType));
                        if(shelf.getResources().getQuantity()==1){
                            whShelf3_1_imageView.setImage(image);
                            whShelf3_2_imageView.setImage(null);
                            whShelf3_3_imageView.setImage(null);
                        }
                        else if(shelf.getResources().getQuantity()==2) {
                            whShelf3_1_imageView.setImage(image);
                            whShelf3_2_imageView.setImage(image);
                            whShelf3_3_imageView.setImage(null);
                        }
                        else if(shelf.getResources().getQuantity()==3) {
                            whShelf3_1_imageView.setImage(image);
                            whShelf3_2_imageView.setImage(image);
                            whShelf3_3_imageView.setImage(image);
                        }
                    }
                    else {
                        whShelf3_1_imageView.setImage(null);
                        whShelf3_2_imageView.setImage(null);
                        whShelf3_3_imageView.setImage(null);
                    }
                    break;
                case(3):
                    if(shelf.getResourceType() != ResourceType.WILDCARD) {
                        resType = shelf.getResourceType().toString()+".png";
                        image = new Image(getClass().getResourceAsStream(resPath+resType));
                        if(shelf.getResources().getQuantity()==1){
                            leaderShelf1_1_imageView.setImage(image);
                            leaderShelf1_2_imageView.setImage(null);
                        }
                        else if(shelf.getResources().getQuantity()==2) {
                            leaderShelf1_1_imageView.setImage(image);
                            leaderShelf1_2_imageView.setImage(image);
                        }
                        else {
                            leaderShelf1_1_imageView.setImage(null);
                            leaderShelf1_2_imageView.setImage(null);
                        }
                    }
                    break;
                case(4):
                    if(shelf.getResourceType() != ResourceType.WILDCARD) {
                        resType = shelf.getResourceType().toString()+".png";
                        image = new Image(getClass().getResourceAsStream(resPath+resType));
                        if(shelf.getResources().getQuantity()==1){
                            leaderShelf2_1_imageView.setImage(image);
                            leaderShelf2_2_imageView.setImage(null);
                        }
                        else if(shelf.getResources().getQuantity()==2) {
                            leaderShelf2_1_imageView.setImage(image);
                            leaderShelf2_2_imageView.setImage(image);
                        }
                        else {
                            leaderShelf2_1_imageView.setImage(null);
                            leaderShelf2_2_imageView.setImage(null);
                        }
                    }
                    break;
                default: break;
            }
        }
    }

    /**
     * Calculates and sets total resources to take
     */
    private void setTotalResourcesToTake(){
        totalResourcesToTake = resourcesToTake.stream().map(Resource::getQuantity).reduce(0, Integer::sum);
    }

    /**
     * Checks if the resource given by parameter needs to be taken
     * @param resourceType Resource's type
     * @return True if the resource needs to be taken, false otherwise
     */
    private boolean isToTake(ResourceType resourceType){
        for(Resource resource : resourcesToTake)
            if(resource.getResourceType()==resourceType && getResourceQuantity(resourceType)+1 <= resource.getQuantity())
              return true;
        return false;
    }

    /**
     * Sets the action attribute
     * @param action New attribute value
     */
    public void setAction(int action){
        this.action = action;
    }

    /**
     * Sets the devCardToBuy attribute
     * @param devCardToBuy New attribute value
     */
    public void setDevCardToBuy(DevelopmentCard devCardToBuy) {
        this.devCardToBuy = devCardToBuy;
    }

    /**
     * Sets the spaceToPlace attribute
     * @param spaceToPlace New attribute value
     */
    public void setSpaceToPlace(int spaceToPlace) {
        this.spaceToPlace = spaceToPlace;
    }

    /**
     * Sets the productionsToActivate attribute
     * @param productionsToActivate New attribute value
     */
    public void setProductionsToActivate(List<Production> productionsToActivate) {
        this.productionsToActivate = productionsToActivate;
    }
}
