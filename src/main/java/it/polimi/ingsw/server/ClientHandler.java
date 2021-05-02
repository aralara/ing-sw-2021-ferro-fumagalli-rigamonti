package it.polimi.ingsw.server;

import it.polimi.ingsw.utils.messages.Message;
import it.polimi.ingsw.utils.messages.client.*;
import it.polimi.ingsw.utils.messages.server.NewPlayerMessage;
import it.polimi.ingsw.utils.messages.server.ack.ActivateProductionsAckMessage;
import it.polimi.ingsw.utils.messages.server.ack.ConnectionAckMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ClientHandler implements Runnable{

    private Socket client;

    ClientHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run()
    {
        try {
            handleClientConnection();
        } catch (IOException e) {
            System.out.println("client " + client.getInetAddress() + " connection dropped");
        }
    }

    private void handleClientConnection() throws IOException{
        System.out.println("Connected to " + client.getInetAddress());

        ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream input = new ObjectInputStream(client.getInputStream());

        Object temp;    //TODO: fa schifo? ricezione pacchetti?

        try {
            while (true){  //TODO: quando bisogna chiudere la connessione?
                temp = input.readObject();
                if (temp instanceof ActivateProductionsMessage) {

                } else if (temp instanceof BuyDevelopmentCardMessage){

                }else if (temp instanceof ConnectionMessage){
                    //TODO: server controlla se user disponibile
                    System.out.println("Benvenuto "+ ((ConnectionMessage) temp).getNickname());
                    output.writeObject(new ConnectionAckMessage(true));  //TODO:inserire controllo per user valido
                    //output.writeObject(new NewPlayerMessage(((ConnectionMessage) temp).getNickname()));
                    //TODO: inserito per prova, da controllare
                }else if (temp instanceof EndTurnMessage){

                }else if (temp instanceof LeaderCardDiscardMessage){

                }else if (temp instanceof LeaderCardPlayMessage){

                }else if (temp instanceof NewLobbyMessage){

                }else if (temp instanceof RequestResourcesMessage){

                }else if (temp instanceof SelectMarketMessage){

                }else if (temp instanceof ShelvesConfigurationMessage){

                }
            }
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //TODO: da completare con codice client
    }
}
