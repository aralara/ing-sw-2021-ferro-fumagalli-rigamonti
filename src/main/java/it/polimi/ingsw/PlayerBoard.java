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
    private Production production;


    public PlayerBoard(){

    }


    public Player getPlayer(){
        return null;
    }

    public void setPlayer(Player player){

    }

    public void firstPlayer(){

    }

    public void addFaith(int faith){

    }

    public List<Resource> createResourceStock(){
        return null;
    }

    public List<Production> createProductionStock(){
        return null;
    }

    public int calculateVP(){
        return 0;
    }

    public void playTurn(){

    }

    public void getFromMarket(List<Resource> resources){

    }

    private List<Resource> resolveMarketMarbles(List<Resource> resources){
        return null;
    }

    private void chooseWharehouse(List<Resource> resources){

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
