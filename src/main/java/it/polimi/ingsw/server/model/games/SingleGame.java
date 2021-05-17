package it.polimi.ingsw.server.model.games;

import it.polimi.ingsw.server.model.FileNames;
import it.polimi.ingsw.server.model.boards.*;
import it.polimi.ingsw.server.model.cards.deck.DevelopmentDeck;
import it.polimi.ingsw.server.model.faith.FaithTrack;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.TurnStatus;
import it.polimi.ingsw.utils.listeners.Listeners;
import it.polimi.ingsw.utils.listeners.LorenzoCardPlayListener;
import it.polimi.ingsw.utils.listeners.LorenzoFaithChangeListener;

import java.util.List;

public class SingleGame extends Game{

    private LorenzoBoard lorenzoBoard;
    private boolean isLorenzoTurn, isLorenzoWinner;


    public SingleGame(){
    }


    @Override
    public void initGame(List<VirtualView> views){
        initLorenzoBoard();
        super.initGame(views);
        isLorenzoTurn = false;
        isLorenzoWinner = false;
    }

    @Override
    public String getPlayingNickname() {
        if(isLorenzoTurn)
            return "Lorenzo";   //TODO: uhm... boh
        else
            return getPlayerBoards().get(0).getPlayer().getNickname();
    }

    /**
     * Initializes the LorenzoBoard loading tokens from a file
     */
    public void initLorenzoBoard() {
        lorenzoBoard = new LorenzoBoard(this);
        lorenzoBoard.initLorenzoDeck(FileNames.LORENZO_DEV_FILE.value(), FileNames.LORENZO_FAITH_FILE.value());
    }

    @Override
    public int loadNextTurn() {
        int temp = TurnStatus.LOAD_TURN_NORMAL.value();
        isLorenzoTurn = !isLorenzoTurn;
        getPlayerBoards().get(0).setTurnPlayed(false);
        checkFaith();
        if(checkEndGame()) {
            calculateTotalVP();
            calculateFinalPositions();
            finished = true;
            temp = TurnStatus.LOAD_TURN_END_GAME.value();
        }
        else if(isLorenzoTurn) {
            lorenzoBoard.pickLorenzoCard().activateLorenzo(lorenzoBoard);
            temp = loadNextTurn();
        }
        return temp;
    }

    @Override
    void checkFaith(){
        FaithTrack faithTrack = getFaithTrack();
        PlayerBoard playerBoard = getPlayerBoards().get(0);
        boolean vatReport = getFaithTrack().checkReportActivation(playerBoard.getFaithBoard().getFaith());
        if(!vatReport)
            vatReport = faithTrack.checkReportActivation(lorenzoBoard.getFaith());
        if(vatReport)
            playerBoard.getFaithBoard().handleReportActivation(faithTrack);
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

    @Override
    public void addListeners(List<VirtualView> virtualViews) {
        super.addListeners(virtualViews);
        lorenzoBoard.addListener(Listeners.GAME_LORENZO_FAITH.value(),
                new LorenzoFaithChangeListener(virtualViews.get(0)));
        lorenzoBoard.addListener(Listeners.GAME_LORENZO_CARD.value(),
                new LorenzoCardPlayListener(virtualViews.get(0)));
    }
}
