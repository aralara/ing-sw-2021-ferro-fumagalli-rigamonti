package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.SceneNames;
import it.polimi.ingsw.client.structures.MarketView;
import it.polimi.ingsw.server.model.market.Marble;
import it.polimi.ingsw.utils.messages.client.SelectMarketMessage;
import javafx.fxml.FXML;
import javafx.scene.Node;
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

    /**
     * Takes resources from the market according to the selected row and column
     * and sends a message to the server
     */
    public void takeMarketResources() {
        if((selectedRow==3 && selectedColumn>=0 && selectedColumn<=3) ||
                (selectedColumn==4 && selectedRow>=0 && selectedRow<=2)) {
            int row = selectedRow==3?-1:selectedRow;
            int col = selectedColumn==4?-1:selectedColumn;
            selectedRow=0;
            selectedColumn=0;
            disableMarketAction(true);
            ((DecksBoardController)getGUIApplication().getController(SceneNames.DECKS_BOARD)).disableBuyCardAction(true);
            PlayerBoardController pbc = ((PlayerBoardController)getGUIApplication().getController(SceneNames.PLAYER_BOARD));
            pbc.disableActivateProductionsAction();
            pbc.disableActivateLeaderAction();
            pbc.disableDiscardLeaderAction();
            pbc.setWarehouseIsDisabled(false);
            pbc.setMainActionPlayed(true);
            pbc.setIsResToPlace(true);
            pbc.hideCheckBoxes();
            resetAll();
            getGUI().getMessageHandler().sendClientMessage(new SelectMarketMessage(row, col));
            getGUIApplication().closeSecondStage();
        }
    }

    /**
     * Resets marbles' imageViews, close this stage and go back to the main stage
     */
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

    /**
     * Handles drag detected event of the marbles
     * @param mouseEvent Mouse event
     * @param srcImageView ImageView to drag
     */
    private void handleDragDetected(MouseEvent mouseEvent, ImageView srcImageView){
        Dragboard dragboard = srcImageView.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putImage(srcImageView.getImage());
        dragboard.setContent(clipboardContent);
        mouseEvent.consume();
    }

    /**
     * Handles drag dropped events of the marbles
     * @param dragEvent Drag event
     * @param destImageView ImageView to drop
     * @param destRow Row where drop the marble
     * @param destColumn Column where drop the marble
     */
    private void handleDragDropped(DragEvent dragEvent, ImageView destImageView, int destRow, int destColumn) {
        Image image = dragEvent.getDragboard().getImage();
        resetAll();
        destImageView.setImage(image);
        selectedRow = destRow;
        selectedColumn = destColumn;
    }

    /**
     * Handles drag over events of the depots
     * @param dragEvent Drag event
     */
    private void handleDragOver(DragEvent dragEvent) {
        if(dragEvent.getDragboard().hasImage())
            dragEvent.acceptTransferModes(TransferMode.MOVE);
    }

    /**
     * Handles drag detected event of the floating marble
     * @param mouseEvent Mouse event
     */
    public void handleDragDetectedFloatingMarble(MouseEvent mouseEvent) {
        handleDragDetected(mouseEvent, floatingMarble_imageView);
    }

    /**
     * Handles drag dropped event of the floating marble
     * @param dragEvent Drag event
     */
    public void handleDragDroppedFloatingMarble(DragEvent dragEvent) {
        handleDragDropped(dragEvent, floatingMarble_imageView, 0, 0);
    }

    /**
     * Handles drag over event of the floating marble
     * @param dragEvent Drag event
     */
    public void handleDragOverFloatingMarble(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    /**
     * Handles drag detected event of the marble at row 0
     * @param mouseEvent Mouse event
     */
    public void handleDragDetectedR0C4(MouseEvent mouseEvent) {
        handleDragDetected(mouseEvent, marbleR0C4_imageView);
    }

    /**
     * Handles drag dropped event of the marble at row 0
     * @param dragEvent Drag event
     */
    public void handleDragDroppedR0C4(DragEvent dragEvent) {
        handleDragDropped(dragEvent, marbleR0C4_imageView, 0, 4);
    }

    /**
     * Handles drag over event of the marble at row 0
     * @param dragEvent Drag event
     */
    public void handleDragOverR0C4(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    /**
     * Handles drag detected event of the marble at row 1
     * @param mouseEvent Mouse event
     */
    public void handleDragDetectedR1C4(MouseEvent mouseEvent) {
        handleDragDetected(mouseEvent, marbleR1C4_imageView);
    }

    /**
     * Handles drag dropped event of the marble at row 1
     * @param dragEvent Drag event
     */
    public void handleDragDroppedR1C4(DragEvent dragEvent) {
        handleDragDropped(dragEvent, marbleR1C4_imageView, 1, 4);
    }

    /**
     * Handles drag over event of the marble at row 1
     * @param dragEvent Drag event
     */
    public void handleDragOverR1C4(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    /**
     * Handles drag detected event of the marble at row 2
     * @param mouseEvent Mouse event
     */
    public void handleDragDetectedR2C4(MouseEvent mouseEvent) {
        handleDragDetected(mouseEvent, marbleR2C4_imageView);
    }

    /**
     * Handles drag dropped event of the marble at row 2
     * @param dragEvent Drag event
     */
    public void handleDragDroppedR2C4(DragEvent dragEvent) {
        handleDragDropped(dragEvent, marbleR2C4_imageView, 2, 4);
    }

    /**
     * Handles drag over event of the marble at row 2
     * @param dragEvent Drag event
     */
    public void handleDragOverR2C4(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    /**
     * Handles drag detected event of the marble at column 0
     * @param mouseEvent Mouse event
     */
    public void handleDragDetectedR3C0(MouseEvent mouseEvent) {
        handleDragDetected(mouseEvent, marbleR3C0_imageView);
    }

    /**
     * Handles drag dropped event of the marble at column 0
     * @param dragEvent Drag event
     */
    public void handleDragDroppedR3C0(DragEvent dragEvent) {
        handleDragDropped(dragEvent, marbleR3C0_imageView, 3, 0);
    }

    /**
     * Handles drag over event of the marble at column 0
     * @param dragEvent Drag event
     */
    public void handleDragOverR3C0(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    /**
     * Handles drag detected event of the marble at column 1
     * @param mouseEvent Mouse event
     */
    public void handleDragDetectedR3C1(MouseEvent mouseEvent) {
        handleDragDetected(mouseEvent, marbleR3C1_imageView);
    }

    /**
     * Handles drag dropped event of the marble at column 1
     * @param dragEvent Drag event
     */
    public void handleDragDroppedR3C1(DragEvent dragEvent) {
        handleDragDropped(dragEvent, marbleR3C1_imageView, 3, 1);
    }

    /**
     * Handles drag over event of the marble at column 1
     * @param dragEvent Drag event
     */
    public void handleDragOverR3C1(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    /**
     * Handles drag detected event of the marble at column 2
     * @param mouseEvent Mouse event
     */
    public void handleDragDetectedR3C2(MouseEvent mouseEvent) {
        handleDragDetected(mouseEvent, marbleR3C2_imageView);
    }

    /**
     * Handles drag dropped event of the marble at column 2
     * @param dragEvent Drag event
     */
    public void handleDragDroppedR3C2(DragEvent dragEvent) {
        handleDragDropped(dragEvent, marbleR3C2_imageView, 3, 2);
    }

    /**
     * Handles drag over event of the marble at column 2
     * @param dragEvent Drag event
     */
    public void handleDragOverR3C2(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    /**
     * Handles drag detected event of the marble at column 3
     * @param mouseEvent Mouse event
     */
    public void handleDragDetectedR3C3(MouseEvent mouseEvent) {
        handleDragDetected(mouseEvent, marbleR3C3_imageView);
    }

    /**
     * Handles drag dropped event of the marble at column 3
     * @param dragEvent Drag event
     */
    public void handleDragDroppedR3C3(DragEvent dragEvent) {
        handleDragDropped(dragEvent, marbleR3C3_imageView, 3, 3);
    }

    /**
     * Handles drag over event of the marble at column 3
     * @param dragEvent Drag event
     */
    public void handleDragOverR3C3(DragEvent dragEvent) {
        handleDragOver(dragEvent);
    }

    /**
     * Enables or disables takeResources button according to the value given by parameter
     * @param value True to disable, false otherwise
     */
    public void disableMarketAction(boolean value){
        takeResources_button.setDisable(value);
    }

    /**
     * Updates marbles' imageView in the matrix
     * @param marbles List of marbles to show
     */
    private void updateMarbleMatrix(List<Marble> marbles){
        int i=0;
        for (Node node : marbleMatrix_gridPane.getChildren()) {
            String resPath = "/imgs/marbles/marble_"+ marbles.get(i).getColor().toString().toLowerCase()+".png";
            ((ImageView)node).setImage(new Image(getClass().getResourceAsStream(resPath)));
            i++;
        }
    }

    /**
     * Updates floating marble's imageView
     * @param marble Floating marble to show
     */
    private void updateFloatingMarble(Marble marble){
        floatingMarble_imageView.setImage(new Image(getClass().getResourceAsStream("/imgs/marbles/marble_"
                +marble.getColor().toString().toLowerCase()+".png")));
    }

    /**
     * Loads marbles' imageViews in the marblePosition list
     */
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

    /**
     * Gets the position of the marble specified by selectedRow and selectedColumn
     * @return Returns the position of the marble
     */
    private int getListPosition(){
        if(selectedColumn==4)
            return selectedRow;
        else
            return 3+selectedColumn;
    }

    /**
     * Removes all the images where the floating marble can be placed
     */
    private void resetAll(){
        fillList();
        for(ImageView imageView : marblePositionList)
            imageView.setImage(null);
        floatingMarble_imageView.setImage(null);
    }

    /**
     * Shows market updating all the marbles
     * @param market Market to be shown
     */
    public void showMarket(MarketView market) {
        List<Marble> marbles = new ArrayList<>();
        for(int row = 0; row < MARKET_ROW_SIZE.value(); row++)
            for(int col = 0; col < MARKET_COLUMN_SIZE.value(); col++)
                marbles.add(market.getMarbleMatrix()[row][col]);
        updateMarbleMatrix(marbles);
        resetAll();
        updateFloatingMarble(market.getFloatingMarble());
    }
}
