package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.structures.MarketView;
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
        for(int i= 0; i< 3;i++){   //TODO: dove prendo le dimensioni della matrice
            for(int j = 0; j<4;j++){
                color = chooseColor(market.getMarbleMatrix()[i][j]);
                System.out.print("[ " + color + "■" + color + RESET + " ]");
            }
            System.out.println("[ x ]");
        }
        System.out.println("[ x ][ x ][ x ][ x ]");

        color = chooseColor(market.getFloatingMarble());
        System.out.println("The marble to place is " + color + "■" + color);
        System.out.print(RESET);
    }

    private String chooseColor(Marble m){
        if(m.getColor() == MarbleColors.BLUE){
            return BLUE_BRIGHT;
        }else if(m.getColor() == MarbleColors.GREY){
            return BLACK_BRIGHT;
        }else if(m.getColor() == MarbleColors.PURPLE){
            return PURPLE_BRIGHT;
        }else if(m.getColor() == MarbleColors.RED){
            return RED_BRIGHT;
        }else if(m.getColor() == MarbleColors.WHITE){
            return WHITE_BRIGHT;
        }else if(m.getColor() == MarbleColors.YELLOW){
            return YELLOW_BRIGHT;
        }
        else return RESET;
    }

    public void printLeaderCard(LeaderCard card){
        String toPrint;
        boolean first = true;
        System.out.println("♥ LEADER CARD ♥ ");
        System.out.print(" • Requirements: ");
        if(card.getRequirements().get(0) instanceof RequirementDev) {   //TODO: va bene get(0)? tnato 1 requirement c'e di sicuro
            for (Requirement req : card.getRequirements()) {
                toPrint = ((!first) ? ", " :"") + ((RequirementDev) req).getNumber() + " " +
                        ((RequirementDev) req).getColor() + " development cards level " +
                        ((RequirementDev) req).getLevel();
                System.out.print(toPrint);
                first = false;
            }
        }else if(card.getRequirements().get(0) instanceof RequirementRes) {
            for (Requirement req : card.getRequirements()) {
                System.out.print(((RequirementRes) req).getResource().getQuantity() + " " +
                        ((RequirementRes) req).getResource().getResourceType());
            }
        }
        System.out.println();
        System.out.println(" • The card value is " + card.getVP() + " victory point");

        System.out.print(" • The special ability is ");

        if(card.getAbility() instanceof AbilityDiscount) {
            System.out.println("a discount by 1 unit of " + ((AbilityDiscount) card.getAbility()).getResourceType());
        }else if(card.getAbility() instanceof AbilityMarble) {
            System.out.println("the power of getting a " + ((AbilityMarble) card.getAbility()).getResourceType() +
                    " from a white marble from the market");
        }else if(card.getAbility() instanceof AbilityProduction) {
            System.out.println("the production:  ");
            printProduction(((AbilityProduction) card.getAbility()).getProduction());
        }else if(card.getAbility() instanceof AbilityWarehouse) {
            System.out.println("to have an extra shelf to contain 1 " + ((AbilityWarehouse) card.getAbility()).getResourceType());
        }
    }

    public void printProduction(Production production){
        System.out.print("Consumed: ");
        boolean first = true;
        String toPrint;
        for(int i = 0; i< production.getConsumed().size(); i++){
            toPrint = ((!first) ? "          " :"") + " > " + production.getConsumed().get(i).getQuantity() + " " +
                    production.getConsumed().get(i).getResourceType();
            System.out.println(toPrint);
            first = false;
        }
        System.out.print("Produced: ");
        first = true;
        for(int i = 0; i< production.getProduced().size(); i++){
            toPrint = ((!first) ? "          " :"") + " > " + production.getProduced().get(i).getQuantity() + " " +
                    production.getProduced().get(i).getResourceType();
            System.out.println(toPrint);
            first = false;
        }

    }

}
