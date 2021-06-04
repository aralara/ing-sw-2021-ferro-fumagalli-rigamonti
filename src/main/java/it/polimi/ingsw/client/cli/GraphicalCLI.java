package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.structures.*;
import it.polimi.ingsw.server.model.cards.ability.AbilityMarble;
import it.polimi.ingsw.server.model.cards.card.*;
import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.server.model.storage.Production;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import it.polimi.ingsw.server.model.storage.Shelf;
import it.polimi.ingsw.server.saves.GameLibrary;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utils.Constants.MARKET_COLUMN_SIZE;
import static it.polimi.ingsw.utils.Constants.MARKET_ROW_SIZE;


public class GraphicalCLI {

    private static final String CHOICE_DELIMITERS = " ,.:;-_/\\";

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
     * Reads an int from the standard input, utilizing a blocking scanner
     * @return The integer entered by the user
     */
    public int getNextInt() {
        int value = -1;
        boolean valid;
        do {
            valid = true;
            try {
                value = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                printString("Invalid input! Try again: ");
                valid = false;
            }
        } while(!valid);
        return value;
    }

    /**
     * Reads a line from the standard input, utilizing a blocking scanner
     * @return The line read by the scanner
     */
    public String getNextLine() {
        return scanner.nextLine();
    }

    /**
     * Prints a string passed as a parameter
     * @param toPrint String to be printed
     */
    public void printString(String toPrint) {
        System.out.print(toPrint);
    }

    /**
     * Prints a string passed as a parameter adding a new line at the end
     * @param toPrint String to be printed
     */
    public void printlnString(String toPrint) {
        System.out.println(toPrint);
    }

    /**
     * Asks the user for their nickname and returns it
     * @return Returns a string containing the user's nickname
     */
    public String askNickname() {
        String nickname;
        boolean success;
        do {
            printString("Insert your nickname: ");
            nickname = getNextLine();
            success = !nickname.matches(GameLibrary.NONVALID_REGEX);
            if(!success)
                printlnString("The selected nickname contains unsupported characters");
        } while(!success);
        return nickname;
    }

    /**
     * Asks the user if he wants to go back and choose another action
     * @return The user's answer
     */
    public boolean askGoBack() {
        printString("Do you want to go back and choose another action?" +
                "\nIf you want to, insert YES: ");
        return isAnswerYes();
    }

    /**
     * Checks if the user inserts yes or y
     * @return true if the answer is yes, false otherwise
     */
    public boolean isAnswerYes() {
        return getNextLine().matches("(?i)(^y(?:es)?$)|(^$)");
    }

    /**
     * Asks the user on which shelf does he wants to place a resource
     * @param resource Resources to be placed
     * @param numberOfShelves Number of shelves in the warehouse
     * @param canDiscard True if the user can discard the resource, false otherwise
     * @return The shelf chosen by the user
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

    /**
     * Generic method to let the user choose an integer between an upper and a lower bound
     * @param lowerLimit Lower bound (not specified if null)
     * @param upperLimit Upper bound (not specified if null)
     * @param message String that is visualized as the input message
     * @param adjustP1 If true, all the values that the users sees or enters are counted with a + 1
     * @return Returns the integer chosen by the user
     */
    public int integerSelector(Integer lowerLimit, Integer upperLimit, String message, boolean adjustP1) {
        int choice;
        boolean isLow = lowerLimit != null, isUpp = upperLimit != null;
        Integer adjLow = isLow ? (adjustP1 ? lowerLimit + 1 : lowerLimit) : null;
        Integer adjUpp = isUpp ? (adjustP1 ? upperLimit + 1 : upperLimit) : null;
        String adjMessage = message;
        if(isLow) {
            if(isUpp)
                adjMessage += " (between " + adjLow + " and " + adjUpp + ")";
            else
                adjMessage += " (greater than " + adjLow + ")";
        }
        else if(isUpp)
            adjMessage += " (greater than " + adjUpp + ")";
        adjMessage += ": ";
        do {
            printString(adjMessage);
            choice = getNextInt();
        }
        while ((isLow && choice < adjLow) || (isUpp && choice > adjUpp));
        return adjustP1 ? choice - 1 : choice;
    }

    /**
     * Generic method to let the user choose multiple objects from a list of possible options
     * @param <T> Type of the objects
     * @param list List of possible options
     * @param printObject Consumer function that is called to visualize a single option
     * @param printMessage Runnable function that is called to visualize the first message
     * @param printInput Runnable function that is called to visualize the input message
     * @param printOnlyOption Runnable function that is called to visualize the only option notification
     * @param multiChoice True if the user is able to select multiple options at once, separated by CHOICE_DELIMITERS
     * @param minChoice If multiChoice is true, minimum number of possible choices (0 or lower for no limit)
     * @param maxChoice If multiChoice is true, maximum number of possible choices (0 or lower for no limit)
     * @param maxCopy If multiChoice is true, maximum number of possible copies of the same object (0 or lower
     *                for no limit)
     * @return Returns the objects chosen by the user
     */
    public <T> List<T> objectOptionSelector(List<T> list, Consumer<T> printObject,
                                      Runnable printMessage,
                                      Runnable printInput, Runnable printOnlyOption,
                                      boolean multiChoice, int minChoice, int maxChoice, int maxCopy) {
        if(list.size() > 0) {
            if(list.size() == 1) {
                T opt = list.get(0);
                if(printOnlyOption != null)
                    printOnlyOption.run();
                else
                    printString("Only one option available: ");
                printObject.accept(opt);
                return Collections.singletonList(opt);
            }
            List<Integer> indexes;
            if(printMessage != null)
                printMessage.run();
            else
                printlnString("You can choose an option from the following: ");
            printNumberedList(list, printObject);
            do {
                if(printInput != null)
                    printInput.run();
                else
                    printString("Choose a valid option: ");
                if(multiChoice)
                    try {
                        indexes = parseMultiChoice(getNextLine());
                    } catch(NumberFormatException e) { indexes = null; }
                else
                    indexes = Collections.singletonList(getNextInt() - 1);
            } while(indexes == null ||                                                                      //Indexes are not empty
                    (multiChoice && indexes.size() < minChoice) ||                                          //Number of choices greater or equal than minimum
                    (multiChoice && maxChoice > 0 && indexes.size() > maxChoice) ||                         //Number of choices lower or equal than maximum
                    (multiChoice && maxCopy > 0 && indexes.stream()
                            .collect(Collectors.groupingBy(Integer::intValue, Collectors.counting()))
                            .values().stream().allMatch(count -> count > maxCopy)) ||                       //Number of copies lower or equal than maximum
                    indexes.stream().anyMatch(index -> index < 0 || index >= list.size()));                 //Indexes are part of the list
            return indexes.stream().map(list::get).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * Parses an input string in order to get a list of integers separated by CHOICE_DELIMITERS, 1 is subtracted from
     * all the numbers
     * @param input Input string to parse
     * @return Returns a list of integers containing all the separated inputs
     * @throws NumberFormatException Throws a NumberFormatException if an input isn't an integer
     */
    private List<Integer> parseMultiChoice(String input) throws NumberFormatException {
        List<Integer> indexes = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(input, CHOICE_DELIMITERS);
        while(tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (token != null && !token.equals(""))
                indexes.add(Integer.parseInt(token) - 1);
        }
        return indexes;
    }

    /**
     * Prints a list of objects giving their an ordinal value a visual representation
     * @param list List of objects to be printed
     * @param printConsumer Consumer function that is called to visualize a single object
     * @param <T> Type of the objects
     */
    public <T> void printNumberedList(List<T> list, Consumer<T> printConsumer) {
        list.stream().collect(HashMap<Integer, T>::new,
                (m, elem) -> m.put(m.size() + 1, elem),
                (m1, m2) -> {}).forEach((n, elem) -> {
                    printString(n + ") ");
                    printConsumer.accept(elem);
                });
    }

    /**
     * Prints a list of resources using the symbol (■)
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
     * Prints a resource with its corresponding text
     * @param resource Resource to be printed
     */
    public void printResource(Resource resource){
        printString(resource.getQuantity() + " " + resource.getResourceType());
    }

    /**
     * Prints a list of resources
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
     * Prints the market object
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
     * Chooses a color to print given a resource type
     * @param resourceType Resource type corresponding to a color
     * @return The chosen color
     */
    private String chooseColor(ResourceType resourceType){  //TODO: sostituire con un enum
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
     * Prints a leader card
     * @param leaderCard Leader card to be printed
     */
    public void printLeaderCard(LeaderCard leaderCard){
        printString(leaderCard.toString());
    }

    /**
     * Prints a development card
     * @param developmentCard Development card to be printed
     */
    public void printDevelopmentCard(DevelopmentCard developmentCard){
        printString(developmentCard.toString());
    }

    /**
     * Prints a Lorenzo Card
     * @param lorenzoCard Lorenzo card to be printed
     */
    public void printLorenzoCard(LorenzoCard lorenzoCard){
        printString(lorenzoCard.toString());
    }

    /**
     * Prints a leader board
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
     * Prints a leader card hand
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
     * Prints the first development card for each development deck
     * @param developmentDecks List of development decks to be printed
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

    public void printLeaderAbilityMarble(List<AbilityMarble> abilities){    //TODO: cancellare?
        int i=0;
        for (AbilityMarble abilityMarble : abilities) {
            printlnString((i + 1) + ": " + abilityMarble.getResourceType().toString());
            i++;
        }
    }

    /**
     * Prints a production
     * @param production Production to be printed
     */
    public void printProduction(Production production){
        printString(production.toString());
    }

    /**
     * Prints a faith board object
     * @param player Player board to be printed
     * @param faithTrack FaithTrack information to contextualize the faith board
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
     * Prints the faithTrack
     * @param faithLevel User's faith level
     * @param faithTrack FaithTrack object
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
     * Checks if a space in the faith track is a vatican report
     * @param faithTrack FaithTack object
     * @param faith Space that need to be checked (corresponding to a certain amount of faith)
     * @return True if the space is a vatican report, false otherwise
     */
    private boolean isVaticanReport(FaithTrackView faithTrack, int faith){  //TODO: spostare nel faithTrackView, qui non ha molto senso
        for(int i = 0; i<faithTrack.getVaticanReports().size();i++){
            if(faith >= faithTrack.getVaticanReports().get(i).getMin() && faith <= faithTrack.getVaticanReports().get(i).getMax()){
                return true;
            }
        }
        return false;
    }

    /**
     * Prints a warehouse object
     * @param warehouseView Warehouse to be printed
     * @param showLevel True if levels need to be shown, false otherwise
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
     * Prints special leader's shelves
     * @param warehouseView Warehouse to be printed
     * @param showLevel True if levels need to be shown, false otherwise
     */
    public void printExtraShelfLeader(WarehouseView warehouseView, boolean showLevel){  //TODO: va testato in game quando attiviamo delle leader card con abilità warehouse
        int level = 2;
        String color;
        int specialWarehouse = (int)warehouseView.getShelves().stream().filter(Shelf::isLeader).count();
        if(specialWarehouse > 0){
            printlnString("\nSpecial warehouse:");
            List<Shelf> specialShelf = warehouseView.getShelves().stream()
                    .filter(x -> x.getLevel() == level && x.isLeader()).collect(Collectors.toList());
            for(int i = 0; i < specialWarehouse;i++) {
                if(showLevel){
                    printString((4+i) + ":");
                }
                printString(" ");
                for(int j = 0; j < level; j++){
                    color = chooseColor(specialShelf.get(i).getResourceType());
                    printString("[ " + color + (specialShelf.get(i).getResources().getQuantity()>j ? "■" : "x")
                            + color + RESET + " ]");
                }
            }
        }
        printlnString("");
    }

    /**
     * Prints a strongbox object
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
     * Prints a list of all the different available storages
     */
    public void printChooseStorage(){
        printlnString("\nChoose the storage where remove the resource from:");
        printlnString("•1) Warehouse");
        printlnString("•2) Special Warehouse");
        printlnString("•3) Strongbox");
    }

    /**
     * Prints a warehouse object with every shelf (special and not)
     * @param warehouseView Warehouse to be printed
     * @param showLevel True if levels need to be shown, false otherwise
     */
    public void printWarehouseConfiguration(WarehouseView warehouseView, boolean showLevel){
        printWarehouse(warehouseView, showLevel);
        printExtraShelfLeader(warehouseView, showLevel);
    }

    /**
     * Prints all of the player's information
     * @param playerBoardView PlayerBoard that contains all the information
     * @param faithTrackView FaithTrack information
     */
    public void printPlayer(PlayerBoardView playerBoardView,FaithTrackView faithTrackView){
        printlnString("\n" + playerBoardView.getNickname() + "'s board:");
        printFaithBoard(playerBoardView, faithTrackView);
        printWarehouseConfiguration(playerBoardView.getWarehouse(), false);
        printStrongbox(playerBoardView.getStrongbox());
        printDevelopmentBoard(playerBoardView.getDevelopmentBoard());
        printString("Leaders in hand: \n");
        printLeaderHand(playerBoardView.getLeaderBoard());
        printlnString("Leaders placed on the board: \n");
        printLeaderBoard(playerBoardView.getLeaderBoard());
    }

    /**
     * Prints Lorenzo's board
     * @param faith Lorenzo's faith level
     * @param faithTrack FaithTrack information
     */
    public void printLorenzo(int faith, FaithTrackView faithTrack){
        printFaithTrack(faith, faithTrack);
        printlnString("\t > Lorenzo il Magnifico's faith level is " + faith);
    }

    /**
     * Prints a development board object
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
