package it.polimi.ingsw.client;

import it.polimi.ingsw.utils.messages.Message;
import it.polimi.ingsw.utils.messages.server.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageHandler implements Runnable{

    private Socket server;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private final ClientController client;
    private final Queue<ServerActionMessage> messageQueue;

    private boolean active;


    public MessageHandler(ClientController client) {
        this.client = client;
        this.messageQueue = new ConcurrentLinkedQueue<>();
        active = false;
    }


    public Queue<ServerActionMessage> getQueue() {
        return messageQueue;
    }

    @Override
    public void run() {
        while(active)
            managePackets();
    }

    public boolean connect(String address, int port) {
        try {
            server = new Socket(address, port);
        } catch (IOException e) {
            return false;
        }

        try {
            output = new ObjectOutputStream(server.getOutputStream());
            input = new ObjectInputStream(server.getInputStream());

        } catch (IOException | ClassCastException e) {
            e.printStackTrace();
        }
        active = true;
        return true;
    }

    private void managePackets(){
        try {
            Object message;

            message = input.readObject();

            if(message instanceof ServerUpdateMessage)  //TODO: gestione alternativa (forse più corretta) metterli nella queue e lasciare che il client si costruisca un thread a parte per leggerli asincronamente
                ((ServerUpdateMessage) message).doUpdate(client);
            else if(message instanceof ServerActionMessage)
                messageQueue.add((ServerActionMessage) message);
            else
                System.out.println("Received " + message.toString());
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

    public void stop() {
        active = false;
    }
}