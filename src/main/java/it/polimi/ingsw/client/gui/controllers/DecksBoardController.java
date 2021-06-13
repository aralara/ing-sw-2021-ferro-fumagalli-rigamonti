package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.SceneNames;
import it.polimi.ingsw.client.structures.DevelopmentDeckView;
import it.polimi.ingsw.server.model.cards.card.CardColors;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class DecksBoardController extends GenericController {

    private List<ImageView> decks;
    private CardColors selectedCardColor;
    private int selectedLevel;

    @FXML private Button buyCard_button;
    @FXML private ImageView deckG3_imageView, deckB3_imageView, deckY3_imageView, deckP3_imageView,
            deckG2_imageView, deckB2_imageView, deckY2_imageView, deckP2_imageView,
            deckG1_imageView, deckB1_imageView, deckY1_imageView, deckP1_imageView, selectedDevCard_imageView;

    /**
     * Gets the selectedCardColor attribute
     * @return Returns selectedCardColor
     */
    public CardColors getSelectedCardColor(){
        return selectedCardColor;
    }

    /**
     * Gets the selectedLevel attribute
     * @return Returns selectedLevel
     */
    public int getSelectedLevel(){
        return selectedLevel;
    }

    /**
     * Sets in the CardController the image of the selected card, closes this stage and opens the popUp one
     */
    public void buyCard() {
        if(selectedDevCard_imageView.getImage()!=null) {
            ((CardController) getGUIApplication().getController(SceneNames.CARD))
                    .setImage(selectedDevCard_imageView.getImage());
            disableBuyCardAction();
            ((MarketBoardController) getGUIApplication().getController(SceneNames.MARKET_BOARD)).disableMarketAction();
            PlayerBoardController pbc = ((PlayerBoardController) getGUIApplication().getController(SceneNames.PLAYER_BOARD));
            pbc.disableActivateProductionsAction();
            pbc.enableSpaces();
            pbc.setWarehouseIsDisabled(true);
            pbc.disableActivateLeaderAction();
            pbc.disableDiscardLeaderAction();
            pbc.hideCheckBoxes();
            showAlert(Alert.AlertType.INFORMATION, "Buy card", "Complete buying action",
                    "Now you can drag and drop the card in the desired space");
            selectedDevCard_imageView.setImage(null);
            getGUIApplication().closeSecondStage();
            getGUIApplication().setActiveScene(SceneNames.CARD);
        }
    }

    /**
     * Goes back to the main stage closing this one
     */
    public void goBack() {
        selectedDevCard_imageView.setImage(null);
        getGUIApplication().closeSecondStage();
    }

    /**
     * Sets the attributes of the class according to the clicked card given by parameter
     * @param clickedImageView ImageView of the clicked card
     * @param cardColors Color of the clicked card
     * @param level Level of the clicked card
     */
    private void clickedCard(ImageView clickedImageView, CardColors cardColors, int level){
        selectedDevCard_imageView.setImage(clickedImageView.getImage());
        selectedCardColor = cardColors;
        selectedLevel = level;
    }

    /**
     * Sets the attributes of the class according to the card at the top of the green deck, third level
     */
    public void clickedG3() {
        clickedCard(deckG3_imageView, CardColors.GREEN, 3);
    }

    /**
     * Sets the attributes of the class according to the card at the top of the blue deck, third level
     */
    public void clickedB3() {
        clickedCard(deckB3_imageView, CardColors.BLUE, 3);
    }

    /**
     * Sets the attributes of the class according to the card at the top of the yellow deck, third level
     */
    public void clickedY3() {
        clickedCard(deckY3_imageView, CardColors.YELLOW, 3);
    }

    /**
     * Sets the attributes of the class according to the card at the top of the purple deck, third level
     */
    public void clickedP3() {
        clickedCard(deckP3_imageView, CardColors.PURPLE, 3);
    }

    /**
     * Sets the attributes of the class according to the card at the top of the green deck, second level
     */
    public void clickedG2() {
        clickedCard(deckG2_imageView, CardColors.GREEN, 2);
    }

    /**
     * Sets the attributes of the class according to the card at the top of the blue deck, second level
     */
    public void clickedB2() {
        clickedCard(deckB2_imageView, CardColors.BLUE, 2);
    }

    /**
     * Sets the attributes of the class according to the card at the top of the yellow deck, second level
     */
    public void clickedY2() {
        clickedCard(deckY2_imageView, CardColors.YELLOW, 2);
    }

    /**
     * Sets the attributes of the class according to the card at the top of the purple deck, second level
     */
    public void clickedP2() {
        clickedCard(deckP2_imageView, CardColors.PURPLE, 2);
    }

    /**
     * Sets the attributes of the class according to the card at the top of the green deck, first level
     */
    public void clickedG1() {
        clickedCard(deckG1_imageView, CardColors.GREEN, 1);
    }

    /**
     * Sets the attributes of the class according to the card at the top of the blue deck, first level
     */
    public void clickedB1() {
        clickedCard(deckB1_imageView, CardColors.BLUE, 1);
    }

    /**
     * Sets the attributes of the class according to the card at the top of the yellow deck, first level
     */
    public void clickedY1() {
        clickedCard(deckY1_imageView, CardColors.YELLOW, 1);
    }

    /**
     * Sets the attributes of the class according to the card at the top of the purple deck, first level
     */
    public void clickedP1() {
        clickedCard(deckP1_imageView, CardColors.PURPLE, 1);
    }

    /**
     * Enables buyCard button
     */
    public void enableBuyCardAction(){
        buyCard_button.setDisable(false);
    }

    /**
     * Disables buyCard button
     */
    public void disableBuyCardAction(){
        buyCard_button.setDisable(true);
    }

    /**
     * Loads development decks' imageViews in the decks list
     */
    private void fillList(){
        decks = new ArrayList<>();
        decks.add(deckG1_imageView);
        decks.add(deckP1_imageView);
        decks.add(deckB1_imageView);
        decks.add(deckY1_imageView);
        decks.add(deckG2_imageView);
        decks.add(deckP2_imageView);
        decks.add(deckB2_imageView);
        decks.add(deckY2_imageView);
        decks.add(deckG3_imageView);
        decks.add(deckP3_imageView);
        decks.add(deckB3_imageView);
        decks.add(deckY3_imageView);
    }

    /**
     * Shows decks updating all the development decks
     */
    public void showDevelopmentDeck() {
        fillList();
        List<DevelopmentDeckView> devDecks = getGUI().getDevelopmentDecks().getDecks();
        for (int i=0; i<devDecks.size(); i++) {
            if(!devDecks.get(i).getDeck().isEmpty()){
                String resPath = "/imgs/devCards/"+devDecks.get(i).getDeck().get(0).getID()+".png";
                decks.get(i).setImage(new Image(getClass().getResourceAsStream(resPath)));
            }
            else decks.get(i).setImage(null);
        }
    }
}
