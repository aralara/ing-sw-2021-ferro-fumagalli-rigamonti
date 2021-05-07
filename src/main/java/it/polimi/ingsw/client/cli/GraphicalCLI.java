package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.structures.MarketView;
import it.polimi.ingsw.server.model.market.Marble;
import it.polimi.ingsw.server.model.market.MarbleColors;

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
        System.out.println("The marble to place is " + color + "■ ■" + color);
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

}
