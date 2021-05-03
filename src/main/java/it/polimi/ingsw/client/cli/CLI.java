package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.server.Server;

import java.util.Scanner;

public class CLI {

    private Scanner scanner;
    private PacketHandler packetHandler;

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
        connect();
        askNickname();
    }

    private void connect(){

        System.out.println("IP address of server?");
        String ip = scanner.nextLine();

        packetHandler.start(ip,Server.SOCKET_PORT);

    }

    public void askNickname(){
        System.out.println("Insert your Nickname");
        String nickname = scanner.nextLine();

        packetHandler.sendConnectionMessage(nickname);
    }
}
