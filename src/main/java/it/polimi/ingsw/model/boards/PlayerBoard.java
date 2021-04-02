package it.polimi.ingsw.model.boards;

import it.polimi.ingsw.model.cards.card.*;
import it.polimi.ingsw.model.games.Game;
import it.polimi.ingsw.model.storage.*;

import java.util.List;

public class PlayerBoard {

    private Player player;
    private Game game;
    private DevelopmentBoard developmentBoard;
    private LeaderBoard leaderBoard;
    private FaithBoard faithBoard;
    private Warehouse warehouse;
    private Strongbox strongbox;
    private boolean inkwell;
    private boolean turnPlayed;
    private Production basicProduction;

    private List<Production> activeAbilityProductions;
    private List<ResourceType> activeAbilityMarbles;
    private List<ResourceType> activeAbilityDiscounts;


    public PlayerBoard(){

    }


    /**
     * Gets the player attribute
     * @return Returns player value
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the warehouse attribute
     * @return Returns warehouse value
     */
    public Warehouse getWarehouse(){
        return this.warehouse;
    }

    /**
     * Gets the leaderBoard attribute
     * @return Returns leaderBoard value
     */
    public LeaderBoard getLeaderBoard(){
        return null;
    }

    /**
     * Sets the player attribute
     * @param player New attribute value
     */
    public void setPlayer(Player player){
        this.player = player;
    }

    /**
     * Gets the turnPlayed attribute
     * @return Returns turnPlayed value
     */
    public boolean isTurnPlayed() {
        return turnPlayed;
    }

    /**
     * Sets the turnPlayed attribute
     * @param value New turnPlayed value
     */
    public void setTurnPlayed(boolean value){
        this.turnPlayed = value;
    }

    /**
     * Gets all the possible ResourceType from the activeAbilityMarbles
     * @return Returns a list of the ResourceType
     */
    public List<ResourceType> getAbilityMarbles(){
        return null;
    }

    /**
     * Sets the current PlayerBoard as the first playing one
     */
    public void firstPlayer(){

    }

    /**
     * Adds a set amount of faith to the current FaithBoard
     * @param faith Faith quantity to be added
     */
    public void addFaith(int faith){

    }

    /**
     * Creates a list containing all of the player resources
     * @return Returns a list of resources
     */
    public List<Resource> createResourceStock(){
        return null;
    }

    /**
     * Creates a list containing all of the player productions
     * @return Returns a list of productions
     */
    public List<Production> createProductionStock(){
        return null;
    }

    /**
     * Calculates total VPs for the player checking the FaithTrack, leader cards, development cards and resources
     * @return Returns total VP amount
     */
    public int calculateVP(){
        return -1;
    }

    /**
     * Changes the current Warehouse configuration
     * @param shelves New shelves for the Warehouse
     * @return Returns true if the configuration is valid, false otherwise
     */
    private boolean changeWarehouse(List<Shelf> shelves){
        return false;
    }

    /**
     * Checks if a DevelopmentCard can be placed on the board
     * @param card The development card to be added
     * @return Returns true if the card can be added, false otherwise
     */
    public boolean canBuyDevCard(DevelopmentCard card){
        return false;
    }

    /**
     * Puts a development card at the top of one of the spaces specified by the parameter
     * @param card The development card to be added
     * @param space Position of the space on the board
     * @param request List of requests containing resource quantity and location
     * @return Returns true if the card is bought, false otherwise
     */
    public boolean buyDevCard(DevelopmentCard card, int space, List<RequestResources> request) {
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
     * Takes the resources from the Storages specified by the RequestResources if all the requests are valid
     * @param requests List of requests containing resource quantity and location for the spent resources
     * @return Returns true if the resources are taken, false otherwise
     */
    private boolean takeFromStorages(List<RequestResources> requests){
        return false;
    }

    /**
     * Discards a LeaderCard from the hand
     * @param leaderCard LeaderCard to be discarded
     */
    public void discardLeader(LeaderCard leaderCard){

    }

    /**
     * Plays a LeaderCard from the hand to the board
     * @param leaderCard LeaderCard to be played
     * @return Returns true if the LeaderCard can be played, false otherwise
     */
    public boolean playLeaderCard(LeaderCard leaderCard){
        return false;
    }

    /**
     * Adds a production to the list of productions from active leader abilities
     * @param production Production to be added
     */
    public void addAbilityProductions(Production production) {

    }

    /**
     * Adds a ResourceType to the list of ResourceType from active leader marble abilities
     * @param type ResourceType to be added
     */
    public void addAbilityMarbles(ResourceType type){

    }

    /**
     * Adds a ResourceType to the list of ResourceType from active leader discount abilities
     * @param type ResourceType to be added
     */
    public void addAbilityDiscounts(ResourceType type){

    }

    /**
     * Adds a Shelf given by a leader ability to the Warehouse
     * @param shelf Shelf to be added
     */
    public void addAbilityWarehouse(Shelf shelf){

    }
}
