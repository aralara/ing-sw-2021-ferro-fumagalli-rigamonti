package it.polimi.ingsw.boards;

import it.polimi.ingsw.cards.ability.AbilityMarble;
import it.polimi.ingsw.cards.card.*;
import it.polimi.ingsw.games.Game;
import it.polimi.ingsw.storage.*;

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
    private Production production;


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
     * Gets all the marble abilities of the active leaders
     * @return Returns a list of the marble abilities
     */
    private List<AbilityMarble> getMarbleAbilities(){
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
        return 0;
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
     * Checks if a DevelopmentCard can be placed in a specific space on the board
     * @param card The development card to be added
     * @param space Position of the space on the board
     * @return Returns true if the card can be added, false otherwise
     */
    public boolean checkDevCardAddable(DevelopmentCard card, int space){
        return false;
    }

    /**
     * Puts a development card at the top of one of the spaces specified by the parameter
     * @param card The development card to be added
     * @param space Position of the space on the board
     */
    public void addDevCard(DevelopmentCard card, int space) {

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
     * Adds a list of resources to the Strongbox
     * @param resources List of resources to be added
     */
    public void addToStrongbox(List<Resource> resources) {

    }

    /**
     * Plays a LeaderCard from the hand to the board
     * @param leaderCard LeaderCard to be played
     */
    public void playLeaderCard(LeaderCard leaderCard){

    }

    /**
     * Discards a LeaderCard from the hand
     * @param leaderCard LeaderCard to be discarded
     */
    public void discardLeader(LeaderCard leaderCard){

    }
}
