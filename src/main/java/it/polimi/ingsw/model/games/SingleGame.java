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
        boolean vatReport = getFaithTrack().checkReportActivation(playerBoard.getFaithProgression());
        if(!vatReport)
            vatReport = faithTrack.checkReportActivation(lorenzoBoard.getFaith());
        if(vatReport) {
            boolean playerInVatReport = faithTrack.checkPlayerReportPosition(playerBoard.getFaithProgression());
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
        //TODO: Aggiungere un metodo che controlli nella FaithBoard se Lorenzo ha raggiunto l'ultimo PopeSpace
        for(DevelopmentDeck dDeck : getDevelopmentDecks()) {
            if (dDeck.getDeckLevel() == 3 && dDeck.isEmpty()) {
                endGame = true;
                isLorenzoWinner = true;
            }
        }
        return endGame;
    }

    @Override
    public int[] calculateTotalVP() {
        int[] playersVP = new int[1];
        playersVP[0] = getPlayerBoards().get(0).calculateVP(getFaithTrack());
        return playersVP;
    }

    @Override
    public int[] calculateFinalPositions() {
        int[] playersVP = new int[1];
        if(!isLorenzoWinner)
            playersVP[0] = 1;
        else
            playersVP[0] = 2;
        return playersVP;
    }
}
