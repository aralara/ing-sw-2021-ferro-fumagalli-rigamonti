package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.SceneNames;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class DecksBoardController extends GenericController {

    private List<ImageView> decks;

    @FXML private Button buyCard_button;
    @FXML private ImageView deckG3_imageView, deckB3_imageView, deckY3_imageView, deckP3_imageView,
            deckG2_imageView, deckB2_imageView, deckY2_imageView, deckP2_imageView,
            deckG1_imageView, deckB1_imageView, deckY1_imageView, deckP1_imageView, selectedDevCard_imageView;


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
            pbc.hideProductionCheckBoxes();
            pbc.hideLeaderCheckBoxes();
            //TODO: disabilitare drag strongbox, leader warehouse
            showAlert(Alert.AlertType.INFORMATION, "Buy card", "Complete buying action",
                    "Now you can drag and drop the card in the desired space");
            getGUIApplication().closeSecondStage();
            getGUIApplication().setActiveScene(SceneNames.CARD);
            //TODO: vedere se l'azione va a buon fine, altrimenti rimettere giusti i parametri
        }
    }

    public void goBack() {
        selectedDevCard_imageView.setImage(null);
        getGUIApplication().closeSecondStage();
    }

    private void clickedCard(ImageView clickedImageView){
        selectedDevCard_imageView.setImage(clickedImageView.getImage());
    }

    public void clickedG3() {
        clickedCard(deckG3_imageView);
    }

    public void clickedB3() {
        clickedCard(deckB3_imageView);
    }

    public void clickedY3() {
        clickedCard(deckY3_imageView);
    }

    public void clickedP3() {
        clickedCard(deckP3_imageView);
    }

    public void clickedG2() {
        clickedCard(deckG2_imageView);
    }

    public void clickedB2() {
        clickedCard(deckB2_imageView);
    }

    public void clickedY2() {
        clickedCard(deckY2_imageView);
    }

    public void clickedP2() {
        clickedCard(deckP2_imageView);
    }

    public void clickedG1() {
        clickedCard(deckG1_imageView);
    }

    public void clickedB1() {
        clickedCard(deckB1_imageView);
    }

    public void clickedY1() {
        clickedCard(deckY1_imageView);
    }

    public void clickedP1() {
        clickedCard(deckP1_imageView);
    }


    public void enableBuyCardAction(){
        buyCard_button.setDisable(false);
    }

    public void disableBuyCardAction(){
        buyCard_button.setDisable(true);
    }

    public void updateDevDecks(List<Integer> devCardsId){
        fillList();
        for (int i=0; i<decks.size(); i++) {
            String resPath = "/imgs/devCards/"+devCardsId.get(i)+".png";
            decks.get(i).setImage(new Image(getClass().getResourceAsStream(resPath)));
        }
    }

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

    public void setDevelopmentDeck(List<Integer> idList) {
        //TODO: stub (fare attenzione! update per development deck singolo)
    }
}
