package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.structures.*;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.server.model.cards.deck.DevelopmentDeck;
import it.polimi.ingsw.server.model.faith.VaticanReport;
import it.polimi.ingsw.server.model.market.Marble;
import it.polimi.ingsw.utils.messages.client.SelectMarketMessage;
import it.polimi.ingsw.utils.messages.server.DevelopmentDecksMessage;
import it.polimi.ingsw.utils.messages.server.FaithTrackMessage;
import it.polimi.ingsw.utils.messages.server.MarketMessage;
import it.polimi.ingsw.utils.messages.server.PlayerBoardSetupMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CLI {

    //private String nickname                   //TODO: spostato nella PlayerBoardView
    private int numberOfPlayers;                //TODO: aggiunta da controllare (non so se il numero di player può essere utile anche qua)
    private PlayerBoardView playerBoardView;
    private List<Object> opposingPlayerBoards;  //TODO: aggiunta da controllare
    private MarketView marketView;
    private List<DevelopmentDeckView> developmentDecks;        //TODO: aggiunta da controllare
    private FaithTrackView faithTrackView;
    private Scanner scanner;
    private GraphicalCLI graphicalCLI;
    private PacketHandler packetHandler;

    public CLI(){
        playerBoardView = new PlayerBoardView();
        opposingPlayerBoards = new ArrayList<>();
        marketView = new MarketView();
        developmentDecks = new ArrayList<>();   //TODO: aggiunta da controllare
        faithTrackView = new FaithTrackView();
        scanner = new Scanner(System.in);
        graphicalCLI = new GraphicalCLI();
        packetHandler = new PacketHandler(this);
    }

    public void setup() {
        //TODO: fase di setup per iniziare la partita
        while(!connect());
        askNickname();
    }

    private boolean connect(){

        System.out.println("IP address of server?");
        String ip = scanner.nextLine();

        return packetHandler.start(ip,Server.SOCKET_PORT);
    }

    public void askNickname(){
        System.out.println("Insert your Nickname");
        String nickname = scanner.nextLine();       //TODO: cambiato in var locale

        playerBoardView.setNickname(nickname);      //TODO: aggiunta da controllare
        packetHandler.sendConnectionMessage(nickname);
    }

    public void createNewLobby(){
        int size;
        System.out.println("There isnt's any player waiting for a match!");
        do {
            System.out.println("Insert the number of player that will play the game (between 1 and 4)");
            size = scanner.nextInt();
        }while(size <= 0 || size >= 5);
        setNumberOfPlayers(size);       //TODO: aggiunta da controllare
        packetHandler.sendNewGameSize(size);
    }

    public void setNumberOfPlayers(int numberOfPlayers){        //TODO: aggiunta da controllare
        this.numberOfPlayers = numberOfPlayers;
        if(numberOfPlayers == 1)
            opposingPlayerBoards.add(new LorenzoBoardView());
    }

    public void notifyNewPlayer(String nickname){
        if(!playerBoardView.getNickname().equals(nickname)) {       //TODO: modificato recuperando da playerBoard
            System.out.println("The player " + nickname + " has joined the game!! ♥");
        }else{
            System.out.println("You have been added to the game!! ♥");
        }
    }

    public void playerBoardSetup(PlayerBoardSetupMessage message){
        DevelopmentBoardView developmentBoard = new DevelopmentBoardView(message.getDevelopmentBSpaces()); //TODO: non è sempre vuoto all'inizio?
        LeaderBoardView leaderBoard = new LeaderBoardView();
        leaderBoard.setBoard(message.getLeaderBBoard()); //TODO: stessa cosa
        FaithBoardView faithBoard = new FaithBoardView(message.getFaithBFaith(), message.getFaithBPope());
        WarehouseView warehouse = new WarehouseView(message.getWarehouse());
        StrongboxView strongbox = new StrongboxView(message.getStrongbox()); //TODO: stessa cosa
        boolean inkwell = message.isFirstPlayer();

        if(playerBoardView.getNickname().equals(message.getNickname())){
            leaderBoard.setHand(message.getLeaderBHand());
            playerBoardView.setDevelopmentBoard(developmentBoard);
            playerBoardView.setLeaderBoard(leaderBoard);
            playerBoardView.setFaithBoard(faithBoard);
            playerBoardView.setWarehouse(warehouse);
            playerBoardView.setStrongbox(strongbox);
            playerBoardView.setInkwell(inkwell);
        } else {
            opposingPlayerBoards.add(new PlayerBoardView(message.getNickname()));
            int index = opposingPlayerBoards.size()-1;
            ((PlayerBoardView)opposingPlayerBoards.get(index)).setDevelopmentBoard(developmentBoard);
            ((PlayerBoardView)opposingPlayerBoards.get(index)).setLeaderBoard(leaderBoard);
            ((PlayerBoardView)opposingPlayerBoards.get(index)).setFaithBoard(faithBoard);
            ((PlayerBoardView)opposingPlayerBoards.get(index)).setWarehouse(warehouse);
            ((PlayerBoardView)opposingPlayerBoards.get(index)).setStrongbox(strongbox);
            ((PlayerBoardView)opposingPlayerBoards.get(index)).setInkwell(inkwell);
        }
    }

    private int opposingPlayerBoardOf(String nickname){ //TODO: può servire?
        for(int i=0; i<opposingPlayerBoards.size(); i++)
            if(((PlayerBoardView)opposingPlayerBoards.get(i)).getNickname().equals(nickname))
                return i;
        return -1;
    }

    public void marketSetup(MarketMessage message){
        updateMarket(message.getMarbleMatrix(), message.getFloatingMarble());
    }

    public void updateMarket(Marble[][] matrix, Marble floatingMarble){
        marketView.setMarbleMatrix(matrix);
        marketView.setFloatingMarble(floatingMarble);
        graphicalCLI.printMarket(marketView);  //TODO: da gestire, avremo strutture tutte nella CLI?
    }

    public void developmentDecksSetup(DevelopmentDecksMessage message){ //TODO: aggiunta da controllare
        for(DevelopmentDeck developmentDeck : message.getDevelopmentDecks())
            developmentDecks.add(new DevelopmentDeckView(developmentDeck.getDeck(), developmentDeck.getDeckColor(),
                    developmentDeck.getDeckLevel()));
    }

    public void faithTrackSetup(FaithTrackMessage message){     //TODO: aggiunta da controllare
        List<VaticanReportView> vaticanReports = new ArrayList<>();
        for(VaticanReport vaticanReport : message.getVaticanReports()){
            vaticanReports.add(new VaticanReportView(vaticanReport.getMin(), vaticanReport.getMax(),
                    vaticanReport.getPopeValue()));
        }
        faithTrackView.setFaithTrackView(vaticanReports, message.getFaithSpaces());
    }

    public void chooseAction() {        // TODO: scritto di fretta per testare
        int action = -1;
        while(action != 1) {
            System.out.println("1-market 2-development 3-productions");
            action = scanner.nextInt();
        }
        int column = 0;
        int row = -1;
        while(row < 0 || row > 3) {
            System.out.println("row number? (0 for column)");
            row = scanner.nextInt();
        }
        if(row == 0){
            while(column <= 0 || column > 4) {
                System.out.println("column number?");
                column = scanner.nextInt();
            }
        }
        packetHandler.sendMessage(new SelectMarketMessage(row-1, column-1));
    }
}
