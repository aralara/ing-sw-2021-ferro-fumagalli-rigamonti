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

    public CardColors getSelectedCardColor(){
        return selectedCardColor;
    }

    public int getSelectedLevel(){
        return selectedLevel;
    }

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
            getGUIApplication().closeSecondStage();
            getGUIApplication().setActiveScene(SceneNames.CARD);
        }
    }

    public void goBack() {
        selectedDevCard_imageView.setImage(null);
        getGUIApplication().closeSecondStage();
    }

    private void clickedCard(ImageView clickedImageView, CardColors cardColors, int level){
        selectedDevCard_imageView.setImage(clickedImageView.getImage());
        selectedCardColor = cardColors;
        selectedLevel = level;
    }

    public void clickedG3() {
        clickedCard(deckG3_imageView, CardColors.GREEN, 3);
    }

    public void clickedB3() {
        clickedCard(deckB3_imageView, CardColors.BLUE, 3);
    }

    public void clickedY3() {
        clickedCard(deckY3_imageView, CardColors.YELLOW, 3);
    }

    public void clickedP3() {
        clickedCard(deckP3_imageView, CardColors.PURPLE, 3);
    }

    public void clickedG2() {
        clickedCard(deckG2_imageView, CardColors.GREEN, 2);
    }

    public void clickedB2() {
        clickedCard(deckB2_imageView, CardColors.BLUE, 2);
    }

    public void clickedY2() {
        clickedCard(deckY2_imageView, CardColors.YELLOW, 2);
    }

    public void clickedP2() {
        clickedCard(deckP2_imageView, CardColors.PURPLE, 2);
    }

    public void clickedG1() {
        clickedCard(deckG1_imageView, CardColors.GREEN, 1);
    }

    public void clickedB1() {
        clickedCard(deckB1_imageView, CardColors.BLUE, 1);
    }

    public void clickedY1() {
        clickedCard(deckY1_imageView, CardColors.YELLOW, 1);
    }

    public void clickedP1() {
        clickedCard(deckP1_imageView, CardColors.PURPLE, 1);
    }


    public void enableBuyCardAction(){
        buyCard_button.setDisable(false);
    }

    public void disableBuyCardAction(){
        buyCard_button.setDisable(true);
    }

    /*public void updateDevDecks(List<Integer> devCardsId){
        fillList();
        for (int i=0; i<decks.size(); i++) {
            String resPath = "/imgs/devCards/"+devCardsId.get(i)+".png";
            decks.get(i).setImage(new Image(getClass().getResourceAsStream(resPath)));
        }
    }*/

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

    /*public void setDevelopmentDeck(List<Integer> idList) {
        //TODO: stub (fare attenzione! update per development deck singolo)
    }*/

    public void setDevelopmentDeck(List<Integer> idList) {
        fillList();
        List<DevelopmentDeckView> devDecks = getGUI().getDevelopmentDecks();
        for (int i=0; i<devDecks.size(); i++) {
            String resPath = "/imgs/devCards/"+devDecks.get(i).getDeck().get(0).getID()+".png";
            decks.get(i).setImage(new Image(getClass().getResourceAsStream(resPath)));
        }
    }
}
