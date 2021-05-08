package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.utils.messages.Message;
import it.polimi.ingsw.utils.messages.client.ConnectionMessage;
import it.polimi.ingsw.utils.messages.client.NewLobbyMessage;
import it.polimi.ingsw.utils.messages.server.*;
import it.polimi.ingsw.utils.messages.server.ack.ConnectionAckMessage;
import it.polimi.ingsw.utils.messages.server.ack.LeaderCardDiscardAckMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PacketHandler {

    private CLI cli;
    private Socket server;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private Thread packetReceiver;

    public PacketHandler(CLI cli) {
        this.cli = cli;
    }

    public boolean start(String address, int port) {
        if(!connect(address, port)){
            return false;
        }

        packetReceiver = new Thread(this::managePackets);
        packetReceiver.start();
        return true;
    }

    public boolean connect(String address, int port) {
        try {
            server = new Socket(address, port);
        } catch (IOException e) {
            System.out.println("server unreachable");
            return false;
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
        return true;
    }

    public void sendConnectionMessage(String nickname){

        try{
            output.writeObject(new ConnectionMessage(nickname));
            output.reset();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void sendNewGameSize(int size){

        try{
            output.writeObject(new NewLobbyMessage(size));
            output.reset();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    private void managePackets(){
        try {
            while (true) {
                Object message;

                message = input.readObject();

                //TODO: inserire tutti i casi di pachetti che possiamo ricevere

                if (message instanceof ConnectionAckMessage) {
                    if (!((ConnectionAckMessage) message).isState()) {
                        System.out.println("Nickname is not available, please choose another one");
                        cli.askNickname();
                    }
                } else if (message instanceof LobbyMessage) {
                    if (((LobbyMessage) message).getLobbySize() == ((LobbyMessage) message).getWaitingPlayers())
                        cli.createNewLobby();
                    else cli.setNumberOfPlayers(((LobbyMessage) message).getLobbySize());   //TODO: aggiunta da controllare
                } else if (message instanceof NewPlayerMessage) {
                    cli.notifyNewPlayer(((NewPlayerMessage) message).getPlayerNickname());
                } else if (message instanceof PlayerBoardSetupMessage) {
                    cli.playerBoardSetup((PlayerBoardSetupMessage) message);
                } else if (message instanceof MarketMessage) {
                    cli.marketSetup(((MarketMessage) message));
                } else if (message instanceof DevelopmentDecksMessage) {
                    cli.developmentDecksSetup(((DevelopmentDecksMessage) message));
                } else if (message instanceof FaithTrackMessage) {
                    cli.faithTrackSetup((FaithTrackMessage) message);
                } else if (message instanceof AskLeaderCardDiscardMessage) {
                    cli.askDiscardLeader();
                } else if (message instanceof LeaderCardDiscardAckMessage) {
                    if(!((LeaderCardDiscardAckMessage) message).isState()){
                        //TODO: richiedo?
                        System.out.println("Errore nello scarto dei leader");
                    } else {
                        //TODO: aggiorno?
                        System.out.println("Scarto dei leader a buon fine");
                    }
                } else if (message instanceof PlayerLeaderBBoardMessage) {
                    //TODO: stampare la board delle leader
                }else if (message instanceof PlayerLeaderBHandMessage) {
                    //TODO: da sistemare, ricevo la hand di tutti i player della lobby, ma dovrebbe arrivare solo se la mia hand
                    cli.updateLeaderHand(((PlayerLeaderBHandMessage)message).getHand());
                }else {
                    System.out.println("Received " + message.toString());
                }
            }
        }catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message) {    // TODO: scritto di fretta per testare
        try{
            output.writeObject(message);
            output.reset();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}