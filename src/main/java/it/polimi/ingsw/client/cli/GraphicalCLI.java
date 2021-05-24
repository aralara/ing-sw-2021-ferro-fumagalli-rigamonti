package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.structures.*;
import it.polimi.ingsw.server.model.cards.ability.AbilityMarble;
import it.polimi.ingsw.server.model.cards.card.*;
import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.server.model.storage.Production;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import it.polimi.ingsw.server.model.storage.Shelf;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utils.Constants.MARKET_COLUMN_SIZE;
import static it.polimi.ingsw.utils.Constants.MARKET_ROW_SIZE;


public class GraphicalCLI {

    public static final String RESET = "\033[0m";  // Text Reset

    public static final String BLACK_BRIGHT = "\033[0;90m";  // BLACK
    public static final String RED_BRIGHT = "\033[0;91m";    // RED
    public static final String GREEN_BRIGHT = "\033[0;92m";  // GREEN
    public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
    public static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
    public static final String CYAN_BRIGHT = "\033[0;96m";   // CYAN
    public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
    public static final String WHITE_BRIGHT = "\033[0;97m";  // WHITE

    private final Scanner scanner;


    public GraphicalCLI() {
        scanner = new Scanner(System.in);
    }

    /**
     * Read an int from input
     * @return The value entered by the user
     */
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

    /**
     * Read a string by the scanner
     * @return The string read by the scanner
     */
    public String getNext() {
        return scanner.next();
    }

    /**
     * Read a line by the scanner
     * @return The line read by the scanner
     */
    public String getNextLine() {
        return scanner.nextLine();
    }

    /**
     * Print a string passed by parameters
     * @param toPrint String to be printed
     */
    public void printString(String toPrint) {
        System.out.print(toPrint);
    }

    /**
     * Print a string passed by parameters with a new line
     * @param toPrint String to be printed
     */
    public void printlnString(String toPrint) {
        System.out.println(toPrint);
    }

    /**
     * Ask the user if he wants to go back and choose another action
     * @return The user's answer
     */
    public boolean askGoBack() {
        printString("Do you want to go back and choose another action?" +
                "\nIf you want to, insert YES: ");
        return isAnswerYes();
    }

    /**
     * Check if the user insert yes
     * @return true if the answer is yes, false otherwise
     */
    public boolean isAnswerYes() {
        return getNext().matches("(?i)YES");
    }

    /**
     * Ask the user on witch shelf he wants to place some resources
     * @param resource Resource to be placed
     * @param numberOfShelves Number of shelf in the warehouse
     * @param canDiscard true if the user can discard the resourcce, false otherwise
     * @return The shelf choosen by the user
     */
    public int askWhichShelf(Resource resource, int numberOfShelves, boolean canDiscard) {
        int level;
        printString("Where do you want to place " + resource.getResourceType()
                + (canDiscard ? "? (0 to discard it, " : "? (") + "-1 to restore warehouse): ");
        level = getNextInt();
        while ((canDiscard && level < -1) || (!canDiscard && level == 0) || level > numberOfShelves){
            printString("Choose a valid shelf: ");
            level = getNextInt();
        }
        return level;
    }

    public <T> T objectOptionSelector(List<T> list, Consumer<T> printObject) {
        if(list.size() > 0) {
            if(list.size() == 1) {
                T opt = list.get(0);
                printString("Only one option available: ");
                printObject.accept(opt);
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

    /**
     * Print a list of resources with graphical (■)
     * @param resources Resources to be printed
     */
    public void printGraphicalResources(List<Resource> resources){
        String color;
        for(Resource res : resources){
            color = chooseColor(res.getResourceType());
            printString(color + " ■ " + color + RESET);
        }
        printlnString("");
    }

    /**
     * Print a resource with text
     * @param resource Resource to be printed
     */
    public void printResource(Resource resource){
        printString(resource.getQuantity() + " " + resource.getResourceType());
    }

    /**
     * Print a list of resources
     * @param resources List of resources to be printed
     */
    public void printResources(List<Resource> resources){
        boolean first = true;
        for(Resource res : resources){
            printString(first ? "" : ", ");
            printResource(res);
            first = false;
        }
    }

    /**
     * Print the market
     * @param market Market to be printed
     */
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

    /**
     * Choose a color to print string
     * @param resourceType Resource that correspond to a color
     * @return The color choosen
     */
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

    /**
     * Print a leader card
     * @param leaderCard Leader card to be printed
     */
    public void printLeaderCard(LeaderCard leaderCard){
        printString(leaderCard.cardToString());
    }

    /**
     * Print a development card
     * @param developmentCard Development card to be printed
     */
    public void printDevelopmentCard(DevelopmentCard developmentCard){
        printString(developmentCard.cardToString());
    }

    /**
     * Print a Lorenzo Card
     * @param lorenzoCard Lorenzo card to be printed
     */
    public void printLorenzoCard(LorenzoCard lorenzoCard){
        printString(lorenzoCard.cardToString());
    }

    /**
     * Print a leader board
     * @param leaderBoard Leader board to be printed
     */
    public void printLeaderBoard(LeaderBoardView leaderBoard){
        if(!leaderBoard.getBoard().getCards().isEmpty()){
            printNumberedList((List<LeaderCard>)(List<?>)leaderBoard.getBoard().getCards(),this::printLeaderCard);
        }
        else printlnString(" • Empty");
        printlnString("");
    }

    /**
     * Print a leader hand
     * @param leaderBoard Leader hand to be printed
     */
    public void printLeaderHand(LeaderBoardView leaderBoard){
        if(!leaderBoard.getHand().getCards().isEmpty()) {printlnString("");
            printNumberedList((List<LeaderCard>) (List<?>) leaderBoard.getHand().getCards(), this::printLeaderCard);
        }
        else printlnString(" • Empty");
        printlnString("");
    }

    /**
     * Print the first development card for each development deck
     * @param developmentDecks List of development deck to be printed
     */
    public void printDevelopmentDeckTop(List<DevelopmentDeckView> developmentDecks) {
        printlnString("The development decks:");
        List<DevelopmentCard> developmentCards =  new ArrayList<>();
        for(DevelopmentDeckView temp : developmentDecks){
            if(!temp.getDeck().isEmpty())
                developmentCards.add((DevelopmentCard) temp.getDeck().getCards().get(0));
        }
        printNumberedList(developmentCards, this::printDevelopmentCard);
    }

    public void printLeaderAbilityMarble(List<AbilityMarble> abilities){
        int i=0;
        for (AbilityMarble abilityMarble : abilities) {
            printlnString((i + 1) + ": " + abilityMarble.getResourceType().toString());
            i++;
        }
    }

    /**
     * Print a production
     * @param production Production to be printed
     */
    public void printProduction(Production production){
        printString(production.productionToPrint());
    }

    /**
     * Print a faith board
     * @param player Player board to be printed
     * @param faithTrack Faithtrack information
     */
    public void printFaithBoard(PlayerBoardView player, FaithTrackView faithTrack){
        printFaithTrack(player.getFaithBoard().getFaith(), faithTrack);
        printlnString("\t > Faith level is " + player.getFaithBoard().getFaith());
        for(int i=0;i<player.getFaithBoard().getPopeProgression().length;i++){
            printString("\t > Pope’s Favor tiles number "+(i+1)+" is ");
            if(player.getFaithBoard().getPopeProgression()[i]){
                printlnString("active and its value is " + faithTrack.getVaticanReports().get(i).getPopeValue());
            } else printlnString("not active");
        }
        printlnString("");
    }

    /**
     * Print a faithTrack
     * @param faithLevel Faith level of the user
     * @param faithTrack Faithtrack information
     */
    private void printFaithTrack(int faithLevel, FaithTrackView faithTrack){
        String color;
        int maxSize = faithTrack.getVaticanReports().get(faithTrack.getVaticanReports().size()-1).getMax();
        for(int i = 1; i <= maxSize; i++){
            if(isVaticanReport(faithTrack,i)){
                color = PURPLE_BRIGHT;
            }else{
                color = CYAN_BRIGHT;
            }
            printString(color + (((faithLevel == i) ||
                    ((i==maxSize) && (faithLevel > maxSize))) ?
                    "[" + RED_BRIGHT + " X " + color +  "]" : "[ • ] ") + color + RESET);
        }
        printlnString("");
    }

    /**
     * Check if a space in the faith track is a vatican report
     * @param faithTrack Faithtrack information
     * @param faith Level of space to be checked
     * @return True if the space is a vatican report, false otherwise
     */
    private boolean isVaticanReport(FaithTrackView faithTrack, int faith){
        for(int i = 0; i<faithTrack.getVaticanReports().size();i++){
            if(faith >= faithTrack.getVaticanReports().get(i).getMin() && faith <= faithTrack.getVaticanReports().get(i).getMax()){
                return true;
            }
        }
        return false;
    }

    /**
     * Print a menu with a list of action
     * @param isPlayerTurn true if it's the player turn, false otherwise
     */
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

    /**
     * Print a warehouse
     * @param warehouseView Warehouse to be printed
     * @param showLevel True if we want to show levels, false otherwise
     */
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

    /**
     * Print special leader's shelves
     * @param warehouseView Warehouse to be printed
     * @param showLevel True if we want to show levels, false otherwise
     */
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
                    printString("[ " + color + (specialShelf.get(i).getResources().getQuantity()>j ? "■" : "x")
                            + color + RESET + " ]");
                    printlnString("");
                }
            }
        }
        printlnString("");
    }

    /**
     * Print a strongbox
     * @param strongboxView Strongbox to be printed
     */
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

    /**
     * Print a list that contains different storages
     */
    public void printChooseStorage(){
        printlnString("\nChoose the storage where remove the resource from:");
        printlnString("•1) Warehouse");
        printlnString("•2) Special Warehouse");
        printlnString("•3) Strongbox");
    }

    /**
     * Print warehouse with every shelf (special and not)
     * @param warehouseView Warehouse to be printed
     * @param showLevel True if you want to show a numbered list
     */
    public void printWarehouseConfiguration(WarehouseView warehouseView, boolean showLevel){
        printWarehouse(warehouseView, showLevel);
        printExtraShelfLeader(warehouseView, showLevel);
    }

    /**
     * Print a player's information
     * @param playerBoardView player board that contains all the information
     * @param faithTrackView Faithtrack information
     */
    public void printPlayer(PlayerBoardView playerBoardView,FaithTrackView faithTrackView){
        printlnString("\n" + playerBoardView.getNickname() + "'s board:");
        printFaithBoard(playerBoardView, faithTrackView);
        printWarehouseConfiguration(playerBoardView.getWarehouse(), false);
        printStrongbox(playerBoardView.getStrongbox());
        printDevelopmentBoard(playerBoardView.getDevelopmentBoard());
        printString("Leaders on hand: \n");
        printLeaderHand(playerBoardView.getLeaderBoard());
        printlnString("Leaders placed on the board: \n");
        printLeaderBoard(playerBoardView.getLeaderBoard());
    }

    /**
     * Print the board of Lorenzo
     * @param faith Lorenzo's faith level
     * @param faithTrack Faithtrack information
     */
    public void printLorenzo(int faith, FaithTrackView faithTrack){
        printFaithTrack(faith, faithTrack);
        printlnString("\t > Lorenzo il Magnifico's faith level is " + faith);
    }

    /**
     * Print a development board
     * @param developmentBoard Development board to be printed
     */
    public void printDevelopmentBoard(DevelopmentBoardView developmentBoard){
        printlnString("Development spaces:");
        List<Deck> spaces = developmentBoard.getSpaces();
        for(int i=0; i<spaces.size();i++){
            printString((i+1) + ") ");
            if(spaces.get(i).isEmpty()) {
                printlnString("empty");
            }
            else {
                printDevelopmentCard((DevelopmentCard) spaces.get(i).get(0));
            }
        }
        printlnString("");
    }
}
