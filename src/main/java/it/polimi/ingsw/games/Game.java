package it.polimi.ingsw.games;

import it.polimi.ingsw.boards.PlayerBoard;
import it.polimi.ingsw.cards.card.*;
import it.polimi.ingsw.cards.deck.DevelopmentDeck;
import it.polimi.ingsw.faith.FaithTrack;
import it.polimi.ingsw.market.Market;

import java.util.List;

public abstract class Game {

    private List<PlayerBoard> playerBoards;
    private Market market;
    private List<DevelopmentDeck> developmentDecks;
    private FaithTrack faithTrack;


    public Game(){

    }

    /**
     * Initializes a game by calling initMarket, initDevelopment, initFaithTrack, initLeaders in this order
     */
    void initGame(){

    }

    /**
     * Initializes the market
     */
    private void initMarket(){

    }

    /**
     * Loads development cards from their file utilizing DevelopmentCardFactory and split them into 12 DevelopmentDeck
     */
    private void initDevelopment(){

    }

    /**
     * Loads faith track information from his file
     */
    private void initFaithTrack(){

    }

    /**
     * Loads leader cards from their file utilizing LeaderCardFactory and sends four random cards to each player
     */
    private void initLeaders(){

    }

    /**
     * Method invoked to let the next player play his turn
     */
    public abstract void loadNextTurn();

    /*
    private List<Resource> resolveMarketMarbles(List<Resource> resources){
        return null;
    }
    */

    /**
     * Method invoked to check if a player have reached a Pope space and handle the eventual vatican report updating
     * player boards and the faith track
     */
    void checkFaith(){

    }

    /**
     * Method invoked to add faith to the faith counter to every board except for the active one
     * @param quantity Amount of faith to be added
     */
    public abstract void addFaithAll(int quantity);

    /**
     * Picks the development card at the top of the deck specified by color and level
     * @param color Color of the deck to choose from
     * @param level Level of the deck to choose from (if it's "0", it picks from the lowest)
     * @return Returns the selected development card
     */
    public DevelopmentCard pickDevelopmentCard(CardColors color, int level){
        return null;
    }

    /**
     * Method invoked to check if a player has finished the game by reaching the last Pope space or by buying the 7th
     * development card
     * @return Returns true if the game is finished, false otherwise
     */
    boolean checkEndGame(){
        return false;
    }

    /**
     * Method invoked to calculate the amount of VP earned by each player
     * @return Returns an array containing the results, parallel to the list of player boards
     */
    public int[] calculateTotalVP() {
        return null;
    }

    /**
     * Method invoked to calculate the final position for each player
     * @return Returns an array containing the positions, parallel to the list of player boards
     */
    public int[] calculateFinalPositions() {
        return null;
    }
}
