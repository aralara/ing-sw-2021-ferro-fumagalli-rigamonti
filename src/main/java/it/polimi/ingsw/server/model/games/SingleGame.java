package it.polimi.ingsw.server.model.games;

import it.polimi.ingsw.server.model.FileNames;
import it.polimi.ingsw.server.model.boards.*;
import it.polimi.ingsw.server.model.cards.deck.DevelopmentDeck;
import it.polimi.ingsw.server.model.faith.FaithTrack;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.TurnStatus;
import it.polimi.ingsw.utils.listeners.Listeners;
import it.polimi.ingsw.utils.listeners.server.LorenzoCardPlayListener;
import it.polimi.ingsw.utils.listeners.server.LorenzoFaithChangeListener;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Handles methods relative to the single game
 */
public class SingleGame extends Game {

    private LorenzoBoard lorenzoBoard;
    private boolean isLorenzoTurn, isLorenzoWinner;


    /**
     * Default constructor for SingleGame
     */
    public SingleGame() {
    }


    @Override
    public void initGame(List<String> players) {
        initLorenzoBoard();
        super.initGame(players);
        isLorenzoTurn = false;
        isLorenzoWinner = false;
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
        int status = TurnStatus.LOAD_TURN_NORMAL.value();
        getPlayerBoards().get(0).setTurnPlayed(false);
        isLorenzoTurn = !isLorenzoTurn;
        checkFaith();
        //Checks if the game has ended
        if(checkEndGame()) {
            //Updates the player boards with the results
            calculateTotalVP();
            calculateFinalPositions();
            setFinished(true);
            status = TurnStatus.LOAD_TURN_END_GAME.value();
        }
        else if(isLorenzoTurn) {
            //Plays Lorenzo's turn and loads next turn
            lorenzoBoard.pickLorenzoCard().activateLorenzo(lorenzoBoard);
            status = loadNextTurn();
        }
        return status;
    }

    @Override
    void checkFaith() {
        FaithTrack faithTrack = getFaithTrack();
        PlayerBoard playerBoard = getPlayerBoards().get(0);
        boolean vatReport = getFaithTrack().checkReportActivation(playerBoard.getFaithBoard().getFaith());
        if(!vatReport)
            vatReport = faithTrack.checkReportActivation(lorenzoBoard.getFaith());
        if(vatReport)
            playerBoard.getFaithBoard().handleReportActivation(faithTrack);
    }

    @Override
    public void addFaithAll(int playerEx, int quantity) {
        lorenzoBoard.addFaith(quantity);
    }

    @Override
    boolean checkEndGame(){
        //Checks standard winning conditions
        boolean endGame = super.checkEndGame();
        //Checks if a development card pile has been emptied
        for(DevelopmentDeck dDeck : getDevelopmentDecks()) {
            if (!endGame && dDeck.getDeckLevel() == 3 && dDeck.isEmpty()) {
                endGame = true;
                isLorenzoWinner = true;
            }
        }
        //Checks if Lorenzo has reached the end of the FaithTrack
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
        getPlayerBoards().get(0).getPlayer().setFinalPosition(isLorenzoWinner ? 2 : 1);
        lorenzoBoard.getPlayer().setFinalPosition(isLorenzoWinner ? 1 : 2);
    }

    @Override
    public void addListeners(List<VirtualView> virtualViews) {
        //Standard listeners
        super.addListeners(virtualViews);
        //LorenzoBoard
        lorenzoBoard.addListener(Listeners.GAME_LORENZO_FAITH.value(),
                new LorenzoFaithChangeListener(virtualViews.get(0)));
        lorenzoBoard.addListener(Listeners.GAME_LORENZO_CARD.value(),
                new LorenzoCardPlayListener(virtualViews.get(0)));
    }

    @Override
    public String getPlayingNickname() {
        if(isLorenzoTurn)
            return lorenzoBoard.getPlayer().getNickname();
        else
            return getPlayerBoards().get(0).getPlayer().getNickname();
    }

    @Override
    public List<Player> getEndPlayerList() {
        return Stream.concat(super.getEndPlayerList().stream(),
                Stream.of(lorenzoBoard.getPlayer())).collect(Collectors.toList());
    }

    /**
     * Gets the lorenzoBoard attribute
     * @return Returns lorenzoBoard value
     */
    public LorenzoBoard getLorenzoBoard() {
        return lorenzoBoard;
    }
}
