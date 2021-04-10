package it.polimi.ingsw.model.games;

import it.polimi.ingsw.model.FileNames;
import it.polimi.ingsw.model.boards.LorenzoBoard;
import it.polimi.ingsw.model.boards.PlayerBoard;
import it.polimi.ingsw.model.cards.deck.DevelopmentDeck;
import it.polimi.ingsw.model.faith.FaithTrack;

public class SingleGame extends Game{

    private LorenzoBoard lorenzoBoard;
    private boolean isLorenzoTurn, isLorenzoWinner;


    public SingleGame(String ... players){
        initGame(players);
    }


    @Override
    public void initGame(String ... players){
        super.initGame(players);
        initLorenzoBoard();
        isLorenzoTurn = false;
        isLorenzoWinner = false;
    }

    /**
     * Initializes the LorenzoBoard loading tokens from a file
     */
    public void initLorenzoBoard(){
        lorenzoBoard = new LorenzoBoard(this);
        lorenzoBoard.initLorenzoDeck(FileNames.LORENZO_DEV_FILE.value(), FileNames.LORENZO_FAITH_FILE.value());
    }

    @Override
    public void loadNextTurn(){
        isLorenzoTurn = !isLorenzoTurn;
        checkFaith();
        if(checkEndGame()){
            calculateTotalVP();
            calculateFinalPositions();
        }
    }

    @Override
    void checkFaith(){
        FaithTrack faithTrack = getFaithTrack();
        PlayerBoard playerBoard = getPlayerBoards().get(0);
        boolean vatReport = getFaithTrack().checkReportActivation(playerBoard.getFaithBoard().getFaith());
        if(!vatReport)
            vatReport = faithTrack.checkReportActivation(lorenzoBoard.getFaith());
        if(vatReport) {
            boolean playerInVatReport = faithTrack.checkPlayerReportPosition(playerBoard.getFaithBoard().getFaith());
            //TODO: Gestire update PopeProgression PlayerBoard
        }
    }

    @Override
    public void addFaithAll(int playerEx, int quantity){
        lorenzoBoard.addFaith(quantity);
    }

    @Override
    boolean checkEndGame(){
        boolean endGame = super.checkEndGame();
        for(DevelopmentDeck dDeck : getDevelopmentDecks()) {
            if (!endGame && dDeck.getDeckLevel() == 3 && dDeck.isEmpty()) {
                endGame = true;
                isLorenzoWinner = true;
            }
        }
        if(!endGame && getFaithTrack().isCompleted(lorenzoBoard.getFaith())){
            endGame = true;
            isLorenzoWinner = true;
        }
        return endGame;
    }

    @Override
    public void calculateTotalVP() {
        getPlayerBoards().get(0).calculateVP(getFaithTrack());
    }

    @Override
    public void calculateFinalPositions() {
        if(!isLorenzoWinner)
            getPlayerBoards().get(0).getPlayer().setFinalPosition(1);
        else
            getPlayerBoards().get(0).getPlayer().setFinalPosition(2);
    }
}
