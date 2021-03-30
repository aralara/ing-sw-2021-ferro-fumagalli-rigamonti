package it.polimi.ingsw;

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
     * Handles the market action by receiving a list of resources directly from the Market and calling
     * resolveMarketMarbles
     * @param resources List of resources from the Market
     */
    public List<Resource> getFromMarket(List<Resource> resources){
        return null;
    }

    private boolean changeWarehouse(List<Shelf> shelves){
        return false;
    }

    public boolean buyDevCard(DevelopmentCard card){
        return false;
    }

    private boolean canBuyDevCard(DevelopmentCard card){
        return false;
    }

    private boolean checkStorages(List<RequestResources> request){
        return false;
    }

    private boolean takeFromStorages(List<RequestResources> request){
        return false;
    }

    public void activateProduction(){

    }

    private List<Production> resolveProductionWildcards(List<Production> productions){
        return null;
    }

    public void playLeaderCard(Card leaderCard){

    }

    private boolean checkLeaderRequirements(Card leaderCard){
        return false;
    }

    public void discardLeader(Card leaderCard){

    }
}
