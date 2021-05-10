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

    private void managePackets(){
        try {
            while (true) {
                Object message;

                message = input.readObject();

                if (message instanceof ConnectionAckMessage) {
                    if (!((ConnectionAckMessage) message).isState()) {
                        System.out.println("Nickname is not available, please choose another one");
                        cli.askNickname();
                    }
                } else if (message instanceof LobbyMessage) {
                    int size = ((LobbyMessage) message).getLobbySize();
                    int waitingPlayers = ((LobbyMessage) message).getWaitingPlayers();
                    if (size == waitingPlayers)
                        cli.createNewLobby();
                    else {
                        System.out.print("There's already a " + size + " player lobby waiting for ");
                        if(size-waitingPlayers==1)
                            System.out.println("another player");
                        else System.out.println((size-waitingPlayers) + " more players");
                        cli.setNumberOfPlayers(((LobbyMessage) message).getLobbySize());   //TODO: serve numero giocatori?
                    }
                } else if (message instanceof NewPlayerMessage) {
                    cli.notifyNewPlayer(((NewPlayerMessage) message).getPlayerNickname());
                } else if (message instanceof PlayerBoardSetupMessage) {
                    cli.playerBoardSetup((PlayerBoardSetupMessage) message);
                } else if (message instanceof MarketMessage) {
                    cli.updateMarket(((MarketMessage) message));
                } else if (message instanceof DevelopmentDecksMessage) {
                    cli.developmentDecksSetup(((DevelopmentDecksMessage) message));
                } else if (message instanceof FaithTrackMessage) {
                    cli.faithTrackSetup((FaithTrackMessage) message);
                } else if (message instanceof AskLeaderCardDiscardMessage) {
                    cli.askDiscardLeader();
                } else if (message instanceof LeaderCardDiscardAckMessage) {
                    if(!((LeaderCardDiscardAckMessage) message).isState()){
                        //TODO: richiedo?
                        System.out.println("Something went wrong, please try again");
                        cli.askDiscardLeader();
                    }
                } else if (message instanceof PlayerLeaderBBoardMessage) {
                    //TODO: stampare la board delle leader
                }else if (message instanceof PlayerLeaderBHandMessage) {
                    cli.updateLeaderHand((PlayerLeaderBHandMessage)message); //TODO: il messaggio arriva due volte anche scartando i leader insieme, va bene?
                }else {
                    System.out.println("Received " + message.toString());
                }
            }
        }catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message) {
        try{
            output.writeObject(message);
            output.reset();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}