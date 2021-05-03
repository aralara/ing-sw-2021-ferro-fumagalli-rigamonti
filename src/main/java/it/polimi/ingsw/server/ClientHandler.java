package it.polimi.ingsw.server;

import it.polimi.ingsw.utils.messages.client.*;
import it.polimi.ingsw.utils.messages.server.NewPlayerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private Socket client;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    ClientHandler(Socket client, ObjectOutputStream out, ObjectInputStream in) {
        this.client = client;
        this.output = out;
        this.input = in;
    }

    public Socket getSocket() {
        return client;
    }

    public ObjectOutputStream getOutput() {
        return output;
    }

    public ObjectInputStream getInput() {
        return input;
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
        System.out.println("Connected to " + client.getInetAddress()); //TODO: da togliere


        Object temp;    //TODO: fa schifo? ricezione pacchetti?

        try {
            while (true){  //TODO: quando bisogna chiudere la connessione?
                temp = input.readObject();
                if (temp instanceof ActivateProductionsMessage) {

                } else if (temp instanceof BuyDevelopmentCardMessage){

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

    public void notifyNewPlayer(String nickname){
        try{
            output.writeObject(new NewPlayerMessage(nickname));
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
