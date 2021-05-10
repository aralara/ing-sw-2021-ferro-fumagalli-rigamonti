package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.structures.*;
import it.polimi.ingsw.server.model.cards.ability.AbilityDiscount;
import it.polimi.ingsw.server.model.cards.ability.AbilityMarble;
import it.polimi.ingsw.server.model.cards.ability.AbilityProduction;
import it.polimi.ingsw.server.model.cards.ability.AbilityWarehouse;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.model.cards.requirement.Requirement;
import it.polimi.ingsw.server.model.cards.requirement.RequirementDev;
import it.polimi.ingsw.server.model.cards.requirement.RequirementRes;
import it.polimi.ingsw.server.model.market.Marble;
import it.polimi.ingsw.server.model.market.MarbleColors;
import it.polimi.ingsw.server.model.storage.Production;
import it.polimi.ingsw.server.model.storage.ResourceType;

import java.sql.SQLSyntaxErrorException;

import static it.polimi.ingsw.utils.Constants.MARKET_COLUMN_SIZE;
import static it.polimi.ingsw.utils.Constants.MARKET_ROW_SIZE;


public class GraphicalCLI {

    public static final String RESET = "\033[0m";  // Text Reset

    public static final String BLACK_BRIGHT = "\033[0;90m";  // BLACK
    public static final String RED_BRIGHT = "\033[0;91m";    // RED
    public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
    public static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
    public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
    public static final String WHITE_BRIGHT = "\033[0;97m";  // WHITE


    public void printMarket(MarketView market){
        System.out.println("The market:");
        String color;
        for(int i= 0; i<MARKET_ROW_SIZE.value();i++){
            for(int j = 0; j<MARKET_COLUMN_SIZE.value();j++){
                color = chooseColor(market.getMarbleMatrix()[i][j].getResourceType());
                System.out.print("[ " + color + "■" + color + RESET + " ]");
            }
            System.out.println("[ x ]");
        }
        System.out.println("[ x ][ x ][ x ][ x ]");

        color = chooseColor(market.getFloatingMarble().getResourceType());
        System.out.println("The marble to place is " + color + "■" + color);
        System.out.print(RESET);
    }

    private String chooseColor(ResourceType resourceType){
        if(resourceType == ResourceType.SHIELD){
            return BLUE_BRIGHT;
        }else if(resourceType == ResourceType.STONE){
            return BLACK_BRIGHT;
        }else if(resourceType == ResourceType.SERVANT){
            return PURPLE_BRIGHT;
        }else if(resourceType == ResourceType.FAITH){
            return RED_BRIGHT;
        }else if(resourceType == ResourceType.WILDCARD){
            return WHITE_BRIGHT;
        }else if(resourceType == ResourceType.COIN){
            return YELLOW_BRIGHT;
        }
        else return RESET;
    }

    public void printLeaderCard(LeaderCard card){
        String toPrint;
        boolean first = true;
        if(card.getID() != -1) {
            System.out.println("♥ LEADER CARD ♥ ");
            System.out.print(" • Requirements: ");
            if (card.getRequirements().get(0) instanceof RequirementDev) {   //TODO: sostituire con strategy quando ci sarà una classe apposita
                for (Requirement req : card.getRequirements()) {
                    toPrint = ((!first) ? ", " : "") + ((RequirementDev) req).getNumber() + " " +
                            ((RequirementDev) req).getColor() + " development cards level " +
                            ((RequirementDev) req).getLevel();
                    System.out.print(toPrint);
                    first = false;
                }
            } else if (card.getRequirements().get(0) instanceof RequirementRes) {
                for (Requirement req : card.getRequirements()) {
                    System.out.print(((RequirementRes) req).getResource().getQuantity() + " " +
                            ((RequirementRes) req).getResource().getResourceType());
                }
            }
            System.out.println();
            System.out.println(" • The card value is " + card.getVP() + " victory point");

            System.out.print(" • The special ability is ");

            if (card.getAbility() instanceof AbilityDiscount) {
                System.out.println("a discount by 1 unit of " + ((AbilityDiscount) card.getAbility()).getResourceType());
            } else if (card.getAbility() instanceof AbilityMarble) {
                System.out.println("the power of getting a " + ((AbilityMarble) card.getAbility()).getResourceType() +
                        " from a white marble from the market");
            } else if (card.getAbility() instanceof AbilityProduction) {
                System.out.println("the production:  ");
                printProduction(((AbilityProduction) card.getAbility()).getProduction());
            } else if (card.getAbility() instanceof AbilityWarehouse) {
                System.out.println("to have an extra shelf to contain 1 " + ((AbilityWarehouse) card.getAbility()).getResourceType());
            }
        }
        else {
            //TODO: printare una carta coperta (?)
        }
    }

    public void printProduction(Production production){
        System.out.print("\tConsumed: ");
        boolean first = true;
        String toPrint;
        for(int i = 0; i< production.getConsumed().size(); i++){
            toPrint = ((!first) ? "\t          " :"") + " > " + production.getConsumed().get(i).getQuantity() + " " +
                    production.getConsumed().get(i).getResourceType();
            System.out.println(toPrint);
            first = false;
        }
        System.out.print("\tProduced: ");
        first = true;
        for(int i = 0; i< production.getProduced().size(); i++){
            toPrint = ((!first) ? "\t          " :"") + " > " + production.getProduced().get(i).getQuantity() + " " +
                    production.getProduced().get(i).getResourceType();
            System.out.println(toPrint);
            first = false;
        }
    }

    public void printFaithBoard(PlayerBoardView player, FaithTrackView faithTrack){
        System.out.println(" • FAITH");
        System.out.println("\t > Faith level is " + player.getFaithBoard().getFaith());
        for(int i=0;i<player.getFaithBoard().getPopeProgression().length;i++){
            System.out.print("\t > Pope’s Favor tiles number "+(i+1)+" is ");
            if(player.getFaithBoard().getPopeProgression()[i]){
                System.out.println("active and its value is " + faithTrack.getVaticanReports().get(i).getPopeValue());
            } else System.out.println("not active");
        }
    }

    public void printActions(){
        System.out.println("You can choose an action to play your turn!");
        System.out.println(" •1) Get resources from market ");
        System.out.println(" •2) Buy a development card ");
        System.out.println(" •3) Activate your production");
        System.out.println(" •4) Activate a leader card");
        System.out.println(" •5) Discard a leader card");
    }

    public void printWarehouse(WarehouseView warehouseView){
        String color;
        int i;
        System.out.println("This is your warehouse!");
        for(i = 1; i <= 3; i++) {
            int finalI = i;
            System.out.print(" ");
            for(int j = 0; j < i; j++) {
                if ((warehouseView.getShelves().stream().anyMatch(x -> x.getLevel() == finalI)) &&
                        (warehouseView.getShelves().stream().filter(x -> x.getLevel() == finalI).findFirst().get().getResources().getQuantity()>=j)) {
                    color = chooseColor(warehouseView.getShelves().stream().filter(x -> x.getLevel() == 1).findFirst().get().getResourceType());
                    System.out.print("[ " + color + "■" + color + RESET + " ]");
                } else {
                    System.out.print("[ x ]");
                }
            }
            System.out.println();
        }
    }

    public void printStrongbox(StrongboxView strongboxView){
        System.out.println("Your strongbox contains: ");
        if(strongboxView.getResources().size() > 0) {
            for (int i = 0; i < strongboxView.getResources().size(); i++) {
                System.out.println(" • " + strongboxView.getResources().get(i).getQuantity() + " " + strongboxView.getResources().get(i).getResourceType());
            }
        }
        else{
            System.out.println(" • Nothing! :(");
        }
    }
}
