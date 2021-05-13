package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.utils.messages.Message;
import it.polimi.ingsw.utils.messages.server.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PacketHandler {

    private CLI cli;
    private Socket server;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private final Queue<Message> messageQueue;

    private Thread packetReceiver;


    public PacketHandler(CLI cli) {
        this.cli = cli;
        this.messageQueue = new ConcurrentLinkedQueue<>();
    }


    public Queue<Message> getQueue() {
        return messageQueue;
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
            System.out.println("Server unreachable");
            return false;
        }
        System.out.println("Connected");

        try {
            output = new ObjectOutputStream(server.getOutputStream());
            input = new ObjectInputStream(server.getInputStream());

        } catch (IOException e) {
            System.out.println("Server has died");
        } catch (ClassCastException e) {
            System.out.println("Protocol violation");
        }
        return true;
    }

    private void managePackets(){
        try {
            while (true) {
                Object message;

                message = input.readObject();
  
                if (message instanceof ServerActionMessage) {
                    messageQueue.add((ServerActionMessage) message);
                } else {

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