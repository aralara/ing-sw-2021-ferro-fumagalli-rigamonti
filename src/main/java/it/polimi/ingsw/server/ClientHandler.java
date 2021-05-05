package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.utils.messages.Message;
import it.polimi.ingsw.utils.messages.client.*;
import it.polimi.ingsw.utils.messages.server.NewPlayerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private final Socket client;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;
    private final Controller controller;

    ClientHandler(Socket client, ObjectOutputStream out, ObjectInputStream in, Controller controller) {
        this.client = client;
        this.output = out;
        this.input = in;
        this.controller = controller;
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
        System.out.println("Connected to " + client.getInetAddress());


        Object message;

        try {
            while (true){  //TODO: quando bisogna chiudere la connessione?
                message = input.readObject();
                if (message instanceof ActivateProductionsMessage) {
                    //((Message)message).doAction(controller.getGame());
                } else if (message instanceof BuyDevelopmentCardMessage){

                }else if (message instanceof EndTurnMessage){

                }else if (message instanceof LeaderCardDiscardMessage){

                }else if (message instanceof LeaderCardPlayMessage){

                }else if (message instanceof NewLobbyMessage){

                }else if (message instanceof RequestResourcesMessage){

                }else if (message instanceof SelectMarketMessage){

                }else if (message instanceof ShelvesConfigurationMessage){

                }
            }
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //TODO: da completare con codice client
    }

    public void sendMessage(Message message){
        try{
            output.writeObject(message);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
