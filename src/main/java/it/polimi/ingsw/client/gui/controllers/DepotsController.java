package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.server.model.storage.*;
import javafx.event.ActionEvent;
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

    @FXML private Label coinToTake_label, servantToTake_label, shieldToTake_label, stoneToTake_label;
    @FXML private Button restore_button, confirm_button;
    @FXML private ImageView whShelf1_1_imageView, whShelf2_1_imageView, whShelf2_2_imageView, whShelf3_1_imageView,
            whShelf3_2_imageView,whShelf3_3_imageView, leaderShelf1_1_imageView, leaderShelf1_2_imageView,
            leaderShelf2_1_imageView, leaderShelf2_2_imageView, strongboxCoin_imageView, strongboxServant_imageView,
            strongboxShield_imageView, strongboxStone_imageView;

    public void restore(ActionEvent actionEvent) {
        setResourcesLabels(resourcesToTake);
    }

    public void confirm(ActionEvent actionEvent) {
        List<RequestResources> requestResources = new ArrayList<>();
        requestResources.add(new RequestResources(strongbox, StorageType.STRONGBOX));
        requestResources.add(new RequestResources(warehouse, StorageType.WAREHOUSE));
        requestResources.add(new RequestResources(leaders, StorageType.LEADER));
        getGUIApplication().closeSecondStage();
        if(action == 1) {
            getGUI().sendRequestResourcesDevMessage(requestResources);
        }else if (action == 2){
            //TODO: invio sendRequestResourcesProdMessage
        }
    }

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

    public void handleDragOver(DragEvent dragEvent) {
        if(dragEvent.getDragboard().hasImage())
            dragEvent.acceptTransferModes(TransferMode.MOVE);
    }

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

    public void handleDragDetectedWhShelf1_1(MouseEvent mouseEvent) {
        if (shelves.get(0).getResourceType()!=ResourceType.WILDCARD)
            handleDragDetected(mouseEvent, whShelf1_1_imageView, StorageType.WAREHOUSE, shelves.get(0).getResourceType());
    }

    public void handleDragDetectedWhShelf2_1(MouseEvent mouseEvent) {
        if (shelves.get(1).getResourceType()!=ResourceType.WILDCARD)
            handleDragDetected(mouseEvent, whShelf2_1_imageView, StorageType.WAREHOUSE, shelves.get(1).getResourceType());
    }

    public void handleDragDetectedWhShelf2_2(MouseEvent mouseEvent) {
        if (shelves.get(1).getResourceType()!=ResourceType.WILDCARD)
            handleDragDetected(mouseEvent, whShelf2_2_imageView, StorageType.WAREHOUSE, shelves.get(1).getResourceType());
    }

    public void handleDragDetectedWhShelf3_1(MouseEvent mouseEvent) {
        if (shelves.get(2).getResourceType()!=ResourceType.WILDCARD)
            handleDragDetected(mouseEvent, whShelf3_1_imageView, StorageType.WAREHOUSE, shelves.get(2).getResourceType());
    }

    public void handleDragDetectedWhShelf3_2(MouseEvent mouseEvent) {
        if (shelves.get(2).getResourceType()!=ResourceType.WILDCARD)
            handleDragDetected(mouseEvent, whShelf3_2_imageView, StorageType.WAREHOUSE, shelves.get(2).getResourceType());
    }

    public void handleDragDetectedWhShelf3_3(MouseEvent mouseEvent) {
        if (shelves.get(2).getResourceType()!=ResourceType.WILDCARD)
            handleDragDetected(mouseEvent, whShelf3_3_imageView, StorageType.WAREHOUSE, shelves.get(2).getResourceType());
    }

    public void handleDragDetectedLeaderShelf1_1(MouseEvent mouseEvent) {
        if (shelves.size()>=4 && shelves.get(3).getResources().getQuantity()>0)
            handleDragDetected(mouseEvent, leaderShelf1_1_imageView, StorageType.LEADER, shelves.get(3).getResourceType());
    }

    public void handleDragDetectedLeaderShelf1_2(MouseEvent mouseEvent) {
        if (shelves.size()>=4 && shelves.get(3).getResources().getQuantity()>0)
            handleDragDetected(mouseEvent, leaderShelf1_2_imageView, StorageType.LEADER, shelves.get(3).getResourceType());
    }

    public void handleDragDetectedLeaderShelf2_1(MouseEvent mouseEvent) {
        if (shelves.size()>=5 && shelves.get(4).getResources().getQuantity()>0)
            handleDragDetected(mouseEvent, leaderShelf2_1_imageView, StorageType.LEADER, shelves.get(4).getResourceType());
    }

    public void handleDragDetectedLeaderShelf2_2(MouseEvent mouseEvent) {
        if (shelves.size()>=5 && shelves.get(4).getResources().getQuantity()>0)
            handleDragDetected(mouseEvent, leaderShelf2_2_imageView, StorageType.LEADER, shelves.get(4).getResourceType());
    }

    public void handleDragDetectedStrongboxCoin(MouseEvent mouseEvent) {
        handleDragDetected(mouseEvent, strongboxCoin_imageView, StorageType.STRONGBOX, ResourceType.COIN);
    }

    public void handleDragDetectedStrongboxServant(MouseEvent mouseEvent) {
        handleDragDetected(mouseEvent, strongboxServant_imageView, StorageType.STRONGBOX, ResourceType.SERVANT);
    }

    public void handleDragDetectedStrongboxShield(MouseEvent mouseEvent) {
        handleDragDetected(mouseEvent, strongboxShield_imageView, StorageType.STRONGBOX, ResourceType.SHIELD);
    }

    public void handleDragDetectedStrongboxStone(MouseEvent mouseEvent) {
        handleDragDetected(mouseEvent, strongboxStone_imageView, StorageType.STRONGBOX, ResourceType.STONE);
    }

    public void setResourcesLabels(List<Resource> resources){
        resetLabels();
        resourcesToTake = new ArrayList<>(resources);
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

    private void resetLabels(){
        coinToTake_label.setText("0/0");
        servantToTake_label.setText("0/0");
        shieldToTake_label.setText("0/0");
        stoneToTake_label.setText("0/0");
    }

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

    private void incrementQuantity(ResourceType resourceType){
        setResourceQuantity(resourceType, getResourceQuantity(resourceType) + 1);
    }

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

    public int setResourceQuantity(ResourceType resourceType, int quantity) {
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
        return -1;
    }

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
                default: break;
            }
        }
    }

    private void setTotalResourcesToTake(){
        totalResourcesToTake = resourcesToTake.stream().map(Resource::getQuantity).reduce(0, Integer::sum);
    }

    private boolean isToTake(ResourceType resourceType){
        for(Resource resource : resourcesToTake)
            if(resource.getResourceType()==resourceType)
              return true;
        return false;
    }

    public void setAction(int action){
        this.action = action;
    }
}
