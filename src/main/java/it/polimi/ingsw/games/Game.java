package it.polimi.ingsw.games;

import it.polimi.ingsw.boards.PlayerBoard;
import it.polimi.ingsw.cards.ability.AbilityMarble;
import it.polimi.ingsw.cards.card.*;
import it.polimi.ingsw.cards.deck.DevelopmentDeck;
import it.polimi.ingsw.faith.FaithTrack;
import it.polimi.ingsw.market.Market;
import it.polimi.ingsw.storage.RequestResources;
import it.polimi.ingsw.storage.Resource;
import it.polimi.ingsw.storage.ResourceType;
import it.polimi.ingsw.storage.Shelf;

import java.util.List;

public abstract class Game {

    private List<PlayerBoard> playerBoards;
    private Market market;
    private List<DevelopmentDeck> developmentDecks;
    private FaithTrack faithTrack;


    public Game(){

    }


    /**
     * Gets the playerBoards attribute
     * @return Returns playerBoards value
     */
    List<PlayerBoard> getplayerBoards(){
        return playerBoards;
    }

    /**
     * Sets the playerBoards attribute
     * @param playerBoards New attribute value
     */
    void setPlayerBoards (List<PlayerBoard> playerBoards){
        this.playerBoards = playerBoards;
    }

    /**
     * Initializes a game by calling initMarket, initDevelopment, initFaithTrack, initLeaders in this order
     */
    public void initGame(){

    }

    /**
     * Initializes the market
     */
    void initMarket(){

    }

    /**
     * Loads development cards from their file utilizing DevelopmentCardFactory and split them into 12 DevelopmentDeck
     */
    void initDevelopment(){

    }

    /**
     * Loads faith track information from his file
     */
    void initFaithTrack(){

    }

    /**
     * Loads leader cards from their file utilizing LeaderCardFactory and sends four random cards to each player
     */
    void initLeaders(){

    }

    /**
     * Discards leader cards from the hand of a player
     * @param player Index of the player discarding cards
     * @param cards List of leader cards to discard
     */
    public void discardLeaders(int player, List<LeaderCard> cards){

    }

    /**
     * Adds resources organized in shelves to the specified player's warehouse
     * @param player Index of the player to add resources to
     * @param shelves List of shelves containing resources to add
     * @param discarded List of resources to be discarded
     */
    public void addResourcesToWarehouse(int player, List<Shelf> shelves, List<Resource> discarded){

    }

    /**
     * Method invoked to let the next player play his turn
     */
    public abstract void loadNextTurn();


    /**
     * Method invoked to take resources from the market
     * @param row Row chosen by the player, if the player chose a column it is -1
     * @param column Column chosen by the player, if the player chose a row it is -1
     * @return Returns a list of resources corresponding to the marbles contained in the market
     */
    public List<Resource> getFromMarket(int row, int column){
        return null;
    }

    /**
     * Method invoked to get all of the resource types available to the current player given by their leader cards'
     * abilities in order to resolve market wildcards
     * @return Returns a list of ResourceType
     */
    private List<ResourceType> resolveMarketWildcards(){
        return null;
    }

    /**
     * Checks if a DevelopmentCard can be bought and placed on the board for the current player
     * @param card The development card to be added
     * @param space Position of the space on the board
     * @return Returns true if the card can be added, false otherwise
     */
    public boolean canBuyDevCard(DevelopmentCard card, int space){
        return false;
    }

    /**
     * If the resources contained in the requests match the actual available resources detained by the current player,
     * buys the card for said player
     * @param card The development card to buy
     * @param requests List of requests containing resource quantity and location
     * @return Returns true if the card is bought, false otherwise
     */
    public boolean buyDevCard(DevelopmentCard card, List<RequestResources> requests){
        return false;
    }

    /**
     * Checks if a list of productions can be activated for the current player by checking if they own enough resources
     * @param consumed The list of resources to be consumed
     * @return Returns true if the productions can be activated, false otherwise
     */
    public boolean canActivateProductions(List<Resource> consumed){
        return false;
    }

    /**
     * If the resources contained in the requests match the actual available resources detained by the current player,
     * activates the productions for said player by adding the produced resources to their Strongbox
     * @param produced List of resources to produce
     * @param requests List of requests containing resource quantity and location for the spent resources
     * @return Returns true if the card is bought, false otherwise
     */
    public boolean activateProductions(List<Resource> produced, List<RequestResources> requests){
        return false;
    }

    /**
     * Discards a specified LeaderCard from the hand of the current player calling discardLeaders and adds 1 faith
     * @param card LeaderCard to be discarded
     */
    public void discardExtraLeader(LeaderCard card){

    }

    /**
     * Plays the selected LeaderCard if the current player meets its resource requirements
     * @param card LeaderCard to be played
     * @return Returns true if the card is played, false otherwise
     */
    public boolean playLeaderCard(LeaderCard card){
        return false;
    }

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
