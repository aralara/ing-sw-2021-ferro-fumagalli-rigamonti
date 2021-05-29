package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.SceneNames;
import it.polimi.ingsw.client.structures.MarketView;
import it.polimi.ingsw.server.model.market.Marble;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.utils.Constants.MARKET_COLUMN_SIZE;
import static it.polimi.ingsw.utils.Constants.MARKET_ROW_SIZE;

public class MarketBoardController extends GenericController {

    private List<ImageView> marblePositionList;
    private int selectedRow=0, selectedColumn=0;

    @FXML private Button takeResources_button;
    @FXML private GridPane marbleMatrix_gridPane;
    @FXML private ImageView marbleR0C4_imageView, marbleR1C4_imageView, marbleR2C4_imageView, marbleR3C0_imageView,
            marbleR3C1_imageView, marbleR3C2_imageView, marbleR3C3_imageView, floatingMarble_imageView;


    public void takeMarketResources() {
        if((selectedRow==3 && selectedColumn>=0 && selectedColumn<=3) ||
                (selectedColumn==4 && selectedRow>=0 && selectedRow<=2)) {
            int row = selectedRow==3?-1:selectedRow;
            int col = selectedColumn==4?-1:selectedColumn;
            selectedRow=0;
            selectedColumn=0;
            disableMarketAction();
            ((DecksBoardController)getGUIApplication().getController(SceneNames.DECKS_BOARD)).disableBuyCardAction();
            PlayerBoardController pbc = ((PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD));
            pbc.disableActivateProductionsAction();
            pbc.disableActivateLeaderAction();
            pbc.disableDiscardLeaderAction();
            pbc.setWarehouseIsDisabled(false);
            pbc.setMainActionPlayed(true);
            pbc.setIsResToPlace(true);
            pbc.hideCheckBoxes();
            //TODO: disabilitare drag strongbox, abilitare leader warehouse
            resetAll();
            getGUI().sendMarketMessage(row, col);
            getGUIApplication().closeSecondStage();
        }
    }

    public void goBack() {
        if((selectedRow==3 && selectedColumn>=0 && selectedColumn<=3) ||
                (selectedColumn==4 && selectedRow>=0 && selectedRow<=2)) {
            fillList();
            Image image = marblePositionList.get(getListPosition()).getImage();
            floatingMarble_imageView.setImage(image);
            marblePositionList.get(getListPosition()).setImage(null);
        }
        getGUIApplication().closeSecondStage();
    }

    private void handleDragDetected(MouseEvent mouseEvent, ImageView srcImageView){
        Dragboard dragboard = srcImageView.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putImage(srcImageView.getImage());
        dragboard.setContent(clipboardContent);
        mouseEvent.consume();
    }

    private void handleDragDropped(DragEvent dragEvent, ImageView destImageView, int destRow, int destColumn) {
        Image image = dragEvent.getDragboard().getImage();
        resetAll();
        destImageView.setImage(image);
        selectedRow = destRow;
        selectedColumn = destColumn;
    }

    private void handleDragOver(DragEvent dragEvent) {
        if(dragEvent.getDragboard().hasImage())
            dragEvent.acceptTransferModes(TransferMode.MOVE);
    }

    public void handleDragDetectedFloatingMarble(MouseEvent mouseEvent) {
        handleDragDetected(mouseEvent, floatingMarble_imageView);
    }

    public void handleDragDroppedFloatingMarble(DragEvent dragEvent) {
        handleDragDropped(dragEvent, floatingMarble_imageView, 0, 0);
    }

    public void handleDragOverFloatingMarble(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragDetectedR0C4(MouseEvent mouseEvent) {
        handleDragDetected(mouseEvent, marbleR0C4_imageView);
    }

    public void handleDragDroppedR0C4(DragEvent dragEvent) {
        handleDragDropped(dragEvent, marbleR0C4_imageView, 0, 4);
    }

    public void handleDragOverR0C4(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragDetectedR1C4(MouseEvent mouseEvent) {
        handleDragDetected(mouseEvent, marbleR1C4_imageView);
    }

    public void handleDragDroppedR1C4(DragEvent dragEvent) {
        handleDragDropped(dragEvent, marbleR1C4_imageView, 1, 4);
    }

    public void handleDragOverR1C4(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragDetectedR2C4(MouseEvent mouseEvent) {
        handleDragDetected(mouseEvent, marbleR2C4_imageView);
    }

    public void handleDragDroppedR2C4(DragEvent dragEvent) {
        handleDragDropped(dragEvent, marbleR2C4_imageView, 2, 4);
    }

    public void handleDragOverR2C4(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragDetectedR3C0(MouseEvent mouseEvent) {
        handleDragDetected(mouseEvent, marbleR3C0_imageView);
    }

    public void handleDragDroppedR3C0(DragEvent dragEvent) {
        handleDragDropped(dragEvent, marbleR3C0_imageView, 3, 0);
    }

    public void handleDragOverR3C0(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragDetectedR3C1(MouseEvent mouseEvent) {
        handleDragDetected(mouseEvent, marbleR3C1_imageView);
    }

    public void handleDragDroppedR3C1(DragEvent dragEvent) {
        handleDragDropped(dragEvent, marbleR3C1_imageView, 3, 1);
    }

    public void handleDragOverR3C1(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragDetectedR3C2(MouseEvent mouseEvent) {
        handleDragDetected(mouseEvent, marbleR3C2_imageView);
    }

    public void handleDragDroppedR3C2(DragEvent dragEvent) {
        handleDragDropped(dragEvent, marbleR3C2_imageView, 3, 2);
    }

    public void handleDragOverR3C2(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    public void handleDragDetectedR3C3(MouseEvent mouseEvent) {
        handleDragDetected(mouseEvent, marbleR3C3_imageView);
    }

    public void handleDragDroppedR3C3(DragEvent dragEvent) {
        handleDragDropped(dragEvent, marbleR3C3_imageView, 3, 3);
    }

    public void handleDragOverR3C3(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }


    public void enableMarketAction(){
        takeResources_button.setDisable(false);
    }

    public void disableMarketAction(){
        takeResources_button.setDisable(true);
    }

    /*public void updateMarket(List<MarbleColors> marbles, MarbleColors floatingMarble){
        updateMarbleMatrix(marbles);
        resetAll();
        updateFloatingMarble(floatingMarble);
    }*/

    private void updateMarbleMatrix(List<Marble> marble){
        int i=0;
        for (Node node : marbleMatrix_gridPane.getChildren()) {
            String resPath = "/imgs/marbles/marble_"+ marble.get(i).getColor().toString().toLowerCase()+".png";
            ((ImageView)node).setImage(new Image(getClass().getResourceAsStream(resPath)));
            i++;
        }
    }

    private void updateFloatingMarble(Marble marble){
        floatingMarble_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/marbles/marble_"
                +marble.getColor().toString().toLowerCase()+".png")));
    }

    private void fillList(){
        marblePositionList = new ArrayList<>();
        marblePositionList.add(marbleR0C4_imageView);
        marblePositionList.add(marbleR1C4_imageView);
        marblePositionList.add(marbleR2C4_imageView);
        marblePositionList.add(marbleR3C0_imageView);
        marblePositionList.add(marbleR3C1_imageView);
        marblePositionList.add(marbleR3C2_imageView);
        marblePositionList.add(marbleR3C3_imageView);
    }

    private int getListPosition(){
        if(selectedColumn==4)
            return selectedRow;
        else
            return 3+selectedColumn;
    }

    private void resetAll(){
        fillList();
        for(ImageView imageView : marblePositionList)
            imageView.setImage(null);
        floatingMarble_imageView.setImage(null);
    }

    public void setMarket(MarketView market) {
        List<Marble> marbles = new ArrayList<>();
        for(int row = 0; row < MARKET_ROW_SIZE.value(); row++)
            for(int col = 0; col < MARKET_COLUMN_SIZE.value(); col++)
                marbles.add(market.getMarbleMatrix()[row][col]);
        updateMarbleMatrix(marbles);
        resetAll();
        updateFloatingMarble(market.getFloatingMarble());
    }
}
