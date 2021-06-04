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
    private boolean isFirstPhase=false, isProducedAction=false, areResolved=false, isMarbleAction=false;
    private int totalResources;

    @FXML private Label chooseResources_label, coin_label, servant_label, shield_label, stone_label;
    @FXML private Button coin_button, servant_button, shield_button, stone_button,
            confirm_button, restore_button, goBack_button;


    /**
     * Sets the isFirstPhase attribute
     * @param value New attribute value
     */
    public void setIsFirstPhase(boolean value){
        isFirstPhase=value;
    }

    /**
     * Sets the isMarbleAction attribute, if it's true stores the marbles to resolve
     * @param value New attribute value
     * @param resourcesToResolve Resources to resolve
     */
    public void setIsMarbleAction(boolean value, List<Resource> resourcesToResolve){
        isMarbleAction=value;
        if(isMarbleAction) {
            resources = new ArrayList<>();
            for (Resource resource : resourcesToResolve)
                if (resource.getResourceType() != ResourceType.WILDCARD)
                    resources.add(new Resource(resource.getResourceType(), resource.getQuantity()));
        }
    }

    /**
     * Sets the totalResources attribute
     * @param total New attribute value
     */
    public void setTotalResources(int total){
        totalResources=total;
    }

    /**
     * Sets the chooseResources_label's text
     * @param title New attribute value
     */
    public void setChooseResources_label(String title){
        this.chooseResources_label.setText(title);
    }

    /**
     * Stores the selected resource in the right list according to the current action
     * @param label Resource's label that needs to be updated
     * @param resourceType Resource's type
     */
    private void takeResource(Label label, ResourceType resourceType) {
        if (isFirstPhase || isMarbleAction) {
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

    /**
     * Stores 1 coin
     */
    public void takeCoin() {
        takeResource(coin_label,ResourceType.COIN);
    }

    /**
     * Stores 1 servant
     */
    public void takeServant() {
        takeResource(servant_label,ResourceType.SERVANT);
    }

    /**
     * Stores 1 shield
     */
    public void takeShield() {
        takeResource(shield_label,ResourceType.SHIELD);
    }

    /**
     * Stores 1 stone
     */
    public void takeStone() {
        takeResource(stone_label,ResourceType.STONE);
    }

    /**
     * Increments by one the the quantity of the resource given by parameter
     * @param resourceType Resource's type
     */
    private void incrementQuantity(ResourceType resourceType){
        setQuantity(resourceType, getQuantity(resourceType) + 1);
    }

    /**
     * Gets the quantity of the resource given by parameter
     * @param resourceType Resource's type
     * @return Returns resource's quantity
     */
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

    /**
     * Sets the quantity of the resource given by parameter
     * @param resourceType Resource's type
     * @param quantity Resource's quantity
     */
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

    /**
     * Gets the total amount of resources taken
     * @return Returns total amount
     */
    private int getTotalTakenResources(){
        return getQuantity(ResourceType.COIN)+getQuantity(ResourceType.SERVANT)+
                getQuantity(ResourceType.SHIELD)+getQuantity(ResourceType.STONE);
    }

    /**
     * Gets the total amount of the resource given by parameter
     * @param resources Resource's type
     * @return Returns total amount
     */
    private int getTotal(List<Resource> resources){
        return resources.stream().map(Resource::getQuantity).reduce(0, Integer::sum);
    }

    /**
     * Stores the resources, calls a specific method according to the current action,
     * then resets all the components, closes this stage and shows the main one
     * If is the first phase, sets resources that need to be placed
     * If is "marble action", call setResourcesToPlace method
     * If is "resolve wildcards action", call sendCanActivateProductionsMessage method
     */
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
        else if(isMarbleAction){
            getGUI().controlResourcesToPlace(resources);
            getGUIApplication().closePopUpStage();
        }
        else if(selectedConsumed!=null && !selectedConsumed.isEmpty()){
            consumedResolved.addAll(selectedConsumed);
            selectedConsumed=null;
            //restore();
            if(producedWildcards!=null && !producedWildcards.isEmpty()) {
                isProducedAction = true;
                setChooseResources_label("Choose " + producedWildcards.size()
                        + " resources to resolve produced wildcards");
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
            //restore();
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
        restore();
        enableButtons();
        goBack_button.setVisible(true);
    }

    /**
     * Restores all the components of the class
     */
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

    /**
     * Closes this stage, resets buttons and goes back to the main stage
     */
    public void goBack() {
        getGUIApplication().closePopUpStage();
        restore();
        PlayerBoardController pbc = ((PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD));
        pbc.enableButtons();
        pbc.setWarehouseIsDisabled(false);
        pbc.enableOpponents();
        getGUIApplication().setActiveScene(SceneNames.PLAYER_BOARD);
    }

    /**
     * Sets the lists of resources to resolve wildcards productions
     * @param consumedResolved List of resources to be consumed already correct
     * @param producedResolved List of resources to be produced already correct
     * @param consumedWildcards List of resources to be consumed to resolve
     * @param producedWildcards List of resources to be produced to resolve
     */
    public void resolveWildcard(List<Resource> consumedResolved, List<Resource> producedResolved,
                                List<Resource> consumedWildcards, List<Resource> producedWildcards){
        restore();
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

    /**
     * Enable only the buttons of the resources given by parameter
     * @param resourceTypes Resource's type
     */
    public void enableButtons(List<ResourceType> resourceTypes){
        disableButtons();
        for(ResourceType resourceType : resourceTypes) {
            if (resourceType == ResourceType.COIN)
                coin_button.setDisable(false);
            else if (resourceType == ResourceType.SERVANT)
                servant_button.setDisable(false);
            else if (resourceType == ResourceType.SHIELD)
                shield_button.setDisable(false);
            else if (resourceType == ResourceType.STONE)
                stone_button.setDisable(false);
        }
    }

    /**
     * Disable buttons to take resources
     */
    private void disableButtons(){
        coin_button.setDisable(true);
        servant_button.setDisable(true);
        shield_button.setDisable(true);
        stone_button.setDisable(true);
    }

    /**
     * Enable buttons to take resources
     */
    private void enableButtons(){
        coin_button.setDisable(false);
        servant_button.setDisable(false);
        shield_button.setDisable(false);
        stone_button.setDisable(false);
    }
}
