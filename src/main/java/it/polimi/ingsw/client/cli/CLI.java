package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.structures.FaithTrackView;
import it.polimi.ingsw.client.structures.LorenzoBoardView;
import it.polimi.ingsw.client.structures.MarketView;
import it.polimi.ingsw.client.structures.PlayerBoardView;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.market.Marble;
import it.polimi.ingsw.utils.messages.client.SelectMarketMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CLI {

    //private String nickname                   //TODO: spostato nella PlayerBoardView
    private int numberOfPlayers;                //TODO: aggiunta da controllare (non so se il numero di player può essere utile anche qua)
    private PlayerBoardView playerBoardView;
    private List<Object> opposingPlayerBoards;  //TODO: aggiunta da controllare
    private MarketView marketView;
    private FaithTrackView faithTrackView;
    private Scanner scanner;
    private GraphicalCLI graphicalCLI;
    private PacketHandler packetHandler;

    public CLI(){
        playerBoardView = new PlayerBoardView();
        opposingPlayerBoards = new ArrayList<>();
        marketView = new MarketView();
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
            opposingPlayerBoards.add(new PlayerBoardView(nickname));        //TODO: aggiunta da controllare
        }else{
            System.out.println("You have been added to the game!! ♥");
        }
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

    public void updateMarket(Marble[][] matrix, Marble floatingMarble){
        marketView.setMarbleMatrix(matrix);
        marketView.setFloatingMarble(floatingMarble);
        graphicalCLI.printMarket(marketView);  //TODO: da gestire, avremo strutture tutte nella CLI?
    }
}
