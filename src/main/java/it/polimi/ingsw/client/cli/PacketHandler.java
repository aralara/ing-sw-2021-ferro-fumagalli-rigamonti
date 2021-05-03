package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.utils.messages.client.ConnectionMessage;
import it.polimi.ingsw.utils.messages.client.NewLobbyMessage;
import it.polimi.ingsw.utils.messages.server.LobbyMessage;
import it.polimi.ingsw.utils.messages.server.ack.ActivateProductionsAckMessage;
import it.polimi.ingsw.utils.messages.server.ack.ConnectionAckMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class PacketHandler {

    private CLI cli;
    private Scanner scanner;
    private Socket server;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private Thread packetReceiver;

    public PacketHandler(CLI cli) {
        scanner = new Scanner(System.in);
        this.cli = cli;
    }

    public void start(String address, int port) {
        connect(address, port);

        packetReceiver = new Thread(this::managePackets);
        packetReceiver.start();

    }

    public void connect(String address, int port) {
        try {
            server = new Socket(address, port);
        } catch (IOException e) {
            System.out.println("server unreachable");
            return;
        }
        System.out.println("Connected");

        try {
            output = new ObjectOutputStream(server.getOutputStream());
            input = new ObjectInputStream(server.getInputStream());

        } catch (IOException e) {
            System.out.println("server has died");
        } catch (ClassCastException e) {
            System.out.println("protocol violation");
        }
    }

    public void sendConnectionMessage(String nickname){

        try{
            output.writeObject(new ConnectionMessage(nickname));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void sendNewGameSize(int size){

        try{
            output.writeObject(new NewLobbyMessage(size));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    private void managePackets(){
        while(true){
            Object temp;
            try {
                temp = input.readObject();

                //TODO: inserire tutti i casi di pachetti che poissiamo ricevere

                if (temp instanceof ConnectionAckMessage) {
                    if(((ConnectionAckMessage) temp).isState()){
                        System.out.println("riceve ack funziona");
                    }
                    else{
                        System.out.println("nick non va bene");
                        cli.askNickname();
                    }

                } else if (temp instanceof LobbyMessage) {
                    if(((LobbyMessage) temp).getLobbySize() == ((LobbyMessage) temp).getWaitingPlayers()){
                        cli.createNewLobby();
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
