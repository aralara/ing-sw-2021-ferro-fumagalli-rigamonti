package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.utils.messages.client.SelectMarketMessage;

import java.util.Scanner;

public class CLI {

    private Scanner scanner;
    private PacketHandler packetHandler;
    private String nickname;

    CLI(){
        scanner = new Scanner(System.in);
        packetHandler = new PacketHandler(this);
        setup();
    }

    public static void main(String[] args) {
        CLI cli = new CLI();
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
        nickname = scanner.nextLine();

        packetHandler.sendConnectionMessage(nickname);
    }

    public void createNewLobby(){
        int size;
        System.out.println("There isnt's any player waiting for a match!");
        do {
            System.out.println("Insert the number of player that will play the game (between 1 and 4)");
            size = scanner.nextInt();
        }while(size <= 0 || size >= 5);
        packetHandler.sendNewGameSize(size);
    }

    public void notifyNewPlayer(String nickname){
        if(!this.nickname.equals(nickname)) {
            System.out.println("The player " + nickname + " has joined the game!! ♥");
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
}
