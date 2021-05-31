package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.SceneNames;
import it.polimi.ingsw.server.model.storage.Production;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class WildcardResolverController extends GenericController {

    private List<Resource> resources, consumedWildcards, producedWildcards, consumedResolved, producedResolved,
            selectedConsumed, selectedProduced;
    private boolean isFirstPhase=false, isProducedAction=false, areResolved=false;
    private int totalResources;

    @FXML private Label chooseResources_label, coin_label, servant_label, shield_label, stone_label;
    @FXML private Button confirm_button, restore_button, goBack_button;

    public void setIsFirstPhase(boolean value){
        isFirstPhase=value;
    }

    public void setTotalResources(int total){
        totalResources=total;
    }

    public void setChooseResources_label(String title){
        this.chooseResources_label.setText(title);
    }

    private void takeResource(Label label, ResourceType resourceType) {
        if (isFirstPhase) {
            if (getTotalTakenResources() < totalResources) {
                if (resources == null)
                    resources = new ArrayList<>();
                resources.add(new Resource(resourceType, 1));
                incrementQuantity(resourceType);
                label.setText("x " + getQuantity(resourceType));
                restore_button.setDisable(false);
            }
            if (getTotalTakenResources() == totalResources) {
                confirm_button.setDisable(false);
            }
        }
        else if(!isProducedAction && !consumedWildcards.isEmpty()){
            if (getTotalTakenResources() < getTotal(consumedWildcards)) {
                if (selectedConsumed == null)
                    selectedConsumed = new ArrayList<>();
                selectedConsumed.add(new Resource(resourceType, 1));
                incrementQuantity(resourceType);
                label.setText("x " + getQuantity(resourceType));
                restore_button.setDisable(false);
            }
            if (getTotalTakenResources() == getTotal(consumedWildcards)) {
                confirm_button.setDisable(false);
            }
        }
        else if(!producedWildcards.isEmpty()){
            if (getTotalTakenResources() < getTotal(producedWildcards)) {
                if (selectedProduced == null)
                    selectedProduced = new ArrayList<>();
                selectedProduced.add(new Resource(resourceType, 1));
                incrementQuantity(resourceType);
                label.setText("x " + getQuantity(resourceType));
                restore_button.setDisable(false);
            }
            if (getTotalTakenResources() == getTotal(producedWildcards)) {
                confirm_button.setDisable(false);
            }
        }
    }

    public void takeCoin() {
        takeResource(coin_label,ResourceType.COIN);
    }

    public void takeServant() {
        takeResource(servant_label,ResourceType.SERVANT);
    }

    public void takeShield() {
        takeResource(shield_label,ResourceType.SHIELD);
    }

    public void takeStone() {
        takeResource(stone_label,ResourceType.STONE);
    }

    private void incrementQuantity(ResourceType resourceType){
        setQuantity(resourceType, getQuantity(resourceType) + 1);
    }

    private int getQuantity(ResourceType resourceType) {
        if (resourceType == ResourceType.COIN) {
            return Integer.parseInt(coin_label.getText().substring(2));
        } else if (resourceType == ResourceType.SERVANT) {
            return Integer.parseInt(servant_label.getText().substring(2));
        }else if (resourceType == ResourceType.SHIELD) {
            return Integer.parseInt(shield_label.getText().substring(2));
        }else if (resourceType == ResourceType.STONE){
            return Integer.parseInt(stone_label.getText().substring(2));
        }
        return -1;
    }

    private void setQuantity(ResourceType resourceType, int quantity){
        if(resourceType == ResourceType.COIN) {
            coin_label.setText("x " + quantity);
        }else if (resourceType == ResourceType.SERVANT) {
            servant_label.setText("x "+quantity);
        }else if (resourceType == ResourceType.SHIELD) {
            shield_label.setText("x "+quantity);
        }else if (resourceType == ResourceType.STONE){
            stone_label.setText("x "+quantity);
        }
    }

    private int getTotalTakenResources(){
        return getQuantity(ResourceType.COIN)+getQuantity(ResourceType.SERVANT)+
                getQuantity(ResourceType.SHIELD)+getQuantity(ResourceType.STONE);
    }

    private int getTotal(List<Resource> resources){
        return resources.stream().map(Resource::getQuantity).reduce(0, Integer::sum);
    }

    public void confirm() {
        PlayerBoardController pbc = (PlayerBoardController) getGUIApplication().getController(SceneNames.PLAYER_BOARD);
        if(isFirstPhase) {
            for(Resource resource : resources) {
                int labelQuantity = pbc.getResToPlaceQuantity(resource.getResourceType());
                pbc.setResToPlaceQuantity(resource.getResourceType(), ++labelQuantity);
            }
            pbc.setIsResToPlace(true);
            getGUI().setResourcesToPlace(resources);
            getGUIApplication().closePopUpStage();
        }
        else if(selectedConsumed!=null && !selectedConsumed.isEmpty()){
            consumedResolved.addAll(selectedConsumed);
            selectedConsumed=null;
            restore();
            if(producedWildcards!=null && !producedWildcards.isEmpty()) {
                isProducedAction = true;
                setChooseResources_label("Choose " + producedWildcards.size() + " resources to resolve produced wildcards");
            }
            else
                areResolved = true;

        }
        else if(selectedProduced!=null && !selectedProduced.isEmpty()){
            producedResolved.addAll(selectedProduced);
            selectedProduced=null;
            areResolved = true;
        }
        if(!isFirstPhase && areResolved){
            List<Production> productions = new ArrayList<>();
            productions.add(new Production(consumedResolved, producedResolved));
            restore();
            isFirstPhase=false;
            isProducedAction=false;
            areResolved=false;
            this.consumedResolved=null;
            this.producedResolved=null;
            this.consumedWildcards=null;
            this.producedWildcards=null;
            getGUIApplication().closePopUpStage();
            getGUI().sendCanActivateProductionsMessage(productions);
        }
        confirm_button.setDisable(true);
        restore_button.setDisable(true);
        goBack_button.setVisible(true);
    }

    public void restore() {
        resources=null;
        selectedConsumed=null;
        selectedProduced=null;
        setQuantity(ResourceType.COIN, 0);
        setQuantity(ResourceType.SERVANT, 0);
        setQuantity(ResourceType.SHIELD, 0);
        setQuantity(ResourceType.STONE, 0);
        restore_button.setDisable(true);
        confirm_button.setDisable(true);
    }

    public void goBack() {
        getGUIApplication().closePopUpStage();
        restore();
        PlayerBoardController pbc = ((PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD));
        pbc.enableButtons();
        pbc.setWarehouseIsDisabled(false);
        pbc.enableOpponents();
        getGUIApplication().setActiveScene(SceneNames.PLAYER_BOARD);
    }

    public void resolveWildcard(List<Resource> consumedResolved, List<Resource> producedResolved,
                                List<Resource> consumedWildcards, List<Resource> producedWildcards){
        this.consumedResolved=consumedResolved;
        this.producedResolved=producedResolved;
        this.consumedWildcards=consumedWildcards;
        this.producedWildcards=producedWildcards;
        if(!consumedWildcards.isEmpty()) {
            setChooseResources_label("Choose " + getTotal(consumedWildcards) + " resources to resolve consumed wildcards");
            isProducedAction = false;
        }
        else {
            setChooseResources_label("Choose " + getTotal(producedWildcards) + " resources to resolve produced wildcards");
            isProducedAction = true;
        }
        goBack_button.setVisible(true);
    }
}
