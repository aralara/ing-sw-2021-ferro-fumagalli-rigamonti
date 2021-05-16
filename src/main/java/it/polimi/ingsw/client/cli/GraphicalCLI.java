package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.structures.*;
import it.polimi.ingsw.server.model.cards.ability.AbilityDiscount;
import it.polimi.ingsw.server.model.cards.ability.AbilityMarble;
import it.polimi.ingsw.server.model.cards.ability.AbilityProduction;
import it.polimi.ingsw.server.model.cards.ability.AbilityWarehouse;
import it.polimi.ingsw.server.model.cards.card.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.server.model.cards.requirement.Requirement;
import it.polimi.ingsw.server.model.cards.requirement.RequirementDev;
import it.polimi.ingsw.server.model.cards.requirement.RequirementRes;
import it.polimi.ingsw.server.model.storage.Production;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import it.polimi.ingsw.server.model.storage.Shelf;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utils.Constants.MARKET_COLUMN_SIZE;
import static it.polimi.ingsw.utils.Constants.MARKET_ROW_SIZE;


public class GraphicalCLI { //TODO: sostituire System.out.println

    public static final String RESET = "\033[0m";  // Text Reset

    public static final String BLACK_BRIGHT = "\033[0;90m";  // BLACK
    public static final String RED_BRIGHT = "\033[0;91m";    // RED
    public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
    public static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
    public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
    public static final String WHITE_BRIGHT = "\033[0;97m";  // WHITE

    private final Scanner scanner;


    public GraphicalCLI() {
        scanner = new Scanner(System.in);
    }


    public int getNextInt() {
        int value = -1;
        boolean valid;
        do {
            valid = true;
            try {
                value = scanner.nextInt();
            } catch (InputMismatchException e) {
                printString("Invalid input! Try again: ");
                scanner.nextLine();
                valid = false;
            }
        } while(!valid);
        return value;
    }

    public String getNext() {
        return scanner.next();
    }

    public String getNextLine() {
        return scanner.nextLine();
    }

    public void printString(String toPrint) {
        System.out.print(toPrint);
    }

    public void printlnString(String toPrint) {
        System.out.println(toPrint);
    }

    public boolean askGoBack() {
        printString("Do you want to go back and choose another action?" +
                "\nIf you want to, insert YES: ");
        return isAnswerYes();
    }

    public boolean isAnswerYes() {
        return getNext().matches("(?i)YES");
    }

    public int askWhichShelf(Resource resource, int numberOfShelves, boolean rearranged, boolean canDiscard) {
        int level;
        if(rearranged) {
            printString("Do you want to restore warehouse to its original configuration? ");
            if (isAnswerYes())
                return -1;
        }
        printString("Where do you want to place " + resource.getResourceType()
                + (canDiscard ? "? (0 to discard it) " : "? "));
        level = getNextInt();
        while ((canDiscard && level<0) || (!canDiscard && level<=0) || level>numberOfShelves){
            printString("Choose a valid shelf: ");
            level = getNextInt();
        }
        return level;
    }

    public <T> T objectOptionSelector(List<T> list, Consumer<T> printObject) {
        if(list.size() > 0) {
            if(list.size() == 1) {
                T opt = list.get(0);
                printlnString(opt.toString() + " is the only option available");
                return opt;
            }
            int index;
            printlnString("You can choose an option from the following: ");
            printNumberedList(list, printObject);
            do {
                printString("Please choose a valid option: ");
                index = getNextInt() - 1;
            } while(index < 0 || index >= list.size());
            return list.get(index);
        }
        return null;
    }

    public <T> void printNumberedList(List<T> list, Consumer<T> printConsumer) {
        list.stream().collect(HashMap<Integer, T>::new,
                (m, elem) -> m.put(m.size() + 1, elem),
                (m1, m2) -> {}).forEach((n, elem) -> {
                    printString(n + ") ");
                    printConsumer.accept(elem);
                });
    }

    public void printDevelopmentDeckTop(List<DevelopmentDeckView> developmentDecks) {
        printlnString("The development decks:");
        List<DevelopmentCard> developmentCards =  new ArrayList<>();
        for(DevelopmentDeckView temp : developmentDecks){
            if(!temp.getDeck().isEmpty())
                developmentCards.add((DevelopmentCard) temp.getDeck().getCards().get(0));
        }
        printNumberedList(developmentCards, this::printDevelopmentCard);
    }

    public void printMarket(MarketView market){
        printlnString("\nThe market:");
        String color;
        for(int i= 0; i<MARKET_ROW_SIZE.value();i++){
            for(int j = 0; j<MARKET_COLUMN_SIZE.value();j++){
                color = chooseColor(market.getMarbleMatrix()[i][j].getResourceType());
                printString("[ " + color + "■" + color + RESET + " ]");
            }
            printlnString(" [ " + (i+1) + " ]");
        }
        printlnString("[ 1 ][ 2 ][ 3 ][ 4 ]");

        color = chooseColor(market.getFloatingMarble().getResourceType());
        printlnString("Extra marble: " + color + "■" + color);
        printString(RESET);
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

    public void printLeaderCard(LeaderCard leaderCard){
        String toPrint;
        boolean first = true;
        if(leaderCard.getID() != -1) {
            printlnString(" LEADER CARD");
            printString(" • Requirements: ");
            if (leaderCard.getRequirements().get(0) instanceof RequirementDev) {   //TODO: mettere to string alle carte
                for (Requirement req : leaderCard.getRequirements()) {
                    toPrint = ((!first) ? ", " : "") + ((RequirementDev) req).getNumber() + " " +
                            ((RequirementDev) req).getColor() + " level " +
                            ((RequirementDev) req).getLevel() + " development cards";
                    printString(toPrint);
                    first = false;
                }
            } else if (leaderCard.getRequirements().get(0) instanceof RequirementRes) {
                for (Requirement req : leaderCard.getRequirements()) {
                    printResource(((RequirementRes) req).getResource());
                }
            }
            printlnString("");
            printlnString(" • Victory points: " + leaderCard.getVP());

            printString(" • Special ability: You ");

            if (leaderCard.getAbility() instanceof AbilityDiscount) {
                printlnString("can get 1 " + ((AbilityDiscount) leaderCard.getAbility()).getResourceType()
                        + " off the cost of development cards");
            } else if (leaderCard.getAbility() instanceof AbilityMarble) {
                printlnString("can get a " + ((AbilityMarble) leaderCard.getAbility()).getResourceType() +
                        " from the white marbles in the market");
            } else if (leaderCard.getAbility() instanceof AbilityProduction) {
                printlnString("gain access to the following production:  ");
                printProduction(((AbilityProduction) leaderCard.getAbility()).getProduction());
            } else if (leaderCard.getAbility() instanceof AbilityWarehouse) {
                printlnString("gain an extra shelf to contain 2 units of " + ((AbilityWarehouse) leaderCard.getAbility())
                        .getResourceType());
            }
        }
        else {
            printlnString(" LEADER CARD");
            printlnString(" • The leader card is covered, you can't see it!");
        }
    }

    public void printLeaderAbilityMarble(List<AbilityMarble> abilities){
        int i=0;
        for (AbilityMarble abilityMarble : abilities) {
            printlnString((i + 1) + ": " + abilityMarble.getResourceType().toString());
            i++;
        }
    }

    public void printProduction(Production production){
        printString("\tConsumed: ");
        boolean first = true;
        String toPrint;
        for(int i = 0; i< production.getConsumed().size(); i++){
            printString(((!first) ? "\t          " :"") + " > ");
            printResource(production.getConsumed().get(i));
            printlnString("");
            first = false;
        }
        printString("\tProduced: ");
        first = true;
        for(int i = 0; i< production.getProduced().size(); i++){
            printString(((!first) ? "\t          " :"") + " > ");
            printResource(production.getProduced().get(i));
            printlnString("");
            first = false;
        }
    }

    public void printFaithBoard(PlayerBoardView player, FaithTrackView faithTrack){
        printlnString("Faith:");
        printlnString("\t > Faith level is " + player.getFaithBoard().getFaith());
        for(int i=0;i<player.getFaithBoard().getPopeProgression().length;i++){
            printString("\t > Pope’s Favor tiles number "+(i+1)+" is ");
            if(player.getFaithBoard().getPopeProgression()[i]){
                printlnString("active and its value is " + faithTrack.getVaticanReports().get(i).getPopeValue());
            } else printlnString("not active");
        }
        printlnString("");
    }

    public void printActions(boolean isPlayerTurn){
        printlnString(" Here's the menu: ");
        if(isPlayerTurn) {
            printlnString(" • 1) Get resources from market ");
            printlnString(" • 2) Buy a development card ");
            printlnString(" • 3) Activate your productions");
            printlnString(" • 4) Activate a leader card");
            printlnString(" • 5) Discard a leader card");
        }
        printlnString((isPlayerTurn ? " • 6" : " •1") + ") View market and development decks");
        printlnString((isPlayerTurn ? " • 7" : " •2") + ") View your board");
        printlnString((isPlayerTurn ? " • 8" : " •3") + ") View opponents' boards");
        printlnString((isPlayerTurn ? " • 9" : " •4") + ") Rearrange Warehouse");
        if(isPlayerTurn)
            printlnString(" •10) End turn");
        printString("Choose an action to do"+ (isPlayerTurn ? " on your turn: " : ": "));
    }

    public void printWarehouse(WarehouseView warehouseView, boolean showLevel){
        String color;
        int i;
        printlnString("Warehouse:");
        for(i = 1; i <= 3; i++) {
            int finalI = i;
            if(showLevel){
                printString(i + ":");
            }
            printString(" ");
            for(int j = 0; j < i; j++) {
                if ((warehouseView.getShelves().stream().anyMatch(x -> x.getLevel() == finalI && !x.isLeader())) &&
                        (warehouseView.getShelves().stream().filter(x -> x.getLevel() == finalI && !x.isLeader())
                                .findFirst().get().getResources().getQuantity()>j)) {
                    color = chooseColor(warehouseView.getShelves().stream().filter(x -> x.getLevel() == finalI)
                            .findFirst().get().getResourceType());
                    printString("[ " + color + "■" + color + RESET + " ]");
                } else {
                    printString("[ x ]");
                }
            }
            printlnString("");
        }
    }

    public void printExtraShelfLeader(WarehouseView warehouseView, boolean showLevel){  //TODO: va testato in game quando attiviamo delle leader card con abilità warehouse
        int level = 2;
        String color;
        int specialWarehouse = (int)warehouseView.getShelves().stream().filter(Shelf::isLeader).count();
        if(specialWarehouse > 0){
            printlnString("\nSpecial warehouse:");
            List<Shelf> specialShelf = warehouseView.getShelves().stream()
                    .filter(x -> x.getLevel() == level && x.isLeader()).collect(Collectors.toList());
            for(int i = 0; i < specialWarehouse;i++){
                if(showLevel){
                    printString((4+i) + ":");
                }
                printString(" ");
                for(int j = 0; j < level; j++){
                    color = chooseColor(specialShelf.get(i).getResourceType());
                    /*if (specialShelf.get(i).getResources().getQuantity()>j) { //TODO: cambiato
                        printString("[ " + color + "■" + color + RESET + " ]");
                    } else {
                        printString("[ x ]");
                    }*/
                    printString("[ " + color + (specialShelf.get(i).getResources().getQuantity()>j ? "■" : "x")
                            + color + RESET + " ]");
                }
            }
        }
    }

    public void printStrongbox(StrongboxView strongboxView){
        String color;
        printlnString("\nStrongbox: ");
        if(strongboxView.getResources().size() > 0) {
            for (int i = 0; i < strongboxView.getResources().size(); i++) {
                printString(" • ");
                printResource(strongboxView.getResources().get(i));
                color = chooseColor(strongboxView.getResources().get(i).getResourceType());
                printString(color + " ■ " + color + RESET);
                printlnString("");
            }
        }
        else{
            printlnString(" • Empty");
        }
        printlnString("");
    }

    public void printGraphicalResources(List<Resource> resources){
        String color;
        for(Resource res : resources){
            color = chooseColor(res.getResourceType());
            printString(color + " ■ " + color + RESET);
        }
        printlnString("");
    }

    public void printResource(Resource resource){
        printString(resource.getQuantity() + " " + resource.getResourceType());
    }

    public void printResources(List<Resource> resources){
        boolean first = true;
        for(Resource res : resources){
            printString(first ? "" : ", ");
            printResource(res);
            first = false;
        }
    }

    public void printDevelopmentCard(DevelopmentCard developmentCard){
        boolean first = true;
        printlnString(" DEVELOPMENT CARD");
        printlnString(" • This is a " + developmentCard.getColor() + " card level " + developmentCard.getLevel());

        printlnString(" • Victory points: " + developmentCard.getVP());
        printString(" • Cost: ");
        for(int i = 0; i<developmentCard.getCost().size();i++){
            printString((!first) ? ", " : "");
            printResource(developmentCard.getCost().get(i));
            first = false;
        }
        printlnString("");
        printlnString(" • Production that can be activated: ");
        printProduction(developmentCard.getProduction());
    }

    public void printChooseStorage(){
        printlnString("\nChoose the storage where remove the resource from:");
        printlnString("•1) Warehouse");
        printlnString("•2) Special Warehouse");
        printlnString("•3) Strongbox");
    }

    public void printWarehouseConfiguration(WarehouseView warehouseView, boolean showLevel){
        printWarehouse(warehouseView, showLevel);
        printExtraShelfLeader(warehouseView, showLevel);
    }

    public void printPlayer(PlayerBoardView playerBoardView,FaithTrackView faithTrackView){
        printlnString("\n" + playerBoardView.getNickname() + "'s board:");
        printFaithBoard(playerBoardView, faithTrackView);
        printWarehouseConfiguration(playerBoardView.getWarehouse(), false);
        printStrongbox(playerBoardView.getStrongbox());
        printDevelopmentBoard(playerBoardView.getDevelopmentBoard());
        printLeaderHand(playerBoardView.getLeaderBoard());
        printString("Leaders placed on the board: ");
        printLeaderBoard(playerBoardView.getLeaderBoard());
    }

    public void printLorenzo(int faith){
        printlnString("Lorenzo il Magnifico's faith level is " + faith);
    }

    public void printDevelopmentBoard(DevelopmentBoardView developmentBoard){
        printlnString("Development spaces:");
        List<Deck> spaces = developmentBoard.getSpaces();
        for(int i=0; i<spaces.size();i++){
            printString((i+1) + ") ");
            if(spaces.get(i).isEmpty()) printlnString("empty");
            else printDevelopmentCard((DevelopmentCard) spaces.get(i).get(0));
        }
        printlnString("");
    }

    public void printLeaderBoard(LeaderBoardView leaderBoard){
        if(!leaderBoard.getBoard().getCards().isEmpty()){
            printlnString("");
            printNumberedList((List<LeaderCard>)(List<?>)leaderBoard.getBoard().getCards(),this::printLeaderCard);
        }
        else printString("empty");
        printlnString("");
    }

    public void printLeaderHand(LeaderBoardView leaderBoard){
        if(!leaderBoard.getHand().getCards().isEmpty()) {printlnString("");
            printNumberedList((List<LeaderCard>) (List<?>) leaderBoard.getHand().getCards(), this::printLeaderCard);
        }
        else printString("empty");
        printlnString("");
    }
}
